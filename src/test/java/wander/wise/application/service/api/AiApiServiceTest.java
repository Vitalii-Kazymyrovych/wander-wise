package wander.wise.application.service.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.ChatClient;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;
import wander.wise.application.dto.ai.AiResponseDto;
import wander.wise.application.dto.ai.CheckedLocation;
import wander.wise.application.dto.ai.FullNameDto;
import wander.wise.application.dto.ai.LocationListDto;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.dto.card.CreateCardRequestDto;
import wander.wise.application.dto.card.RegionAndContinentDto;
import wander.wise.application.service.api.ai.AiApiService;
import wander.wise.application.service.api.ai.AiApiServiceImpl;

import java.io.IOException;
import java.sql.Struct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static wander.wise.application.constants.GlobalConstants.SET_DIVIDER;

@ExtendWith(MockitoExtension.class)
public class AiApiServiceTest {
    private static String excludeTotal = "place1, place2";
    String emptyString = "";
    String startLocation = "Loc1,Loc2";
    String[] stringArr = new String[]{"", "", };
    @InjectMocks
    private AiApiServiceImpl aiApiService;
    @Mock
    ChatClient chatClient;
    @Mock
    ObjectMapper objectMapper;
    Random random = new Random();

    @Test
    public void getAiResponses_ValidData_ReturnsAiResponseDtoList() throws JsonProcessingException {
        AiResponseDto responseDto = mock(AiResponseDto.class);
        CardSearchParameters searchParameters = mock(CardSearchParameters.class);
        Map excludeMap = mock(Map.class);
        List excludeByTypeList = mock(List.class);
        Set<String> keySet = Set.of("active", "romantic");
        LocationListDto listDto = mock(LocationListDto.class);
        Set<String> locationNames = Set.of(
                "place3|place3|place3|place3|place3",
                "place4|place3|place3|place3|place3",
                "place5|place3|place3|place3|place3");
        FullNameDto fullNameDto = mock(FullNameDto.class);
        CheckedLocation checkedLocation = mock(CheckedLocation.class);
        AiResponseDto aiResponseDto = new AiResponseDto(
                "", "", "",
                "", "", "");

        //Extract exclude total from excludeMap
        Collection values = mock(Collection.class);
        Stream stream = mock(Stream.class);
        when(excludeMap.values()).thenReturn(values);
        when(values.stream()).thenReturn(stream);
        when(stream.flatMap(any())).thenReturn(stream);
        when(stream.collect(any())).thenReturn(excludeTotal);

        //Extract keySet from excludeMap to iterate through
        when(excludeMap.keySet()).thenReturn(keySet);

        //Extract exclude by type string from excludeMap
        when(excludeMap.get(anyString())).thenReturn(excludeByTypeList);
        when(excludeByTypeList.toString()).thenReturn(excludeTotal);

        //Get location list prompt
        when(excludeMap.size()).thenReturn(2);
        when(searchParameters.startLocation()).thenReturn(emptyString);
        when(searchParameters.climate()).thenReturn(stringArr);
        when(searchParameters.specialRequirements()).thenReturn(stringArr);
        when(searchParameters.travelDistance()).thenReturn(stringArr);

        //Generate location list
        when(chatClient.call(anyString())).thenReturn(emptyString);
        when(objectMapper.readValue(anyString(), eq(LocationListDto.class))).thenReturn(listDto);
        when(listDto.locationNames()).thenReturn(locationNames);

        //Generate location full names
        when(objectMapper.readValue(anyString(), eq(FullNameDto.class))).thenReturn(fullNameDto);
        when(fullNameDto.name()).thenReturn(emptyString);
        when(fullNameDto.populatedLocality()).thenReturn(emptyString);
        when(fullNameDto.region()).thenReturn(emptyString);
        when(fullNameDto.country()).thenReturn(emptyString);
        when(fullNameDto.continent()).thenReturn(emptyString);
        when(objectMapper.writeValueAsString(any(LocationListDto.class))).thenReturn(emptyString);

        //Check locations
        when(objectMapper.readValue(anyString(), eq(CheckedLocation.class))).thenReturn(checkedLocation);
        when(checkedLocation.locationName()).thenReturn("");

        //Get location details
        when(objectMapper.readValue(anyString(), eq(AiResponseDto.class))).thenReturn(aiResponseDto);

        List<AiResponseDto> responseDtos = aiApiService
                .getAiResponses(searchParameters, excludeMap);

        Assertions.assertThat(responseDtos).isNotNull();
        Assertions.assertThat(responseDtos.size()).isGreaterThan(1);
    }

    @Test
    public void defineRegion_ValidData_ReturnsCardSearchParameters() throws IOException {
        CardSearchParameters searchParameters = mock(CardSearchParameters.class);
        when(objectMapper.writeValueAsString(searchParameters)).thenReturn(emptyString);
        when(searchParameters.startLocation()).thenReturn(startLocation);
        when(chatClient.call(anyString())).thenReturn(emptyString);
        when(objectMapper.readValue(anyString(), eq(CardSearchParameters.class))).thenReturn(searchParameters);

        CardSearchParameters actual = aiApiService.defineRegion(searchParameters);
        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    public void defineContinent_ValidData_ReturnsCardSearchParameters() throws IOException {
        CardSearchParameters searchParameters = mock(CardSearchParameters.class);
        when(objectMapper.writeValueAsString(searchParameters)).thenReturn(emptyString);
        when(searchParameters.startLocation()).thenReturn(startLocation);
        when(chatClient.call(anyString())).thenReturn(emptyString);
        when(objectMapper.readValue(anyString(), eq(CardSearchParameters.class))).thenReturn(searchParameters);

        CardSearchParameters actual = aiApiService.defineContinent(searchParameters);
        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    public void defineRegionAndContinent_ValidData_ReturnsCreateCardRequestDto() throws JsonProcessingException {
        CreateCardRequestDto requestDto = mock(CreateCardRequestDto.class);
        RegionAndContinentDto regionAndContinentDto = mock(RegionAndContinentDto.class);
        when(requestDto.populatedLocality()).thenReturn(emptyString);
        when(requestDto.country()).thenReturn(emptyString);
        when(chatClient.call(anyString())).thenReturn(emptyString);
        when(objectMapper.readValue(anyString(), eq(RegionAndContinentDto.class))).thenReturn(regionAndContinentDto);
        when(regionAndContinentDto.region()).thenReturn(emptyString);
        when(regionAndContinentDto.continent()).thenReturn(emptyString);
        when(requestDto.setRegionAndContinent(anyString(), anyString())).thenReturn(requestDto);

        CreateCardRequestDto actual = aiApiService.defineRegionAndContinent(requestDto);
        Assertions.assertThat(actual).isNotNull();
    }
}
