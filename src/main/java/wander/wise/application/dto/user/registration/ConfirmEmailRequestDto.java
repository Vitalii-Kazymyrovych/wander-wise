package wander.wise.application.dto.user.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ConfirmEmailRequestDto(@NotBlank @Email String email) {
}
