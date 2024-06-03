package wander.wise.application.service.api.ai;

import java.util.List;
import java.util.Map;

import wander.wise.application.dto.ai.AiResponseDto;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.dto.card.CreateCardRequestDto;

public interface AiApiService {
    List<AiResponseDto> getAiResponses(
            CardSearchParameters searchParameters,
            Map<String, List<String>> locationsToExcludeAndTypeMap);

    CardSearchParameters defineRegion(CardSearchParameters searchParameters);

    CardSearchParameters defineContinent(CardSearchParameters searchParameters);

    CreateCardRequestDto defineRegionAndContinent(CreateCardRequestDto requestDto);
}
