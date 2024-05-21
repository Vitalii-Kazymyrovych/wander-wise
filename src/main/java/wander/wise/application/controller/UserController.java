package wander.wise.application.controller;

import static wander.wise.application.constants.SwaggerConstants.BAN_USER_DESC;
import static wander.wise.application.constants.SwaggerConstants.BAN_USER_SUM;
import static wander.wise.application.constants.SwaggerConstants.DELETE_USER_DESC;
import static wander.wise.application.constants.SwaggerConstants.DELETE_USER_SUM;
import static wander.wise.application.constants.SwaggerConstants.GET_USER_COLLECTIONS_DESC;
import static wander.wise.application.constants.SwaggerConstants.GET_USER_COLLECTIONS_SUM;
import static wander.wise.application.constants.SwaggerConstants.GET_USER_COMMENTS_DESC;
import static wander.wise.application.constants.SwaggerConstants.GET_USER_COMMENTS_SUM;
import static wander.wise.application.constants.SwaggerConstants.GET_USER_PROFILE_DESC;
import static wander.wise.application.constants.SwaggerConstants.GET_USER_PROFILE_SUM;
import static wander.wise.application.constants.SwaggerConstants.GET_USER_SOCIAL_LINKS_DESC;
import static wander.wise.application.constants.SwaggerConstants.GET_USER_SOCIAL_LINKS_SUM;
import static wander.wise.application.constants.SwaggerConstants.REQUEST_UPDATE_USER_EMAIL_DESC;
import static wander.wise.application.constants.SwaggerConstants.REQUEST_UPDATE_USER_EMAIL_SUM;
import static wander.wise.application.constants.SwaggerConstants.UNBAN_USER_DESC;
import static wander.wise.application.constants.SwaggerConstants.UNBAN_USER_SUM;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_EMAIL_DESC;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_EMAIL_SUM;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_IMAGE_DESC;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_IMAGE_SUM;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_INFO_DESC;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_INFO_SUM;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_PASSWORD_DESC;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_PASSWORD_SUM;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_ROLES_DESC;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_USER_ROLES_SUM;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wander.wise.application.dto.collection.CollectionWithoutCardsDto;
import wander.wise.application.dto.comment.CommentDto;
import wander.wise.application.dto.social.link.SocialLinkDto;
import wander.wise.application.dto.user.UserDto;
import wander.wise.application.dto.user.login.LoginResponseDto;
import wander.wise.application.dto.user.update.UpdateUserEmailRequestDto;
import wander.wise.application.dto.user.update.UpdateUserInfoRequestDto;
import wander.wise.application.dto.user.update.UpdateUserPasswordRequestDto;
import wander.wise.application.dto.user.update.UpdateUserRolesRequestDto;
import wander.wise.application.service.user.UserService;

@Tag(name = "User management endpoints")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}/profile")
    @Operation(summary = GET_USER_PROFILE_SUM, description = GET_USER_PROFILE_DESC)
    public UserDto getUserProfile(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/{id}/social-links")
    @Operation(summary = GET_USER_SOCIAL_LINKS_SUM, description = GET_USER_SOCIAL_LINKS_DESC)
    public Set<SocialLinkDto> getUserSocialLinks(@PathVariable Long id) {
        return userService.getUserSocialLinks(id);
    }

    @GetMapping("/{id}/collections")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = GET_USER_COLLECTIONS_SUM, description = GET_USER_COLLECTIONS_DESC)
    public Set<CollectionWithoutCardsDto> getUserCollections(@PathVariable Long id,
                                                             Authentication authentication) {
        return userService.getUserCollections(id, authentication.getName());
    }

    @GetMapping("/{id}/comments")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = GET_USER_COMMENTS_SUM, description = GET_USER_COMMENTS_DESC)
    public Set<CommentDto> getUserComments(@PathVariable Long id,
                                           Authentication authentication) {
        return userService.getUserComments(id, authentication.getName());
    }

    @PutMapping("/update-user-info/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = UPDATE_USER_INFO_SUM, description = UPDATE_USER_INFO_DESC)
    public UserDto updateUserInfo(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody UpdateUserInfoRequestDto requestDto) {
        return userService.updateUserInfo(id, authentication.getName(), requestDto);
    }

    @PutMapping("/update-user-image/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = UPDATE_USER_IMAGE_SUM, description = UPDATE_USER_IMAGE_DESC)
    public UserDto updateUserImage(
            @PathVariable Long id,
            @RequestParam(value = "image") MultipartFile userImage,
            Authentication authentication) {
        return userService.updateUserImage(id, authentication.getName(), userImage);
    }

    @PostMapping("/request-update-user-email/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = REQUEST_UPDATE_USER_EMAIL_SUM,
            description = REQUEST_UPDATE_USER_EMAIL_DESC)
    public UserDto requestUpdateUserEmail(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody UpdateUserEmailRequestDto requestDto) {
        return userService.requestUpdateUserEmail(id, authentication.getName(), requestDto);
    }

    @PutMapping("/update-user-email/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = UPDATE_USER_EMAIL_SUM, description = UPDATE_USER_EMAIL_DESC)
    public LoginResponseDto updateUserEmail(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody UpdateUserEmailRequestDto requestDto) {
        return userService.updateUserEmail(id, authentication.getName(), requestDto);
    }

    @PutMapping("/update-user-password/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = UPDATE_USER_PASSWORD_SUM, description = UPDATE_USER_PASSWORD_DESC)
    public LoginResponseDto updateUserPassword(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody UpdateUserPasswordRequestDto requestDto) {
        return userService.updateUserPassword(id, authentication.getName(), requestDto);
    }

    @PutMapping("/update-user-roles/{id}")
    @PreAuthorize("hasAuthority('ROOT')")
    @Operation(summary = UPDATE_USER_ROLES_SUM, description = UPDATE_USER_ROLES_DESC)
    public UserDto updateUserRoles(@PathVariable Long id,
                                   @Valid @RequestBody UpdateUserRolesRequestDto requestDto) {
        return userService.updateUserRoles(id, requestDto);
    }

    @DeleteMapping("/delete-user/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = DELETE_USER_SUM, description = DELETE_USER_DESC)
    public ResponseEntity<String> deleteUser(@PathVariable Long id,
                                             Authentication authentication) {
        userService.deleteUser(id, authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/ban-user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = BAN_USER_SUM, description = BAN_USER_DESC)
    public UserDto banUser(@PathVariable Long id) {
        return userService.banUser(id);
    }

    @GetMapping("/unban-user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = UNBAN_USER_SUM, description = UNBAN_USER_DESC)
    public UserDto unbanUser(@PathVariable Long id) {
        return userService.unbanUser(id);
    }
}
