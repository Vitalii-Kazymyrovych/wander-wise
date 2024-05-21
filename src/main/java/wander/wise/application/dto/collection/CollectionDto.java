package wander.wise.application.dto.collection;

import java.util.Set;
import wander.wise.application.dto.card.CardDto;

public record CollectionDto(
        Long id,
        String author,
        String name,
        String imageLink,
        Set<CardDto> cardDtos,
        boolean isPublic) {
}
