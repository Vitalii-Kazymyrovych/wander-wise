package wander.wise.application.dto.card;

import jakarta.validation.constraints.NotBlank;

public record ReportCardRequestDto(
        @NotBlank String text) {
}
