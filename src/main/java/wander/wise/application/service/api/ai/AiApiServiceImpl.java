package wander.wise.application.service.api.ai;

import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATIONS_FIRST_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATIONS_SECOND_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CHECK_LOCATIONS_THIRD_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.CLIMATE_LIST;
import static wander.wise.application.constants.AiApiServiceConstants.FULL_NAME_EXAMPLES;
import static wander.wise.application.constants.AiApiServiceConstants.FULL_NAME_RULES;
import static wander.wise.application.constants.AiApiServiceConstants.FULL_NAME_TEMPLATE;
import static wander.wise.application.constants.AiApiServiceConstants.LIST_FORMATING_RULES;
import static wander.wise.application.constants.AiApiServiceConstants.LOCATION_NAMES_FIELD_FORMAT;
import static wander.wise.application.constants.AiApiServiceConstants.NON_EXISTING_RESTRICT;
import static wander.wise.application.constants.AiApiServiceConstants.REGION_EXAMPLES;
import static wander.wise.application.constants.AiApiServiceConstants.REMORE_DUPLICATES_SECOND_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.REMOVE_DUPLICATES_FIRST_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.REMOVE_DUPLICATES_THIRD_RULE;
import static wander.wise.application.constants.AiApiServiceConstants.SPECIAL_REQUIREMENTS_LIST;
import static wander.wise.application.constants.AiApiServiceConstants.SPECIFIC_LOCATION_EXAMPLES;
import static wander.wise.application.constants.AiApiServiceConstants.TOTAL_REQUIRED_RESPONSES_AMOUNT;
import static wander.wise.application.constants.AiApiServiceConstants.TRIP_TYPES_LIST;
import static wander.wise.application.constants.GlobalConstants.JSON_MAPPER;
import static wander.wise.application.constants.GlobalConstants.SEPARATOR;
import static wander.wise.application.constants.GlobalConstants.SET_DIVIDER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;
import wander.wise.application.dto.ai.AiResponseDto;
import wander.wise.application.dto.ai.LocationListDto;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.exception.custom.AiServiceException;

@Service
@RequiredArgsConstructor
public class AiApiServiceImpl implements AiApiService {
    public static final int TRAVEL_SCALE_INDEX = 0;
    public static final int POPULATED_LOCALITY_INDEX = 0;
    public static final int COUNTRY_INDEX = 1;
    private final ChatClient chatClient;

    @Override
    public List<AiResponseDto> getAiResponses(
            CardSearchParameters searchParameters,
            Map<String, List<String>> locationsToExcludeAndTypeMap) {
        List<AiResponseDto> aiResponses = new ArrayList<>();
        String totalLocationsToExclude = getTotalLocationsToExclude(locationsToExcludeAndTypeMap);
        locationsToExcludeAndTypeMap.keySet().forEach(tripType -> {
            String locationsToExcludeForType = getLocationsToExclude(
                    locationsToExcludeAndTypeMap,
                    tripType);
            Set<String> generatedNamesByType = getLocationList(
                    searchParameters,
                    locationsToExcludeAndTypeMap,
                    tripType,
                    locationsToExcludeForType,
                    totalLocationsToExclude);
            if (!generatedNamesByType.isEmpty()) {
                List<AiResponseDto> aiResponsesByType = initializeAiResponses(
                        searchParameters,
                        tripType,
                        generatedNamesByType);
                aiResponses.addAll(aiResponsesByType);
            }
        });
        return aiResponses
                .stream()
                .filter(aiResponseDto -> aiResponseDto.fullName()
                        .contains(searchParameters.travelDistance()[TRAVEL_SCALE_INDEX]))
                .toList();
    }

    @Override
    public CardSearchParameters defineRegion(CardSearchParameters searchParameters) {
        String paramsJson = objectToJson(searchParameters);
        String defineRegionPrompt = getDefineRegionPrompt(searchParameters, paramsJson);
        String response = chatClient.call(defineRegionPrompt);
        return (CardSearchParameters) jsonToObject(response, CardSearchParameters.class);
    }

    @Override
    public CardSearchParameters defineContinent(CardSearchParameters searchParameters) {
        String paramsJson = objectToJson(searchParameters);
        String detectDistancePrompt = getDefineContinentPrompt(searchParameters, paramsJson);
        String response = chatClient.call(detectDistancePrompt);
        return (CardSearchParameters) jsonToObject(response, CardSearchParameters.class);
    }

    /**
     * Get checked and formated list of locations
     */
    private Set<String> getLocationList(
            CardSearchParameters searchParameters,
            Map<String, List<String>> locationsToExcludeAndTypeMap,
            String tripType, String locationsToExclude,
            String totalLocationsToExclude) {
        return getLocationListDto(
                searchParameters,
                locationsToExclude,
                totalLocationsToExclude,
                tripType,
                getResponsesAmount(locationsToExcludeAndTypeMap))
                .locationNames()
                .stream()
                .filter(name -> !totalLocationsToExclude.contains(name))
                .collect(Collectors.toSet());
    }

    /**
     * Initialize AiResponseDtos and complete "tripTypes"
     * and "specialRequirements" fields
     */
    private List<AiResponseDto> initializeAiResponses(
            CardSearchParameters searchParameters,
            String tripType,
            Set<String> generatedNamesByType) {
        List<AiResponseDto> aiResponsesByType = generatedNamesByType.stream()
                .map(this::generateLocationDetails)
                .map(aiResponseDto -> finishResponseDtoInitialization(
                        searchParameters,
                        tripType,
                        aiResponseDto))
                .toList();
        return aiResponsesByType;
    }

    private static AiResponseDto finishResponseDtoInitialization(
            CardSearchParameters searchParameters,
            String tripType,
            AiResponseDto aiResponseDto) {
        if (!aiResponseDto.tripTypes().contains(tripType)) {
            aiResponseDto = aiResponseDto.setTripTypes(aiResponseDto.tripTypes() + SET_DIVIDER + tripType);
        }
        for (String specialRequirement : searchParameters.specialRequirements()) {
            if (!aiResponseDto.specialRequirements().contains(specialRequirement)) {
                aiResponseDto = aiResponseDto.setSpecialRequirements(
                        aiResponseDto.specialRequirements()
                                + SET_DIVIDER
                                + specialRequirement);
            }
        }
        return aiResponseDto;
    }

    /**
     * ChatClient requests
     */
    private LocationListDto getLocationListDto(
            CardSearchParameters searchParameters,
            String locationsToExclude,
            String totalLocationsToExclude,
            String tripType,
            int responsesAmount) {
        LocationListDto locationListDto = null;
        String locationListPrompt = getListOfLocationsPrompt(
                searchParameters,
                locationsToExclude,
                tripType,
                responsesAmount);
        String locationList = chatClient.call(locationListPrompt);
        locationList = removeDuplicates(
                locationList,
                totalLocationsToExclude);
        locationList = check(locationList);
        return (LocationListDto) jsonToObject(locationList, LocationListDto.class);
    }

    private String removeDuplicates(String locationList, String totalLocationsToExclude) {
        String removeDuplicatesPrompt = getRemoveDuplicatesPrompt(
                locationList,
                totalLocationsToExclude);
        locationList = chatClient.call(removeDuplicatesPrompt);
        return locationList;
    }

    private String check(String locationList) {
        String checkWhereIsPrompt = getCheckPrompt(locationList);
        locationList = chatClient.call(checkWhereIsPrompt);
        return locationList;
    }

    private AiResponseDto generateLocationDetails(String name) {
        String locationDetailsPrompt = getLocationDetailsPrompt(name);
        String locationDetails = chatClient.call(locationDetailsPrompt);
        return (AiResponseDto) jsonToObject(locationDetails, AiResponseDto.class);
    }

    /**
     * Prompt generation
     */
    private String getListOfLocationsPrompt(CardSearchParameters searchParameters,
                                            String locationsToExclude, String tripType,
                                            int responsesAmount) {
        StringBuilder listOfLocationsPrompt = new StringBuilder();
        listOfLocationsPrompt.append("I am in ").append(searchParameters.startLocation())
                .append(SEPARATOR).append("Find me ").append(responsesAmount)
                .append(" locations, where to travel by this requirements: ")
                .append(SEPARATOR)
                .append("Trip type: ").append(tripType)
                .append(SEPARATOR)
                .append("Climate: ")
                .append(String.join(", ", searchParameters.climate()))
                .append("Special requirements: ")
                .append(String.join(", ", searchParameters.specialRequirements()))
                .append(SEPARATOR)
                .append("The result should not contain these locations: ")
                .append(locationsToExclude)
                .append(SEPARATOR)
                .append("Locations must be within: ")
                .append(searchParameters.travelDistance()[TRAVEL_SCALE_INDEX])
                .append(". Collect locations from different parts of it. It is very "
                        + "important to fill the list with locations all around ")
                .append(searchParameters.travelDistance()[TRAVEL_SCALE_INDEX])
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
                .append("}");
        return listOfLocationsPrompt.toString();
    }

    private String getRemoveDuplicatesPrompt(String locationList,
                                             String locationsToExclude) {
        StringBuilder filterListPrompt = new StringBuilder();
        filterListPrompt
                .append("I have two lists of locations. The first here: ")
                .append(SEPARATOR)
                .append(locationsToExclude)
                .append(SEPARATOR)
                .append("The second text is a json file with locations, ")
                .append("that has been generated by AI. Here: ")
                .append(SEPARATOR)
                .append(locationList)
                .append(SEPARATOR)
                .append("I need you delete duplicates from the second list by this algorithm: ")
                .append(SEPARATOR)
                .append(REMOVE_DUPLICATES_FIRST_RULE)
                .append(SEPARATOR)
                .append(REMORE_DUPLICATES_SECOND_RULE)
                .append(SEPARATOR)
                .append(REMOVE_DUPLICATES_THIRD_RULE)
                .append(SEPARATOR)
                .append("Return the second list in the same json format, ")
                .append("in which you received it.");
        return filterListPrompt.toString();
    }

    private String getCheckPrompt(String locationList) {
        StringBuilder checkedLocations = new StringBuilder();
        checkedLocations.append("I have this list of locations in json: ")
                .append(SEPARATOR)
                .append(locationList)
                .append(SEPARATOR)
                .append("I need you to fix mistakes in this list")
                .append(" of locations by next algorithm: ")
                .append(SEPARATOR)
                .append(CHECK_LOCATIONS_FIRST_RULE)
                .append(SEPARATOR)
                .append(CHECK_LOCATIONS_SECOND_RULE)
                .append(SEPARATOR)
                .append(CHECK_LOCATIONS_THIRD_RULE)
                .append(SEPARATOR)
                .append("Fix location name, if it doesn't match required pattern.")
                .append(SEPARATOR)
                .append("Return the result in the same json format.");
        return checkedLocations.toString();
    }

    private String getLocationDetailsPrompt(String locationName) {
        StringBuilder locationDetailsPrompt = new StringBuilder();
        locationDetailsPrompt.append("I want to know more about this location: ")
                .append(locationName)
                .append(SEPARATOR)
                .append("Give me answer strictly as a json object. Use this format: ")
                .append(SEPARATOR)
                .append("{").append(SEPARATOR)
                .append("\"fullName\": ").append(FULL_NAME_TEMPLATE).append(FULL_NAME_RULES)
                .append(FULL_NAME_EXAMPLES)
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

    private static String getDefineRegionPrompt(CardSearchParameters searchParameters,
                                                String paramsJson) {
        return new StringBuilder()
                .append("I have this json object with search parameters: ")
                .append(paramsJson)
                .append(SEPARATOR)
                .append("Find in which region(part) of ")
                .append(searchParameters.startLocation().split(",")[COUNTRY_INDEX])
                .append(" the ")
                .append(searchParameters.startLocation().split(",")[POPULATED_LOCALITY_INDEX])
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

    private static String getDefineContinentPrompt(CardSearchParameters searchParameters,
                                                   String paramsJson) {
        return new StringBuilder()
                .append("I have this json object with search parameters: ")
                .append(paramsJson)
                .append(SEPARATOR)
                .append("Find on what continent is ")
                .append(searchParameters.startLocation().split(",")[COUNTRY_INDEX])
                .append(" located?, set name of this continent in travel distance ")
                .append("field and return new object in the same format.")
                .toString();
    }

    /**
     * Util methods
     */
    private static int getResponsesAmount(
            Map<String, List<String>> locationsToExcludeAndTypeMap) {
        return TOTAL_REQUIRED_RESPONSES_AMOUNT / locationsToExcludeAndTypeMap.size();
    }

    private static String getTotalLocationsToExclude(
            Map<String, List<String>> locationsToExcludeAndTypeMap) {
        return locationsToExcludeAndTypeMap.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.joining(", "));
    }

    private static String getLocationsToExclude(
            Map<String, List<String>> locationsToExcludeAndTypeMap,
            String tripType) {
        return locationsToExcludeAndTypeMap.get(tripType).toString();
    }

    private static String objectToJson(Object object) {
        String paramsJson = null;
        try {
            paramsJson = JSON_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            String className = getClassName(object.getClass());
            throw new AiServiceException(
                    "Can't convert " + className + " to JSON", e);
        }
        return paramsJson;
    }

    private static Object jsonToObject(String json, Class clazz) {
        try {
            return JSON_MAPPER.readValue(json, clazz);
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
}
