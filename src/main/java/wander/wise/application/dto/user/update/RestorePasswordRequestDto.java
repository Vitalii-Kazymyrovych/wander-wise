package wander.wise.application.dto.user.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RestorePasswordRequestDto(@NotBlank @Email String email) {
}
