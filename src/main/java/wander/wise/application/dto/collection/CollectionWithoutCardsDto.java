package wander.wise.application.dto.collection;

public record CollectionWithoutCardsDto(
        Long id,
        String author,
        String name,
        String imageLink,
        boolean isPublic) {
}
