package wander.wise.application.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import wander.wise.application.config.MapperConfig;
import wander.wise.application.dto.comment.CommentDto;
import wander.wise.application.dto.comment.CreateCommentRequestDto;
import wander.wise.application.model.Card;
import wander.wise.application.model.Comment;
import wander.wise.application.model.User;

import static wander.wise.application.constants.GlobalConstants.TIMESTAMP_FORMAT;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    @Mapping(target = "author", source = "user", qualifiedByName = "userToAuthor")
    @Mapping(target = "timeStamp", source = "timeStamp", qualifiedByName = "timeStampToString")
    @Mapping(target = "cardId", source = "card", qualifiedByName = "cardToCardId")
    CommentDto toDto(Comment comment);

    @Mapping(target = "card", source = "cardId", qualifiedByName = "cardIdToCard")
    Comment toModel(CreateCommentRequestDto requestDto);

    Comment updateCommentFromDto(@MappingTarget Comment comment,
                                 CreateCommentRequestDto requestDto);

    @Named("toCommentDtoSet")
    default Set<CommentDto> toCommentDtoList(Set<Comment> comments) {
        return comments.stream().map(this::toDto).collect(Collectors.toSet());
    }

    @Named("cardIdToCard")
    default Card cardIdToCard(Long cardId) {
        return new Card(cardId);
    }

    @Named("userToAuthor")
    default String userToAuthor(User user) {
        return user.getPseudonym();
    }

    @Named("timeStampToString")
    default String timeStampToString(LocalDateTime timeStamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
        return timeStamp.format(formatter);
    }

    @Named("userIdToUser")
    default User userIdToUser(Long userId) {
        return new User(userId);
    }

    @Named("cardToCardId")
    default Long cardToCardId(Card card) {
        return card.getId();
    }
}
