package wander.wise.application.service;

import org.junit.jupiter.api.Test;

public class CommentServiceTest {

    // Tests for save

    @Test
    public void save_ValidRequest_SavesComment() {
        // Test when the request is valid and the user is not banned
    }

    @Test
    public void save_UserIsBanned_ThrowsAuthorizationException() {
        // Test when the user is banned
    }

    @Test
    public void save_InvalidRequest_ThrowsValidationException() {
        // Test when the request is invalid
    }

    // Tests for update

    @Test
    public void update_ValidRequest_UpdatesComment() {
        // Test when the request is valid and the user has authority
    }

    @Test
    public void update_UserHasNoAuthority_ThrowsAuthorizationException() {
        // Test when the user has no authority
    }

    @Test
    public void update_CommentNotFound_ThrowsEntityNotFoundException() {
        // Test when the comment is not found
    }

    @Test
    public void update_InvalidRequest_ThrowsValidationException() {
        // Test when the request is invalid
    }

    // Tests for report

    @Test
    public void report_ValidRequest_ReportsComment() {
        // Test when the request is valid
    }

    @Test
    public void report_CommentNotFound_ThrowsEntityNotFoundException() {
        // Test when the comment is not found
    }

    @Test
    public void report_InvalidRequest_ThrowsValidationException() {
        // Test when the request is invalid
    }

    // Tests for deleteById

    @Test
    public void deleteById_ValidRequest_DeletesComment() {
        // Test when the request is valid and the user has authority
    }

    @Test
    public void deleteById_UserHasNoAuthority_ThrowsAuthorizationException() {
        // Test when the user has no authority
    }

    @Test
    public void deleteById_CommentNotFound_ThrowsEntityNotFoundException() {
        // Test when the comment is not found
    }

    @Test
    public void deleteById_InvalidRequest_ThrowsValidationException() {
        // Test when the request is invalid
    }
}
