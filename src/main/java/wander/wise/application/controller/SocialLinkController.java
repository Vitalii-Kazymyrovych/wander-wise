package wander.wise.application.controller;

import static wander.wise.application.constants.SwaggerConstants.ADD_SOCIAL_LINK_DESC;
import static wander.wise.application.constants.SwaggerConstants.ADD_SOCIAL_LINK_SUM;
import static wander.wise.application.constants.SwaggerConstants.DELETE_SOCIAL_LINK_DESC;
import static wander.wise.application.constants.SwaggerConstants.DELETE_SOCIAL_LINK_SUM;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_SOCIAL_LINK_DESC;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_SOCIAL_LINK_SUM;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wander.wise.application.dto.social.link.CreateSocialLinkRequestDto;
import wander.wise.application.dto.social.link.SocialLinkDto;
import wander.wise.application.service.social.link.SocialLinkService;

@Tag(name = "Social links management endpoints")
@RestController
@RequestMapping("/social-links")
@RequiredArgsConstructor
public class SocialLinkController {
    private final SocialLinkService socialLinkService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = ADD_SOCIAL_LINK_SUM, description = ADD_SOCIAL_LINK_DESC)
    public SocialLinkDto addSocialLink(Authentication authentication,
                                       @RequestBody CreateSocialLinkRequestDto requestDto) {
        return socialLinkService.save(authentication.getName(), requestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = UPDATE_SOCIAL_LINK_SUM, description = UPDATE_SOCIAL_LINK_DESC)
    public SocialLinkDto updateSocialLink(@PathVariable Long id,
                                          Authentication authentication,
                                          @RequestBody CreateSocialLinkRequestDto requestDto) {
        return socialLinkService.updateSocialLinkById(id, authentication.getName(), requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = DELETE_SOCIAL_LINK_SUM, description = DELETE_SOCIAL_LINK_DESC)
    public ResponseEntity<String> deleteSocialLink(@PathVariable Long id,
                                                   Authentication authentication) {
        socialLinkService.deleteSocialLinkById(id, authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
