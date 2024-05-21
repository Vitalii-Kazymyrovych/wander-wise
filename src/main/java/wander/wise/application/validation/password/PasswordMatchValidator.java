package wander.wise.application.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    @Override
    public boolean isValid(
            Object requestDto,
            ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field passwordField = requestDto.getClass().getDeclaredField("password");
            passwordField.setAccessible(true);
            String password = (String) passwordField.get(requestDto);

            Field repeatPasswordField = requestDto.getClass().getDeclaredField("repeatPassword");
            repeatPasswordField.setAccessible(true);
            String repeatPassword = (String) repeatPasswordField.get(requestDto);

            return Objects.equals(password, repeatPassword);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Incorrect request dto: " + requestDto, e);
        }
    }
}
