package wander.wise.application.controller;

import static wander.wise.application.constants.SwaggerConstants.CONFIRM_EMAIL_DESC;
import static wander.wise.application.constants.SwaggerConstants.CONFIRM_EMAIL_SUM;
import static wander.wise.application.constants.SwaggerConstants.LOGIN_DESC;
import static wander.wise.application.constants.SwaggerConstants.LOGIN_SUM;
import static wander.wise.application.constants.SwaggerConstants.LOGOUT_DESC;
import static wander.wise.application.constants.SwaggerConstants.LOGOUT_SUM;
import static wander.wise.application.constants.SwaggerConstants.REFRESH_JWT_DESC;
import static wander.wise.application.constants.SwaggerConstants.REFRESH_JWT_SUM;
import static wander.wise.application.constants.SwaggerConstants.REGISTER_NEW_USER_DESC;
import static wander.wise.application.constants.SwaggerConstants.REGISTER_NEW_USER_SUM;
import static wander.wise.application.constants.SwaggerConstants.RESTORE_PASSWORD_DESC;
import static wander.wise.application.constants.SwaggerConstants.RESTORE_PASSWORD_SUM;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wander.wise.application.dto.user.UserDto;
import wander.wise.application.dto.user.login.LoginRequestDto;
import wander.wise.application.dto.user.login.LoginResponseDto;
import wander.wise.application.dto.user.registration.ConfirmEmailRequestDto;
import wander.wise.application.dto.user.registration.RegisterUserRequestDto;
import wander.wise.application.dto.user.update.RestorePasswordRequestDto;
import wander.wise.application.security.AuthenticationService;
import wander.wise.application.service.user.UserService;

@Tag(name = "Authentication endpoints")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = REGISTER_NEW_USER_SUM, description = REGISTER_NEW_USER_DESC)
    public UserDto registerNewUser(@Valid @RequestBody RegisterUserRequestDto requestDto) {
        return userService.save(requestDto);
    }

    @PostMapping("/login")
    @Operation(summary = LOGIN_SUM, description = LOGIN_DESC)
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @GetMapping("/refresh-jwt")
    @Operation(summary = REFRESH_JWT_SUM, description = REFRESH_JWT_DESC)
    public LoginResponseDto refreshJwt(Authentication authentication) {
        return authenticationService.refreshJwt(authentication.getName());
    }

    @PostMapping("/confirm-email")
    @Operation(summary = CONFIRM_EMAIL_SUM, description = CONFIRM_EMAIL_DESC)
    public LoginResponseDto confirmEmail(@Valid @RequestBody ConfirmEmailRequestDto requestDto) {
        return userService.confirmEmail(requestDto.email());
    }

    @PostMapping("restore-password")
    @Operation(summary = RESTORE_PASSWORD_SUM, description = RESTORE_PASSWORD_DESC)
    public ResponseEntity<String> restorePassword(
            @Valid @RequestBody RestorePasswordRequestDto requestDto) {
        userService.restorePassword(requestDto);
        return new ResponseEntity<>(
                "New password was sent on the email: "
                        + requestDto.email(),
                HttpStatus.OK);
    }

    @GetMapping("logout/{token}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = LOGOUT_SUM, description = LOGOUT_DESC)
    public ResponseEntity<String> logout(@PathVariable String token, Authentication authentication) {
        authenticationService.logout(token, authentication.getName());
        return new ResponseEntity<>("Success logout.", HttpStatus.OK);
    }

}
