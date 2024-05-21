package wander.wise.application.dto.user.update;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record UpdateUserRolesRequestDto(@NotEmpty Set<Long> roleIds) {
}
