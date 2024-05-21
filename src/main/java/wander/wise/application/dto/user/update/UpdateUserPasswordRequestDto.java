package wander.wise.application.dto.user.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import wander.wise.application.validation.password.PasswordMatch;

@PasswordMatch
public record UpdateUserPasswordRequestDto(
        @NotBlank @Size(min = 8) String oldPassword,
        @NotBlank @Size(min = 8) String password,
        @NotBlank @Size(min = 8) String repeatPassword) {
}
