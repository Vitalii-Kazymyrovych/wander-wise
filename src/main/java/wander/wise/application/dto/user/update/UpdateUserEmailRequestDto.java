package wander.wise.application.dto.user.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserEmailRequestDto(@NotBlank @Email String email) {
}
