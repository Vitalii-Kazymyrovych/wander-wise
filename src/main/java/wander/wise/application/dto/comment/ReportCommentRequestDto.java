package wander.wise.application.dto.comment;

import jakarta.validation.constraints.NotBlank;

public record ReportCommentRequestDto(
        @NotBlank String commentAuthor,
        @NotBlank String commentText,
        @NotBlank String reportText) {
}
