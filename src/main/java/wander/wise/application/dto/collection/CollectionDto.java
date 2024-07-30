package wander.wise.application.dto.collection;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import wander.wise.application.dto.card.CardDto;

import java.util.Set;

@Data
@ToString
@RequiredArgsConstructor
public class CollectionDto {
        Long id;
        String author;
        String name;
        String imageLink;
        Set<CardDto> cardDtos;
        boolean isPublic;
}