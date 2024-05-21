package wander.wise.application.dto.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequestDto(
        @NotNull Long cardId,
        @NotBlank String text,
        @NotNull @Min(1) @Max(5) Integer stars) {
}
