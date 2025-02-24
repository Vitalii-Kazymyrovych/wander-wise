package wander.wise.application.service.api.ai;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATION_EIGHTH_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATION_FIFTH_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATION_FIRST_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATION_FOURTH_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATION_SECOND_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATION_SEVENTH_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATION_SIXTH_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATION_THIRD_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CLIMATE_LIST;
import static wander.wise.application.constants.AiApiServiceConstants.FULL_NAME_FORMAT;
import static wander.wise.application.constants.AiApiServiceConstants.LIST_FORMATING_RULES;
import static wander.wise.application.constants.AiApiServiceConstants.LOCATION_NAMES_FIELD_FORMAT;
import static wander.wise.application.constants.AiApiServiceConstants.NON_EXISTING_RESTRICT;
import static wander.wise.application.constants.AiApiServiceConstants.REGION_AND_CONTINENT_DTO_EXAMPLE;
import static wander.wise.application.constants.AiApiServiceConstants.REGION_EXAMPLES;
import static wander.wise.application.constants.AiApiServiceConstants.SPECIAL_REQUIREMENTS_LIST;
import static wander.wise.application.constants.AiApiServiceConstants.SPECIFIC_LOCATION_EXAMPLES;
import static wander.wise.application.constants.AiApiServiceConstants.TRIP_TYPES_LIST;
import static wander.wise.application.constants.GlobalConstants.RM_DIVIDER;
import static wander.wise.application.constants.GlobalConstants.SEPARATOR;
import static wander.wise.application.constants.GlobalConstants.SET_DIVIDER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;
import wander.wise.application.dto.ai.AiResponseDto;
import wander.wise.application.dto.ai.CheckedLocation;
import wander.wise.application.dto.ai.FullNameDto;
import wander.wise.application.dto.ai.LocationListDto;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.dto.card.CreateCardRequestDto;
import wander.wise.application.dto.card.RegionAndContinentDto;
import wander.wise.application.exception.custom.AiServiceException;

@Service
@RequiredArgsConstructor
public class AiApiServiceImpl implements AiApiService {
    private static final int TOTAL_REQUIRED_RESPONSES_AMOUNT = 20;
    private static final int TRAVEL_SCALE_INDEX = 0;
    private static final int POPULATED_LOCALITY_INDEX = 0;
    private static final int COUNTRY_INDEX = 1;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<AiResponseDto> getAiResponses(CardSearchParameters searchParams,
                                              Map<String, List<String>> excludeMap) {
        List<AiResponseDto> responses = new ArrayList<>();
        generateLocations(searchParams, excludeMap, responses);
        return rmExtraData(responses);
    }

    @Override
    public CardSearchParameters defineRegion(CardSearchParameters searchParams) {
        String paramsJson = objectToJson(searchParams);
        String defineRegionPrompt = getDefineRegionPrompt(searchParams, paramsJson);
        String response = chatClient.call(defineRegionPrompt);
        return (CardSearchParameters) jsonToObject(response, CardSearchParameters.class);
    }

    @Override
    public CardSearchParameters defineContinent(CardSearchParameters searchParams) {
        String paramsJson = objectToJson(searchParams);
        String detectDistancePrompt = getDefineContinentPrompt(searchParams, paramsJson);
        String response = chatClient.call(detectDistancePrompt);
        return (CardSearchParameters) jsonToObject(response, CardSearchParameters.class);
    }

    @Override
    public CreateCardRequestDto defineRegionAndContinent(CreateCardRequestDto requestDto) {
        String defineRegionPrompt = getDefineRegionAndContinentPrompt(
                requestDto.populatedLocality(),
                requestDto.country());
        RegionAndContinentDto regionAndContinentDto = (RegionAndContinentDto) jsonToObject(
                chatClient.call(defineRegionPrompt),
                RegionAndContinentDto.class);
        return requestDto.setRegionAndContinent(
                regionAndContinentDto.region(),
                regionAndContinentDto.continent());

    }

    private void generateLocations(CardSearchParameters searchParams,
                                   Map<String, List<String>> excludeMap,
                                   List<AiResponseDto> responses) {
        String excludeTotal = getExcludeTotal(excludeMap);
        List<Future<List<AiResponseDto>>> futures = new ArrayList<>();
        ExecutorService executorService = newFixedThreadPool(excludeMap.keySet().size());
        excludeMap.keySet().forEach(type -> {
            Future<List<AiResponseDto>> responsesByType
                    = executorService.submit(new LocationGenerator(
                    excludeMap, type, searchParams, excludeTotal));
            futures.add(responsesByType);
        });
        for (Future<List<AiResponseDto>> future : futures) {
            try {
                responses.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        executorService.shutdown();
    }

    private static String getDefineRegionPrompt(CardSearchParameters searchParams,
                                                String paramsJson) {
        return new StringBuilder()
                .append("I have this json object with search parameters: ")
                .append(paramsJson)
                .append(SEPARATOR)
                .append("Find in which region(part) of ")
                .append(searchParams.startLocation().split(",")[COUNTRY_INDEX])
                .append(" the ")
                .append(searchParams.startLocation().split(",")[POPULATED_LOCALITY_INDEX])
                .append(" is situated. ")
                .append(REGION_EXAMPLES)
                .append(SEPARATOR)
                .append("Set name of this region in travel distance ")
                .append("field and return new object in the same format.")
                .append(SEPARATOR)
                .append("It is important to use local name of the region. ")
                .append("Good examples: Île-de-France, Kharkiv oblast, New York, ect.")
                .toString();
    }

    private static String getDefineContinentPrompt(CardSearchParameters searchParams,
                                                   String paramsJson) {
        return new StringBuilder()
                .append("I have this json object with search parameters: ")
                .append(paramsJson)
                .append(SEPARATOR)
                .append("Find on what continent is ")
                .append(searchParams.startLocation().split(",")[COUNTRY_INDEX])
                .append(" located?, set name of this continent in travel distance ")
                .append("field and return new object in the same format.")
                .toString();
    }

    private static String getDefineRegionAndContinentPrompt(String populatedLocality, String country) {
        return new StringBuilder()
                .append("Find in which region(part) of ")
                .append(country)
                .append(" the ")
                .append(populatedLocality)
                .append(" is situated. ")
                .append(REGION_EXAMPLES)
                .append(SEPARATOR)
                .append("Set name of this region in the according field.")
                .append(SEPARATOR)
                .append("It is important to use local name of the region. ")
                .append("Good examples: Île-de-France, Kharkiv oblast, New York, ect.")
                .append(SEPARATOR)
                .append("Then ")
                .append("Find on what continent is ")
                .append(country)
                .append(" located?, set name of this continent in the according field.")
                .append(SEPARATOR)
                .append("Return a json object in this format: ")
                .append(SEPARATOR)
                .append(REGION_AND_CONTINENT_DTO_EXAMPLE)
                .toString();
    }

    private static String getExcludeTotal(Map<String, List<String>> excludeMap) {
        return excludeMap.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.joining(", "));
    }

    private String objectToJson(Object object) {
        String paramsJson = null;
        try {
            paramsJson = objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            String className = getClassName(object.getClass());
            throw new AiServiceException(
                    "Can't convert " + className + " to JSON", e);
        }
        return paramsJson;
    }

    private Object jsonToObject(String json, Class clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            String className = getClassName(clazz);
            throw new AiServiceException(
                    "Ai returned incorrect json: " + json
                            + SEPARATOR
                            + "Cant convert it to " + className, e);
        }
    }

    private static String getClassName(Class clazz) {
        String className = clazz.getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        return className;
    }

    private static List<AiResponseDto> rmExtraData(List<AiResponseDto> responses) {
        responses = responses.stream()
                .map(response -> {
                    String[] fullNameArray = response.fullName().split(RM_DIVIDER);
                    String correctName = fullNameArray[0];
                    if (fullNameArray[0].contains("(")) {
                        correctName = fullNameArray[0].substring(0, fullNameArray[0].indexOf("(") - 1);
                    }
                    fullNameArray[0] = correctName;
                    return response.setFullName(String.join(SET_DIVIDER, fullNameArray));
                })
                .toList();
        return responses;
    }

    private class LocationGenerator implements Callable<List<AiResponseDto>> {
        private final Map<String, List<String>> excludeMap;
        private final String tripType;
        private final CardSearchParameters searchParams;
        private final String excludeTotal;

        private LocationGenerator(
                Map<String, List<String>> excludeMap,
                String tripType,
                CardSearchParameters searchParams,
                String excludeTotal) {
            this.excludeMap = excludeMap;
            this.tripType = tripType;
            this.searchParams = searchParams;
            this.excludeTotal = excludeTotal;
        }

        @Override
        public List<AiResponseDto> call() {
            List<AiResponseDto> responses = List.of();
            String excludeByType = getExcludeByType(excludeMap, tripType);
            Set<String> locationList = getLocationList(searchParams, excludeMap,
                    excludeByType, excludeTotal);
            if (!locationList.isEmpty()) {
                responses = getResponses(searchParams, tripType, locationList);
            }
            return responses;
        }

        private Set<String> getLocationList(
                CardSearchParameters searchParams, Map<String,
                List<String>> excludeMap, String excludeByType, String excludeTotal) {
            String locationListPrompt = getLocationListPrompt(searchParams, excludeByType,
                    tripType, getResponsesAmount(excludeMap));
            String locationList = chatClient.call(locationListPrompt);
            locationList = setFullNames(locationList, searchParams);
            locationList = check(locationList, excludeTotal);
            LocationListDto locationListDto = (LocationListDto) jsonToObject(
                    locationList, LocationListDto.class);
            return locationListDto.locationNames()
                    .stream()
                    .filter(name -> !excludeTotal.contains(name))
                    .collect(Collectors.toSet());
        }

        private String setFullNames(String locationList, CardSearchParameters searchParams) {
            LocationListDto listDto = (LocationListDto) jsonToObject(locationList, LocationListDto.class);
            Set<Future<String>> fullNameFutures = new HashSet<>();
            Set<String> fullNames = new HashSet<>();
            ExecutorService executorService = newFixedThreadPool(listDto.locationNames().size());
            for (String name : listDto.locationNames()) {
                Future<String> fullName = executorService.submit(new FullNamesGenerator(name, searchParams));
                fullNameFutures.add(fullName);
            }
            for (Future<String> fullNameFuture : fullNameFutures) {
                try {
                    fullNames.add(fullNameFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            executorService.shutdown();
            return objectToJson(new LocationListDto(fullNames));
        }

        private String getLocationListPrompt(CardSearchParameters searchParams,
                                             String locationsToExclude, String tripType,
                                             int responsesAmount) {
            return new StringBuilder().append("I am in ").append(searchParams.startLocation())
                    .append(SEPARATOR).append("Find me ").append(responsesAmount)
                    .append(" locations, where to travel by this requirements: ")
                    .append(SEPARATOR)
                    .append("Trip type: ").append(tripType)
                    .append(SEPARATOR)
                    .append("Climate: ")
                    .append(String.join(", ", searchParams.climate()))
                    .append("Special requirements: ")
                    .append(String.join(", ", searchParams.specialRequirements()))
                    .append(SEPARATOR)
                    .append("The result should not contain these locations: ")
                    .append(locationsToExclude)
                    .append(SEPARATOR)
                    .append("Locations must be within: ")
                    .append(searchParams.travelDistance()[TRAVEL_SCALE_INDEX])
                    .append(". Collect locations from different parts of it. It is very "
                            + "important to fill the list with locations all around ")
                    .append(searchParams.travelDistance()[TRAVEL_SCALE_INDEX])
                    .append(SEPARATOR)
                    .append("It is very important to find specific locations. ")
                    .append(SPECIFIC_LOCATION_EXAMPLES)
                    .append(SEPARATOR)
                    .append("Result should contain at least ").append(responsesAmount)
                    .append(" locations. Better find more than ").append(responsesAmount)
                    .append(".")
                    .append(SEPARATOR)
                    .append(NON_EXISTING_RESTRICT)
                    .append(SEPARATOR)
                    .append(LIST_FORMATING_RULES)
                    .append(SEPARATOR)
                    .append("{")
                    .append(SEPARATOR)
                    .append(LOCATION_NAMES_FIELD_FORMAT)
                    .append(SEPARATOR)
                    .append("}")
                    .toString();
        }

        private String check(String locationList, String excludeTotal) {
            LocationListDto listDto = (LocationListDto) jsonToObject(
                    locationList, LocationListDto.class);
            Set<Future<String>> checkedLocationFutures = new HashSet<>();
            ExecutorService executorService = newFixedThreadPool(listDto.locationNames().size());
            for (String name : listDto.locationNames()) {
                Future<String> checkedLocation = executorService.submit(new LocationChecker(
                        name, excludeTotal));
                checkedLocationFutures.add(checkedLocation);
            }
            String checkedLocations = objectToJson(checkedFutureSetToLocationList(checkedLocationFutures));
            executorService.shutdown();
            return checkedLocations;
        }

        private List<AiResponseDto> getResponses(CardSearchParameters searchParams,
                                                 String tripType, Set<String> locationList) {
            ExecutorService executorService = newFixedThreadPool(locationList.size());
            List<Future<AiResponseDto>> responseFutures = new LinkedList<>();
            List<AiResponseDto> responses = new LinkedList<>();
            for (String locationName : locationList) {
                Future<AiResponseDto> response = executorService.submit(
                        new LocationDetailsGenerator(locationName, searchParams, tripType));
                responseFutures.add(response);
            }
            for (Future<AiResponseDto> responseFuture : responseFutures) {
                try {
                    responses.add(responseFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            executorService.shutdown();
            return responses;
        }

        private static LocationListDto checkedFutureSetToLocationList(
                Set<Future<String>> checkedLocations) {
            return new LocationListDto(checkedLocations.stream()
                    .map(stringFuture -> {
                        try {
                            return stringFuture.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(name -> !name.isEmpty())
                    .collect(Collectors.toSet()));
        }

        private static String getExcludeByType(Map<String, List<String>> excludeMap,
                                               String tripType) {
            return excludeMap.get(tripType).toString();
        }

        private static int getResponsesAmount(Map<String, List<String>> excludeMap) {
            return TOTAL_REQUIRED_RESPONSES_AMOUNT / excludeMap.size();
        }
    }

    private class FullNamesGenerator implements Callable<String> {
        private final String name;
        private final CardSearchParameters searchParams;

        private FullNamesGenerator(String name, CardSearchParameters searchParams) {
            this.name = name;
            this.searchParams = searchParams;
        }

        @Override
        public String call() throws Exception {
            String fullNamePrompt = getFullNamePrompt(name, searchParams);
            FullNameDto fullNameDto = (FullNameDto) jsonToObject(chatClient.call(fullNamePrompt), FullNameDto.class);
            return new StringBuilder()
                    .append(fullNameDto.name())
                    .append(SET_DIVIDER)
                    .append(fullNameDto.populatedLocality())
                    .append(SET_DIVIDER)
                    .append(fullNameDto.region())
                    .append(SET_DIVIDER)
                    .append(fullNameDto.country())
                    .append(SET_DIVIDER)
                    .append(fullNameDto.continent())
                    .toString();
        }

        private String getFullNamePrompt(String name, CardSearchParameters searchParams) {
            return new StringBuilder()
                    .append("I have the place, named: ")
                    .append(name)
                    .append(", which situated in ")
                    .append(searchParams.travelDistance()[TRAVEL_SCALE_INDEX])
                    .append(SEPARATOR)
                    .append("Help me specify this place location. ")
                    .append("Return answer in this json format: ")
                    .append(SEPARATOR)
                    .append(FULL_NAME_FORMAT)
                    .toString();
        }
    }

    private class LocationChecker implements Callable<String> {
        private final String locationName;
        private final String excludeTotal;

        private LocationChecker(String locationName, String excludeTotal) {
            this.locationName = locationName;
            this.excludeTotal = excludeTotal;
        }

        @Override
        public String call() throws Exception {
            String checkPorompt = getCheckPrompt(locationName, excludeTotal);
            CheckedLocation checkedLocation = (CheckedLocation) jsonToObject(
                    chatClient.call(checkPorompt), CheckedLocation.class);
            return checkedLocation.locationName();
        }

        private String getCheckPrompt(String locationName, String excludeTotal) {
            String[] locationNameArray = locationName.split(RM_DIVIDER);
            return new StringBuilder().append("I have this location: ")
                    .append(SEPARATOR)
                    .append(locationName)
                    .append(SEPARATOR)
                    .append("I need you to fix potential mistakes in it")
                    .append(" by next algorithm: ")
                    .append(SEPARATOR)
                    .append(String.format(CHECK_LOCATION_FIRST_RULE, excludeTotal))
                    .append(SEPARATOR)
                    .append(CHECK_LOCATION_SECOND_RULE)
                    .append(SEPARATOR)
                    .append(String.format(CHECK_LOCATION_THIRD_RULE,
                            locationNameArray[0],
                            locationNameArray[1]))
                    .append(SEPARATOR)
                    .append(String.format(CHECK_LOCATION_FOURTH_RULE,
                            locationNameArray[1],
                            locationNameArray[2]))
                    .append(SEPARATOR)
                    .append(String.format(CHECK_LOCATION_FIFTH_RULE,
                            locationNameArray[2],
                            locationNameArray[3]))
                    .append(SEPARATOR)
                    .append(String.format(CHECK_LOCATION_SIXTH_RULE,
                            locationNameArray[3],
                            locationNameArray[4]))
                    .append(SEPARATOR)
                    .append(CHECK_LOCATION_SEVENTH_RULE)
                    .append(SEPARATOR)
                    .append(CHECK_LOCATION_EIGHTH_RULE)
                    .toString();
        }
    }

    private class LocationDetailsGenerator implements  Callable<AiResponseDto> {
        private final String locationName;
        private final CardSearchParameters searchParams;
        private final String tripType;

        private LocationDetailsGenerator(String locationName, CardSearchParameters searchParams,
                                         String tripType) {
            this.locationName = locationName;
            this.searchParams = searchParams;
            this.tripType = tripType;
        }

        @Override
        public AiResponseDto call() throws Exception {
            AiResponseDto aiResponse = generateLocationDetails(locationName);
            return completeResponse(searchParams, tripType, aiResponse);
        }

        private AiResponseDto generateLocationDetails(String name) {
            String locationDetailsPrompt = getLocationDetailsPrompt(name);
            String locationDetails = chatClient.call(locationDetailsPrompt);
            AiResponseDto responseDto = (AiResponseDto) jsonToObject(locationDetails, AiResponseDto.class);
            responseDto = responseDto.setFullName(name);
            return responseDto;
        }

        private String getLocationDetailsPrompt(String locationName) {
            StringBuilder locationDetailsPrompt = new StringBuilder();
            locationDetailsPrompt.append("I want to know more about this location: ")
                    .append(locationName)
                    .append(SEPARATOR)
                    .append("Give me answer as a json object. Use this format: ")
                    .append(SEPARATOR)
                    .append("{").append(SEPARATOR)
                    .append("\"fullName\": ").append("Provided location name without changes")
                    .append(SEPARATOR)
                    .append("\"tripTypes\": ").append("\"(add several from this list: ")
                    .append(TRIP_TYPES_LIST)
                    .append(". Use | between points.")
                    .append(")\",")
                    .append(SEPARATOR)
                    .append("\"climate\": \"one from this list: ").append(CLIMATE_LIST)
                    .append(")\",")
                    .append(SEPARATOR)
                    .append("\"specialRequirements\": \"(add some from this list: ")
                    .append(SPECIAL_REQUIREMENTS_LIST)
                    .append(" Use | between points)\",")
                    .append(SEPARATOR)
                    .append("\"description\": \"(2-3 sentences)\",")
                    .append(SEPARATOR)
                    .append("\"whyThisPlace\": \"reason 1|reason 2|reason 3|")
                    .append(" (3-5 words per reason)\"")
                    .append(SEPARATOR)
                    .append("}");
            return locationDetailsPrompt.toString();
        }

        private static AiResponseDto completeResponse(
                CardSearchParameters searchParams, String tripType, AiResponseDto response) {
            if (!response.tripTypes().contains(tripType)) {
                response = response.setTripTypes(response.tripTypes() + SET_DIVIDER + tripType);
            }
            for (String specialRequirement : searchParams.specialRequirements()) {
                if (!response.specialRequirements().contains(specialRequirement)) {
                    response = response.setSpecialRequirements(
                            response.specialRequirements()
                                    + SET_DIVIDER
                                    + specialRequirement);
                }
            }
            return response;
        }
    }
}
