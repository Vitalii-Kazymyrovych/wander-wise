package wander.wise.application.service;

public class CollectionServiceImplTest {

    // Tests for save

    @Test
    public void save_ValidRequest_SavesCollection() {
        // Test when the request is valid
    }

    @Test
    public void save_UnauthorizedUser_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void save_UserNotFound_ThrowsEntityNotFoundException() {
        // Test when the user is not found
    }

    // Tests for findById

    @Test
    public void findById_ValidRequest_ReturnsCollection() {
        // Test when the request is valid and the user has access
    }

    @Test
    public void findById_CollectionNotPublic_ThrowsAuthorizationException() {
        // Test when the collection is not public and the user does not have access
    }

    @Test
    public void findById_CollectionNotFound_ThrowsEntityNotFoundException() {
        // Test when the collection is not found
    }

    // Tests for updateById

    @Test
    public void updateById_ValidRequest_UpdatesCollection() {
        // Test when the request is valid and the user has authority
    }

    @Test
    public void updateById_UnauthorizedUser_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void updateById_CollectionNotFound_ThrowsEntityNotFoundException() {
        // Test when the collection is not found
    }

    @Test
    public void updateById_UpdatingDefaultCollection_ThrowsAuthorizationException() {
        // Test when trying to update a default collection
    }

    // Tests for deleteById

    @Test
    public void deleteById_ValidRequest_DeletesCollection() {
        // Test when the request is valid and the user has authority
    }

    @Test
    public void deleteById_UnauthorizedUser_ThrowsAuthorizationException() {
        // Test when the user is not authorized
    }

    @Test
    public void deleteById_CollectionNotFound_ThrowsEntityNotFoundException() {
        // Test when the collection is not found
    }

    @Test
    public void deleteById_DeletingDefaultCollection_ThrowsAuthorizationException() {
        // Test when trying to delete a default collection
    }
}
