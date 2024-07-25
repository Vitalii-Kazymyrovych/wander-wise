package wander.wise.application.service;

import org.junit.jupiter.api.Test;

public class CardServiceTest {

    // Tests for createNewCard

    @Test
    public void createNewCard_ValidRequest_CreatesCard() {
        // Test when the request is valid and the user is not banned
    }

    @Test
    public void createNewCard_UserIsBanned_ThrowsAuthorizationException() {
        // Test when the user is banned
    }

    // Tests for updateById

    @Test
    public void updateById_ValidRequest_UpdatesCard() {
        // Test when the request is valid and the user has authority
    }

    @Test
    public void updateById_UserHasNoAuthority_ThrowsAuthorizationException() {
        // Test when the user has no authority
    }

    @Test
    public void updateById_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for addImagesToCardById

    @Test
    public void addImagesToCardById_ValidRequest_AddsImages() {
        // Test when the request is valid and the user has authority
    }

    @Test
    public void addImagesToCardById_UserHasNoAuthority_ThrowsAuthorizationException() {
        // Test when the user has no authority
    }

    @Test
    public void addImagesToCardById_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for findById

    @Test
    public void findById_ValidId_ReturnsCard() {
        // Test when the card ID is valid
    }

    @Test
    public void findById_CardNotShown_ThrowsEntityNotFoundException() {
        // Test when the card is not shown
    }

    @Test
    public void findById_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for findByIdAsAdmin

    @Test
    public void findByIdAsAdmin_ValidId_ReturnsCard() {
        // Test when the card ID is valid
    }

    @Test
    public void findByIdAsAdmin_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for addCardToSaved

    @Test
    public void addCardToSaved_ValidRequest_AddsCardToSaved() {
        // Test when the request is valid and the card is not already saved
    }

    @Test
    public void addCardToSaved_CardAlreadySaved_ReturnsFalse() {
        // Test when the card is already saved
    }

    @Test
    public void addCardToSaved_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for removeCardFromSaved

    @Test
    public void removeCardFromSaved_ValidRequest_RemovesCardFromSaved() {
        // Test when the request is valid and the card is already saved
    }

    @Test
    public void removeCardFromSaved_CardNotSaved_ReturnsFalse() {
        // Test when the card is not saved
    }

    @Test
    public void removeCardFromSaved_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for getRandomCards

    @Test
    public void getRandomCards_ValidNumber_ReturnsRandomCards() {
        // Test when the number of random cards is valid
    }

    @Test
    public void getRandomCards_ExceedsTotalCards_ReturnsAllCards() {
        // Test when the number exceeds the total cards available
    }

    // Tests for postLike

    @Test
    public void postLike_ValidRequest_PostsLike() {
        // Test when the request is valid and the card is not already liked
    }

    @Test
    public void postLike_CardAlreadyLiked_ReturnsFalse() {
        // Test when the card is already liked
    }

    @Test
    public void postLike_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for removeLike

    @Test
    public void removeLike_ValidRequest_RemovesLike() {
        // Test when the request is valid and the card is already liked
    }

    @Test
    public void removeLike_CardNotLiked_ReturnsFalse() {
        // Test when the card is not liked
    }

    @Test
    public void removeLike_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for hideCard

    @Test
    public void hideCard_ValidRequest_HidesCard() {
        // Test when the request is valid and the card is shown
    }

    @Test
    public void hideCard_CardAlreadyHidden_ReturnsFalse() {
        // Test when the card is already hidden
    }

    @Test
    public void hideCard_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for revealCard

    @Test
    public void revealCard_ValidRequest_RevealsCard() {
        // Test when the request is valid and the card is hidden
    }

    @Test
    public void revealCard_CardAlreadyShown_ReturnsFalse() {
        // Test when the card is already shown
    }

    @Test
    public void revealCard_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for deleteById

    @Test
    public void deleteById_ValidRequest_DeletesCard() {
        // Test when the request is valid and the user has authority
    }

    @Test
    public void deleteById_UserHasNoAuthority_ThrowsAuthorizationException() {
        // Test when the user has no authority
    }

    @Test
    public void deleteById_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for report

    @Test
    public void report_ValidRequest_ReportsCard() {
        // Test when the request is valid
    }

    @Test
    public void report_CardNotFound_ThrowsEntityNotFoundException() {
        // Test when the card is not found
    }

    // Tests for search

    @Test
    public void search_ValidParameters_ReturnsSearchResults() {
        // Test when the search parameters are valid
    }

    @Test
    public void search_NoMatchingCards_ReturnsEmptyList() {
        // Test when there are no matching cards
    }
}
