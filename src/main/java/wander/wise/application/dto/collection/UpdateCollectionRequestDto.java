package wander.wise.application.dto.collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record UpdateCollectionRequestDto(
        @NotBlank String name,
        @NotNull Set<Long> cardIds,
        @NotNull boolean isPublic) {
}
