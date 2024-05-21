package wander.wise.application.dto.collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record CreateCollectionRequestDto(
        @NotBlank String name,
        @NotNull Long userId,
        @NotNull Set<Long> cardIds) {
}
