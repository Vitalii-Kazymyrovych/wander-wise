package wander.wise.application.dto.user.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserInfoRequestDto(
        @NotBlank String pseudonym,
        String firstName,
        String lastName,
        String location,
        String bio) {
}
