package wander.wise.application.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import wander.wise.application.config.MapperConfig;
import wander.wise.application.dto.user.UserDto;
import wander.wise.application.dto.user.registration.RegisterUserRequestDto;
import wander.wise.application.dto.user.update.UpdateUserInfoRequestDto;
import wander.wise.application.model.Role;
import wander.wise.application.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "roleIds", source = "roles", qualifiedByName = "rolesToRoleIds")
    UserDto toDto(User user);

    User toModel(RegisterUserRequestDto requestDto);

    User updateUserFromDto(@MappingTarget User user, UpdateUserInfoRequestDto requestDto);

    @Named("rolesToRoleIds")
    default Set<Long> rolesToRoleIds(Set<Role> roles) {
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }
}
