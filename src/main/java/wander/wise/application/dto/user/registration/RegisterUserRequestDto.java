package wander.wise.application.dto.user.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import wander.wise.application.validation.password.PasswordMatch;

@PasswordMatch
public record RegisterUserRequestDto(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank @Size(min = 8) String repeatPassword) {
}
