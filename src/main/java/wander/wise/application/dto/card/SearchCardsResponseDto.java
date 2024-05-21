package wander.wise.application.dto.card;

import java.util.List;

public record SearchCardsResponseDto(Integer currentPage, List<CardDto> cards) {
}
