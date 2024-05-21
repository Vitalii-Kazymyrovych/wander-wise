package wander.wise.application.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import wander.wise.application.config.MapperConfig;
import wander.wise.application.dto.collection.CollectionDto;
import wander.wise.application.dto.collection.CollectionWithoutCardsDto;
import wander.wise.application.dto.collection.CreateCollectionRequestDto;
import wander.wise.application.dto.collection.UpdateCollectionRequestDto;
import wander.wise.application.model.Card;
import wander.wise.application.model.Collection;
import wander.wise.application.model.User;

@Mapper(config = MapperConfig.class, uses = CardMapper.class)
public interface CollectionMapper {
    @Mapping(target = "author", source = "user", qualifiedByName = "userToAuthor")
    @Mapping(target = "cardDtos", source = "cards", qualifiedByName = "cardsToCardDtos")
    CollectionDto toDto(Collection collection);

    @Mapping(target = "author", source = "user", qualifiedByName = "userToAuthor")
    CollectionWithoutCardsDto toCollectionWithoutCardsDto(Collection collection);

    @Mapping(target = "cards", source = "cardIds", qualifiedByName = "cardIdsToCards")
    Collection toModel(CreateCollectionRequestDto requestDto);

    @Mapping(target = "cards", source = "cardIds", qualifiedByName = "cardIdsToCards")
    Collection updateCollectionFromDto(@MappingTarget Collection collection,
                                       UpdateCollectionRequestDto requestDto);

    @Named("userToAuthor")
    default String userToAuthor(User user) {
        return user.getPseudonym();
    }

    @Named("cardIdsToCards")
    default Set<Card> cardIdsToCards(Set<Long> cardIds) {
        return cardIds.stream()
                .map(Card::new)
                .collect(Collectors.toSet());
    }
}
