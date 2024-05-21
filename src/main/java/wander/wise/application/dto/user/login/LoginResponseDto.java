package wander.wise.application.dto.user.login;

import wander.wise.application.dto.user.UserDto;

public record LoginResponseDto(UserDto user, String token) {
}
