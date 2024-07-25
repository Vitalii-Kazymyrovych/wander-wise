package wander.wise.application.service;

public class UserServiceTest {

    // Tests for save

    @Test
    public void save_ValidRequest_SavesUser() {
        // Test when the request is valid
    }

    @Test
    public void save_EmailAlreadyExists_ThrowsRegistrationException() {
        // Test when the email already exists
    }

    @Test
    public void save_DeletedUserExists_RestoresUser() {
        // Test when a deleted user with the same email exists
    }

    // Tests for confirmEmail

    @Test
    public void confirmEmail_ValidEmail_ConfirmsEmail() {
        // Test when the email is valid
    }

    @Test
    public void confirmEmail_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for findById

    @Test
    public void findById_ValidId_ReturnsUser() {
        // Test when the ID is valid
    }

    @Test
    public void findById_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for getUserSocialLinks

    @Test
    public void getUserSocialLinks_ValidId_ReturnsSocialLinks() {
        // Test when the ID is valid
    }

    @Test
    public void getUserSocialLinks_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for getUserCollections

    @Test
    public void getUserCollections_ValidRequest_ReturnsCollections() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void getUserCollections_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void getUserCollections_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for getUserComments

    @Test
    public void getUserComments_ValidRequest_ReturnsComments() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void getUserComments_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void getUserComments_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for restorePassword

    @Test
    public void restorePassword_ValidRequest_RestoresPassword() {
        // Test when the request is valid
    }

    @Test
    public void restorePassword_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for updateUserInfo

    @Test
    public void updateUserInfo_ValidRequest_UpdatesUserInfo() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void updateUserInfo_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void updateUserInfo_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    @Test
    public void updateUserInfo_PseudonymAlreadyExists_ThrowsRegistrationException() {
        // Test when the pseudonym already exists
    }

    // Tests for updateUserImage

    @Test
    public void updateUserImage_ValidRequest_UpdatesUserImage() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void updateUserImage_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void updateUserImage_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for updateUserRoles

    @Test
    public void updateUserRoles_ValidRequest_UpdatesUserRoles() {
        // Test when the request is valid and the user has authority
    }

    @Test
    public void updateUserRoles_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for requestUpdateUserEmail

    @Test
    public void requestUpdateUserEmail_ValidRequest_SendsEmailConfirmation() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void requestUpdateUserEmail_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void requestUpdateUserEmail_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    @Test
    public void requestUpdateUserEmail_EmailAlreadyExists_ThrowsRegistrationException() {
        // Test when the email already exists
    }

    // Tests for updateUserEmail

    @Test
    public void updateUserEmail_ValidRequest_UpdatesUserEmail() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void updateUserEmail_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void updateUserEmail_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    @Test
    public void updateUserEmail_EmailAlreadyExists_ThrowsRegistrationException() {
        // Test when the email already exists
    }

    // Tests for updateUserPassword

    @Test
    public void updateUserPassword_ValidRequest_UpdatesPassword() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void updateUserPassword_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void updateUserPassword_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    @Test
    public void updateUserPassword_AuthenticationFailed_ThrowsAuthenticationException() {
        // Test when authentication fails
    }

    // Tests for deleteUser

    @Test
    public void deleteUser_ValidRequest_DeletesUser() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void deleteUser_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void deleteUser_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for banUser

    @Test
    public void banUser_ValidRequest_BansUser() {
        // Test when the request is valid
    }

    @Test
    public void banUser_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for unbanUser

    @Test
    public void unbanUser_ValidRequest_UnbansUser() {
        // Test when the request is valid
    }

    @Test
    public void unbanUser_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for findUserAndAuthorize

    @Test
    public void findUserAndAuthorize_ValidRequest_ReturnsUser() {
        // Test when the request is valid and the user is authorized
    }

    @Test
    public void findUserAndAuthorize_UserNotAuthorized_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void findUserAndAuthorize_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for findUserEntityById

    @Test
    public void findUserEntityById_ValidId_ReturnsUser() {
        // Test when the ID is valid
    }

    @Test
    public void findUserEntityById_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for findUserEntityByEmail

    @Test
    public void findUserEntityByEmail_ValidEmail_ReturnsUser() {
        // Test when the email is valid
    }

    @Test
    public void findUserEntityByEmail_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }
}
