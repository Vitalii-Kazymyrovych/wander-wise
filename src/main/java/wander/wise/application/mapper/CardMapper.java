package wander.wise.application.mapper;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import wander.wise.application.config.MapperConfig;
import wander.wise.application.dto.ai.AiResponseDto;
import wander.wise.application.dto.card.CardDto;
import wander.wise.application.dto.card.CreateCardRequestDto;
import wander.wise.application.model.Card;

import static wander.wise.application.constants.GlobalConstants.RM_DIVIDER;
import static wander.wise.application.constants.GlobalConstants.SET_DIVIDER;

@Mapper(config = MapperConfig.class, uses = {CommentMapper.class})
public interface CardMapper {
    int CARD_NAME_INDEX = 0;
    int CARD_WHERE_IS_BEGIN_INDEX = 1;

    Card aiResponseToCard(AiResponseDto aiResponseDto);

    @Mapping(target = "name", source = "fullName",
            qualifiedByName = "fullNameToName")
    @Mapping(target = "whereIs", source = "fullName",
            qualifiedByName = "fullNameToWhereIs")
    @Mapping(target = "tripTypes", source = "tripTypes",
            qualifiedByName = "stringToSet")
    @Mapping(target = "specialRequirements", source = "specialRequirements",
            qualifiedByName = "stringToSet")
    @Mapping(target = "whyThisPlace", source = "whyThisPlace",
            qualifiedByName = "stringToSet")
    @Mapping(target = "imageLinks", source = "imageLinks",
            qualifiedByName = "stringToSet")
    @Mapping(target = "comments", source = "card.comments",
            qualifiedByName = "toCommentDtoSet")
    CardDto toDto(Card card);

    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "tripTypes", ignore = true)
    @Mapping(target = "specialRequirements", ignore = true)
    @Mapping(target = "whyThisPlace", ignore = true)
    @Mapping(target = "imageLinks", ignore = true)
    Card toModel(CreateCardRequestDto requestDto);

    @AfterMapping
    default void afterMappingToModel(@MappingTarget Card card, CreateCardRequestDto requestDto) {
        card.setFullName(new StringBuilder()
                .append(requestDto.name()).append(SET_DIVIDER)
                .append(requestDto.populatedLocality()).append(SET_DIVIDER)
                .append(requestDto.region()).append(SET_DIVIDER)
                .append(requestDto.country()).append(SET_DIVIDER)
                .append(requestDto.continent()).toString());
        card.setTripTypes(String.join(SET_DIVIDER, requestDto.tripTypes()));
        card.setSpecialRequirements(String.join(SET_DIVIDER, requestDto.specialRequirements()));
        card.setWhyThisPlace(String.join(SET_DIVIDER, requestDto.whyThisPlace()));
    }

    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "tripTypes", ignore = true)
    @Mapping(target = "specialRequirements", ignore = true)
    @Mapping(target = "whyThisPlace", ignore = true)
    @Mapping(target = "imageLinks", ignore = true)
    Card updateCardFromRequestDto(@MappingTarget Card card, CreateCardRequestDto requestDto);

    @AfterMapping
    default void afterMappingUpdateCardFromRequestDto(
            @MappingTarget Card card, CreateCardRequestDto requestDto) {
        afterMappingToModel(card, requestDto);
    }

    @Named("stringToSet")
    default Set<String> stringToSet(String field) {
        if (!field.isEmpty()) {
            return Arrays.stream(field.split(RM_DIVIDER))
                    .collect(Collectors.toSet());
        } else return Set.of();
    }

    @Named("fullNameToName")
    default String fullNameToName(String fullName) {
        return fullName.split(RM_DIVIDER)[CARD_NAME_INDEX];
    }

    @Named("fullNameToWhereIs")
    default String fullNameToWhereIs(String fullName) {
        String[] whereIsArray = fullName.split(RM_DIVIDER);
        whereIsArray = Arrays.copyOfRange(
                whereIsArray,
                CARD_WHERE_IS_BEGIN_INDEX,
                whereIsArray.length);
        return String.join(", ", whereIsArray);
    }

    @Named("cardsToCardDtos")
    default Set<CardDto> cardsTocardDtos(Set<Card> cards) {
        return cards.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
