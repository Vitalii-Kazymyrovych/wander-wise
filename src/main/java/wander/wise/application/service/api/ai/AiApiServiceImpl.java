package wander.wise.application.service.api.ai;

import static wander.wise.application.constants.AiApiServiceConstants.REGION_AND_CONTINENT_DTO_EXAMPLE;
import static wander.wise.application.constants.AiApiServiceConstants.REGION_EXAMPLES;
import static wander.wise.application.constants.GlobalConstants.JSON_MAPPER;
import static wander.wise.application.constants.GlobalConstants.SEPARATOR;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;
import wander.wise.application.dto.ai.AiResponseDto;
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


    @Override
    public List<AiResponseDto> getAiResponses(CardSearchParameters searchParameters,
                                              Map<String, List<String>> locationsToExcludeAndTypeMap) {
        return List.of();
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
