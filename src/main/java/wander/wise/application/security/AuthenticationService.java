package wander.wise.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import wander.wise.application.dto.user.UserDto;
import wander.wise.application.dto.user.login.LoginRequestDto;
import wander.wise.application.dto.user.login.LoginResponseDto;
import wander.wise.application.exception.custom.AuthorizationException;
import wander.wise.application.mapper.UserMapper;
import wander.wise.application.repository.user.UserRepository;
import wander.wise.application.service.invalid.jwt.InvalidJwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final InvalidJwtService invalidJwtService;

    public LoginResponseDto authenticate(LoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.email(),
                        requestDto.password()));
        String token = jwtUtil.generateToken(authentication.getName());
        UserDto authenticatedUser = userMapper.toDto(userRepository
                .findByEmail(requestDto.email()).get());
        return new LoginResponseDto(authenticatedUser, token);
    }

    public LoginResponseDto refreshJwt(String email) {
        String token = jwtUtil.generateToken(email);
        UserDto authenticatedUser = userMapper.toDto(userRepository
                .findByEmail(email).get());
        return new LoginResponseDto(authenticatedUser, token);
    }

    public void logout(String token, String email) {
        if (jwtUtil.getUsername(token).equals(email)) {
            jwtUtil.isValidToken(token);
            invalidJwtService.invalidateJwt(token);
        } else {
            throw new AuthorizationException("Access denied.");
        }
    }
}
