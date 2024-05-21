package wander.wise.application.dto.user;

import java.util.Set;

public record UserDto(
        Long id,
        String pseudonym,
        String email,
        String firstName,
        String lastName,
        String profileImage,
        String location,
        String bio,
        Set<Long> roleIds,
        boolean banned,
        String emailConfirmCode) {
    public UserDto setEmailConfirmCode(String emailConfirmCode) {
        return new UserDto(
                this.id,
                this.pseudonym,
                this.email,
                this.firstName,
                this.lastName,
                this.profileImage,
                this.location,
                this.bio,
                this.roleIds,
                this.banned,
                emailConfirmCode);
    }
}
