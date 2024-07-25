package wander.wise.application.service;

import org.junit.jupiter.api.Test;

public class SocialLinkServiceTest {

    // Tests for save

    @Test
    public void save_ValidRequest_SavesSocialLink() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void save_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void save_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    @Test
    public void save_InvalidRequest_ThrowsValidationException() {
        // Test when the request is invalid
    }

    // Tests for updateSocialLinkById

    @Test
    public void updateSocialLinkById_ValidRequest_UpdatesSocialLink() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void updateSocialLinkById_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void updateSocialLinkById_SocialLinkNotFound_ThrowsEntityNotFoundException() {
        // Test when the social link is not found
    }

    @Test
    public void updateSocialLinkById_InvalidRequest_ThrowsValidationException() {
        // Test when the request is invalid
    }

    // Tests for deleteSocialLinkById

    @Test
    public void deleteSocialLinkById_ValidRequest_DeletesSocialLink() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void deleteSocialLinkById_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void deleteSocialLinkById_SocialLinkNotFound_ThrowsEntityNotFoundException() {
        // Test when the social link is not found
    }
}
