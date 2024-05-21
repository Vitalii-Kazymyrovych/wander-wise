package wander.wise.application.constants;

public class SwaggerConstants {
    /**
     * Authentication controller documentation
     */
    public static final String REGISTER_NEW_USER_SUM = "Register a new user";
    public static final String REGISTER_NEW_USER_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User registered successfully.
            - `400 Bad Request`: Validation error or password mismatch.
            - `409 Conflict`: Account with the given email already exists.
            - `500 Internal server error`: may occur when there are any troubles 
            with 3-party API.
            2. **Authorities that have access to the endpoint:**
            -  Accessible by any user without authentication
            3. **Exceptions that can be thrown:**
            - `RegistrationException`: If an account with the provided email 
            already exists.
            - `EmailServiceException`: If there is an error sending the 
            confirmation email.
            4. **Request fields constraints (according to validation):**
            - `email`: Must be a valid email format and not blank.
            - `password`: Must be at least 8 characters long and not blank.
            - `repeatPassword`: Must be at least 8 characters long, not blank, 
            and must match the `password` field.
            5. **Other related information:**
            - Upon successful registration, the user will receive an email 
            with a confirmation code.
            - Default collections for liked, created, and saved cards are 
            initialized for the user.""";
    public static final String LOGIN_SUM = "User authentication";
    public static final String LOGIN_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Successful authentication
            - `401 UNAUTHORIZED`: Authentication failed due to invalid credentials
            - `400 BAD REQUEST`: Validation error in request fields
            2. **Authorities that have access to the endpoint:**
            -  Accessible by any user without authentication
            3. **Exceptions that can be thrown:**
            -  None
            4. **Request fields constraints (according to validation):**
            - `email`: Must be a well-formed email address
            - `password`: Must be at least 8 characters long
            5. **Other related information:**
            - Token is JWT type and must be used in the 
            'Authorization' header for subsequent requests
            - The token has a default expiry time after which it needs to be refreshed""";
    public static final String REFRESH_JWT_SUM = "Refresh JWT Token";
    public static final String REFRESH_JWT_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: JWT token successfully refreshed
            - `401 UNAUTHORIZED`: Failed to refresh JWT token due to invalid authentication
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `AuthenticationException`: Authentication information is missing or invalid
            - `UsernameNotFoundException`: No user found with the given email
            - `JwtException`: Issues with token generation or processing
            4. **Request fields constraints (according to validation):**
            - No request body is required for this endpoint
            5. **Other related information:**
            - The refreshed token will have a new expiry time 
            based on the current time of the request
            - The endpoint requires a valid JWT token to be 
            present in the 'Authorization' header""";
    public static final String CONFIRM_EMAIL_SUM = "Confirm user email and return login details";
    public static final String CONFIRM_EMAIL_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Email confirmed and user logged in
            - `404 NOT_FOUND`: Can't find user by email
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the email does not match any user
            4. **Request fields constraints (according to validation):**
            - `email`: Must not be blank and must be a valid email format
            5. **Other related information:**
            - This endpoint will unban the user and generate a new JWT token for authentication.""";
    public static final String RESTORE_PASSWORD_SUM = "Restore user password";
    public static final String RESTORE_PASSWORD_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: New password was sent to the user's email
            - `500 INTERNAL_SERVER_ERROR`: Can't send message due to email service error
            - `404 NOT_FOUND`: Can't find user by email
            2. **Authorities that have access to the endpoint:**
            -  Accessible by any user without authentication
            3. **Exceptions that can be thrown:**
            - `EmailServiceException`: When there is an issue with the email service
            - `EntityNotFoundException`: When the email does not match any user
            4. **Request fields constraints (according to validation):**
            - `email`: Must not be blank and must be a valid email format
            5. **Other related information:**
            - The endpoint generates a new random password, 
            encodes it, and sends it to the user's email.""";
    public static final String LOGOUT_SUM = "Logout a user";
    public static final String LOGOUT_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Success logout.
            - `403 FORBIDDEN`: Access denied.
            - `401 UNAUTHORIZED`: Expired or invalid JWT token.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `AuthorizationException`: Access denied or token was invalidated.
            - `JwtValidationException`: Expired or invalid JWT token.
            4. **Request fields constraints (according to validation):**
            - `token`: Must be a valid JWT token corresponding to the logged-in user.
            5. **Other related information:**
            - The token must not be expired or previously invalidated.
            - The token must belong to the authenticated user.""";

    /**
     * Cards controller documentation
     */
    public static final String SEARCH_CARDS_SUM = "Search for cards based on various parameters";
    public static final String SEARCH_CARDS_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Successful retrieval of search results
            - `404 NOT_FOUND`: No cards found matching the search criteria
            - `500 INTERNAL_SERVER_ERROR`: Internal server error
            - `400 BAD_REQUEST`: Request contains fields that violate validation constraints
            2. **Authorities that have access to the endpoint:**
            -  Accessible by any user without authentication
            3. **Exceptions that can be thrown:**
            - `CardSearchException`: No matching cards found or unable to generate cards
            - `AiException`: Error during AI processing
            - `ImageSearchException`: Error during image search
            - `MapsException`: Error during map retrieval
            4. **Request fields constraints (according to validation):**
            - `startLocation`: Must not be blank
            - `tripTypes`: No constraints
            - `climate`: No constraints
            - `specialRequirements`: No constraints
            - `travelDistance`: No constraints
            - `author`: No constraints
            5. **Other related information:**
            - The search is performed based on the provided `CardSearchParameters`.
            - Pagination is supported through `Pageable`.
            - The search will attempt to find existing cards or generate new ones if necessary.""";
    public static final String FIND_CARD_BY_ID_SUM = "Retrieve a card's details by its ID";
    public static final String FIND_CARD_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Successful retrieval of the card's details
            - `404 NOT_FOUND`: No card found with the provided ID
            2. **Authorities that have access to the endpoint:**
            -  Accessible by any user without authentication
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: No card found with the provided ID
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid card ID
            5. **Other related information:**
            - The endpoint retrieves the details of a card that is marked as shown.
            - If the card with the given ID is not marked as shown, it will not be retrieved.""";
    public static final String FIND_CARD_BY_ID_AS_ADMIN_SUM
            = "Retrieve a card's details by ID with admin privileges";
    public static final String FIND_CARD_BY_ID_AS_ADMIN_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Successful retrieval of the card's details
            - `404 NOT_FOUND`: No card found with the provided ID
            2. **Authorities that have access to the endpoint:**
            - `ADMIN`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: No card found with the provided ID
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid card ID
            5. **Other related information:**
            - This endpoint is restricted to users with admin authority.
            - It retrieves the details of a card regardless of its 'shown' status.""";
    public static final String CREATE_NEW_CARD_SUM = "Create a new card with the provided details";
    public static final String CREATE_NEW_CARD_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: New card created successfully
            - `403 FORBIDDEN`: Access denied, user is banned
            - `404 NOT_FOUND`: User by email not found
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find user by email
            - `AuthorizationException`: Access denied, user is banned
            4. **Request fields constraints (according to validation):**
            - `name`: Must not be blank
            - `populatedLocality`: Must not be blank
            - `region`: Must not be blank
            - `country`: Must not be blank
            - `continent`: Must not be blank
            - `tripTypes`: Must not be empty
            - `climate`: Must not be blank
            - `specialRequirements`: Must not be empty
            - `description`: Must not be blank
            - `whyThisPlace`: Must not be empty
            - `imageLinks`: No constraints
            - `mapLink`: Must not be blank and must be a valid Google maps link
            5. **Other related information:**
            - The endpoint is secured and can only be accessed by users with `ROLE_USER` authority.
            - The card creation process includes adding the new 
            card to the user's 'Created cards' collection.""";
    public static final String ADD_CARD_TO_SAVED_SUM
            = "Add a card to the user's \"Saved cards\" collection";
    public static final String ADD_CARD_TO_SAVED_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Card was successfully added to "Saved cards" collection.
            - `400 BAD_REQUEST`: Duplicate request. Card has already 
            been added to "Saved cards" collection.
            - `404 NOT_FOUND`: Can't find card by id.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find card by id.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid card ID
            5. **Other related information:**
            - The endpoint allows users to add a card to their "Saved cards" collection.
            - If the card is already in the collection, a duplicate request message is returned.""";
    public static final String REMOVE_CARD_FROM_SAVED_SUM
            = "Remove a card from the user's \"Saved cards\" collection";
    public static final String REMOVE_CARD_FROM_SAVED_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Card was successfully removed from "Saved cards" collection.
            - `400 BAD_REQUEST`: Error. "Saved cards" collection does not contain this card.
            - `404 NOT_FOUND`: Can't find card by id.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find card by id.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid card ID
            5. **Other related information:**
            - The endpoint allows users to remove a card from their "Saved cards" collection.
            - If the card is not in the collection, an error message is returned.""";
    public static final String UPDATE_CARD_BY_ID_SUM = "Update an existing card's details by ID";
    public static final String UPDATE_CARD_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Card details updated successfully
            - `403 FORBIDDEN`: Access denied, user is banned or not authorized
            - `404 NOT_FOUND`: Card or user by email not found
            2. **Authorities that have access to the endpoint:**
            - `USER`
            - `ADMIN`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Card or user by email not found
            - `AuthorizationException`: Access denied, user is banned or does 
            not has authority to update this card
            4. **Request fields constraints (according to validation):**
            - `name`: Must not be blank
            - `populatedLocality`: Must not be blank
            - `region`: Must not be blank
            - `country`: Must not be blank
            - `continent`: Must not be blank
            - `tripTypes`: Must not be empty
            - `climate`: Must not be blank
            - `specialRequirements`: Must not be empty
            - `description`: Must not be blank
            - `whyThisPlace`: Must not be empty
            - `imageLinks`: No constraints
            - `mapLink`: Must not be blank and must be a valid Google map link
            5. **Other related information:**
            - The endpoint allows users with proper authority to update card details. 
            Card can be updated by it's author or user with ADMIN authority.
            - Image links are updated and old images are deleted if not present in 
            the new request.""";
    public static final String ADD_IMAGES_TO_CARD_BY_ID_SUM = "Add images to a card by its ID";
    public static final String ADD_IMAGES_TO_CARD_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Images successfully added to the card
            - `403 FORBIDDEN`: Access denied, user is not authorized or banned
            - `404 NOT_FOUND`: Card or user by email not found
            2. **Authorities that have access to the endpoint:**
            - `USER`
            - `ADMIN`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Card or user by email not found
            - `AuthorizationException`: Access denied, user is banned or does 
            not has authority to update this card
            4. **Request fields constraints (according to validation):**
            - `images`: Must be valid image files
            5. **Other related information:**
            - The endpoint allows users with proper authority to add images to a card. 
            This action can be performed by it's author or user with ADMIN authority.
            - Images are uploaded and linked to the card's existing images.""";
    public static final String POST_LIKE_TO_CARD_SUM = "Post a like to a card by its ID";
    public static final String POST_LIKE_TO_CARD_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Card was liked.
            - `400 BAD_REQUEST`: Denied. Can't like one card multiple times.
            - `404 NOT_FOUND`: Can't find card by id.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find card by id.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid card ID
            5. **Other related information:**
            - The endpoint allows a user to like a card, incrementing the like count.
            - Liked card will be added to user's "Liked cards" collection.
            - A user cannot like the same card more than once.""";
    public static final String REMOVE_LIKE_FROM_CARD_SUM = "Remove a like from a card by its ID";
    public static final String REMOVE_LIKE_FROM_CARD_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Like was removed.
            - `400 BAD_REQUEST`: Denied. Can't remove like from card, that wasn't liked yet.
            - `404 NOT_FOUND`: Can't find card by id.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find card by id.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid card ID
            5. **Other related information:**
            - The endpoint allows users to remove a like from a card.
            - Liked card will be removed from user's "Liked cards" collection.
            - It decreases the like count of the card if the user had previously liked it.""";
    public static final String REPORT_CARD_SUM = "Report a card by its ID";
    public static final String REPORT_CARD_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Report message was sent to the util email.
            - `404 NOT_FOUND`: Can't find card by id.
            - `500 INTERNAL_SERVER_ERROR`: Can't send report email.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find card by id.
            - `EmailServiceException`: Can't send report email.
            4. **Request fields constraints (according to validation):**
            - `text`: Must not be blank
            5. **Other related information:**
            - The endpoint allows users to report a card, which increments 
            the report count and sends an email notification.
            - The report includes the user's email, card ID, and report text.""";
    public static final String HIDE_CARD_SUM = "Hide a card from public view by its ID";
    public static final String HIDE_CARD_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Card was successfully hidden.
            - `400 BAD_REQUEST`: Duplicate request. Card has already been hidden.
            - `404 NOT_FOUND`: Can't find card by id.
            2. **Authorities that have access to the endpoint:**
            - `ADMIN`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find card by id.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid card ID
            5. **Other related information:**
            - The endpoint is available to admin users.
            - It allows admins to hide a card, making it invisible to regular users.""";
    public static final String REVEAL_CARD_SUM = "Reveal a previously hidden card by its ID";
    public static final String REVEAL_CARD_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Card was successfully revealed.
            - `400 BAD_REQUEST`: Duplicate request. Card has already been revealed.
            - `404 NOT_FOUND`: Can't find card by id.
            2. **Authorities that have access to the endpoint:**
            - `ADMIN`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find card by id.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid card ID
            5. **Other related information:**
            - The endpoint is available to admin users.
            - It allows admins to make a previously hidden card visible to all users again.""";
    public static final String DELETE_CARD_SUM = "Delete a card by its ID";
    public static final String DELETE_CARD_DESC = """
            1. **Possible response codes and messages:**
            - `204 NO_CONTENT`: Card was successfully deleted.
            - `403 FORBIDDEN`: Access denied, user is not authorized to delete this card.
            - `404 NOT_FOUND`: Card or user by email not found.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            - `ADMIN`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Card or user by email not found.
            - `AuthorizationException`: Access denied, user is not authorized to delete this card.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid card ID
            5. **Other related information:**
            - The endpoint allows users with proper authority to delete a card.
            - User should have ADMIN authority or be the card's author.
            - Deletion is permanent and cannot be undone.""";

    /**
     * Collections controller documentation
     */
    public static final String SAVE_COLLECTION_SUM = "Save a new collection of cards";
    public static final String SAVE_COLLECTION_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Collection created and saved successfully
            - `403 FORBIDDEN`: Access denied, user is not authorized to create this collection
            - `404 NOT_FOUND`: User by ID not found
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: User by ID not found
            - `AuthorizationException`: Access denied, user is 
            not authorized to create this collection
            4. **Request fields constraints (according to validation):**
            - `userId`: Must not be null
            - `cardIds`: Must not be null and must contain valid card IDs
            5. **Other related information:**
            - The collection name is auto-generated based on the names of the first three cards.
            - The collection is associated with the authenticated user.""";
    public static final String FIND_COLLECTION_BY_ID_SUM = "Find a collection by its ID";
    public static final String FIND_COLLECTION_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Collection found and returned successfully
            - `403 FORBIDDEN`: You don't have access to this collection. 
            Ask its owner to make it public.
            - `404 NOT_FOUND`: Can't find collection by id.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find collection by id.
            - `AuthorizationException`: You don't have access to this 
            collection. Ask its owner to make it public.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid collection ID
            5. **Other related information:**
            - The endpoint retrieves a collection if it is public 
            or if the authenticated user is the owner.
            - Private collections can only be accessed by their owners.""";
    public static final String UPDATE_COLLECTION_BY_ID_SUM = "Update a collection by its ID";
    public static final String UPDATE_COLLECTION_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Collection updated successfully
            - `403 FORBIDDEN`: Access denied, cannot change default collections or unauthorized user
            - `404 NOT_FOUND`: Collection or user by ID not found
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Collection or user by ID not found
            - `AuthorizationException`: Access denied, cannot change 
            default collections or unauthorized user
            4. **Request fields constraints (according to validation):**
            - `name`: Must not be blank
            - `cardIds`: Must not be null and must contain valid card IDs
            - `isPublic`: Must not be null
            5. **Other related information:**
            - Default collections such as 'Liked cards', 'Created cards', 
            and 'Saved cards' cannot be modified.
            - The collection's visibility can be toggled between public and private.""";
    public static final String DELETE_COLLECTION_BY_ID_SUM = "Delete a collection by its ID";
    public static final String DELETE_COLLECTION_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - `204 NO_CONTENT`: Collection was successfully deleted.
            - `403 FORBIDDEN`: Access denied. You can't delete default collections.
            - `404 NOT_FOUND`: Can't find collection by id.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find collection by id.
            - `AuthorizationException`: Access denied. You can't delete default collections.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid collection ID
            5. **Other related information:**
            - Users are not allowed to delete default collections 
            such as 'Liked cards', 'Created cards', and 'Saved cards'.
            - Only the owner of the collection can delete a collection.""";

    /**
     * Comments controller documentation
     */
    public static final String SAVE_COMMENT_SUM = "Save a new comment on a card";
    public static final String SAVE_COMMENT_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Comment created and saved successfully
            - `403 FORBIDDEN`: Access denied, user is banned
            - `404 NOT_FOUND`: User by email not found
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: User by email not found
            - `AuthorizationException`: Access denied, user is banned
            4. **Request fields constraints (according to validation):**
            - `cardId`: Must not be null
            - `text`: Must not be blank
            - `stars`: Must not be null, must be at least 1 and at most 5
            5. **Other related information:**
            - The comment is associated with the authenticated user.
            - The timestamp of the comment is set to the current time upon saving.""";
    public static final String UPDATE_COMMENT_SUM = "Update an existing comment";
    public static final String UPDATE_COMMENT_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Comment updated successfully
            - `403 FORBIDDEN`: Access denied, user is not authorized to update this comment
            - `404 NOT_FOUND`: Comment or user by ID not found
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Comment or user by ID not found
            - `AuthorizationException`: Access denied, user is not authorized to update this comment
            4. **Request fields constraints (according to validation):**
            - `cardId`: Must not be null
            - `text`: Must not be blank
            - `stars`: Must not be null, must be at least 1 and at most 5
            5. **Other related information:**
            - The comment's card ID, text, and star rating can be updated.
            - The timestamp of the comment is not updated.""";
    public static final String REPORT_COMMENT_SUM = "Report a comment by its ID";
    public static final String REPORT_COMMENT_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Report message was sent successfully.
            - `404 NOT_FOUND`: Can't find comment by id.
            - `500 INTERNAL_SERVER_ERROR`: Email service failed to send the report.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find comment by id.
            - `EmailServiceException`: Email service failed to send the report.
            4. **Request fields constraints (according to validation):**
            - `commentAuthor`: Must not be blank
            - `commentText`: Must not be blank
            - `reportText`: Must not be blank
            5. **Other related information:**
            - The report includes the user's email, comment author, comment text, and report text.
            - The reported comment's report count is incremented.
            - Reports are sent to a predefined email address.""";
    public static final String DELETE_COMMENT_SUM = "Delete a comment by its ID";
    public static final String DELETE_COMMENT_DESC = """
            1. **Possible response codes and messages:**
            - `204 NO_CONTENT`: Comment was successfully deleted.
            - `403 FORBIDDEN`: Access denied. You can't delete comments of this user.
            - `404 NOT_FOUND`: Can't find comment by id or can't find user by id.
            2. **Authorities that have access to the endpoint:**
            - `USER`
            - `ADMIN`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: Can't find comment by id or can't find user by id.
            - `AuthorizationException`: Access denied. You can't delete comments of this user.
            4. **Request fields constraints (according to validation):**
            - `id`: Must be a valid comment ID
            5. **Other related information:**
            - Only the author of the comment or an admin can delete the comment.""";

    /**
     * Social links controller documentation
     */
    public static final String ADD_SOCIAL_LINK_SUM = "Add a new social link";
    public static final String ADD_SOCIAL_LINK_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Social link successfully added
            - `403 FORBIDDEN`: Access denied
            - `404 NOT FOUND`: Can't find user by id
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user with the given ID does not exist
            - `AuthorizationException`: When the authenticated user is 
            not authorized to add a social link for the given user ID
            4. **Request fields constraints (according to validation):**
            - `userId`: Must not be null and must be a positive number
            - `name`: Must not be blank
            - `link`: Must not be blank and must be a valid URL
            5. **Other related information:**
            - Inner method contains additional authorization check. 
            You can't create social links for another user.""";
    public static final String UPDATE_SOCIAL_LINK_SUM = "Update an existing social link";
    public static final String UPDATE_SOCIAL_LINK_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Social link updated successfully
            - `404 NOT FOUND`: Can't find social link by id: {id}
            - `403 FORBIDDEN`: Access denied
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the social link or user is not found by id
            - `AuthorizationException`: When the authenticated user 
            is not authorized to update given social link
            4. **Request fields constraints (according to validation):**
            - `userId`: Must not be null and must be a positive number
            - `name`: Must not be blank
            - `link`: Must not be blank and must be a valid URL
            5. **Other related information:**
            - Inner method contains additional authorization check. 
            You can't update social links for another user.""";
    public static final String DELETE_SOCIAL_LINK_SUM = "Delete a social link";
    public static final String DELETE_SOCIAL_LINK_DESC = """
            1. **Possible response codes and messages:**
            - `204 NO CONTENT`: Social link deleted successfully
            - `404 NOT FOUND`: Can't find social link by id: {id}
            - `403 FORBIDDEN`: Access denied
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the social link is not found by id
            - `AuthorizationException`: When the authenticated user 
            is not authorized to delete given social link
            4. **Request fields constraints (according to validation):**
            - There are no request body fields for this DELETE operation.
            5. **Other related information:**
            - Inner method contains additional authorization check. 
            You can't delete social links for another user.""";

    /**
     * Users controller documentation
     */
    public static final String GET_USER_PROFILE_SUM = "Retrieve a user's profile";
    public static final String GET_USER_PROFILE_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User profile retrieved successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            2. **Authorities that have access to the endpoint:**
            - This endpoint is publicly accessible.
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            4. **Request fields constraints (according to validation):**
            - There are no request body fields for this GET operation.
            5. **Other related information:**
            - The endpoint provides the profile information of 
            a user, including personal details and roles.""";
    public static final String GET_USER_SOCIAL_LINKS_SUM = "Retrieve a user's social links";
    public static final String GET_USER_SOCIAL_LINKS_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User's social links retrieved successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            2. **Authorities that have access to the endpoint:**
            - This endpoint is publicly accessible.
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            4. **Request fields constraints (according to validation):**
            - There are no request body fields for this GET operation.
            5. **Other related information:**
            - The endpoint provides a list of social links associated with a user's profile.""";
    public static final String GET_USER_COLLECTIONS_SUM = "Retrieve a user's collections";
    public static final String GET_USER_COLLECTIONS_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User's collections retrieved successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            - `403 FORBIDDEN`: Access denied
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            - `AuthorizationException`: When the user's email does 
            not match the authenticated user's email
            4. **Request fields constraints (according to validation):**
            - There are no request body fields for this GET operation.
            5. **Other related information:**
            - The endpoint initializes the image link of a collection 
            if it is not set and the collection has cards.""";
    public static final String GET_USER_COMMENTS_SUM = "Retrieve a user's comments";
    public static final String GET_USER_COMMENTS_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User's comments retrieved successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            - `403 FORBIDDEN`: Access denied
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            - `AuthorizationException`: When the user's email does 
            not match the authenticated user's email
            4. **Request fields constraints (according to validation):**
            - There are no request body fields for this GET operation.
            5. **Other related information:**
            - The endpoint retrieves all comments associated with the user's email.""";
    public static final String UPDATE_USER_INFO_SUM = "Update user information";
    public static final String UPDATE_USER_INFO_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User information updated successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            - `403 FORBIDDEN`: Access denied
            - `409 CONFLICT`: Such user already exists
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            - `AuthorizationException`: When the user's email does 
            not match the authenticated user's email
            - `RegistrationException`: When the pseudonym is already in use by another user
            4. **Request fields constraints (according to validation):**
            - `pseudonym`: Must not be blank
            - `firstName`: No constraints
            - `lastName`: No constraints
            - `location`: No constraints
            - `bio`: No constraints
            5. **Other related information:**
            - The endpoint allows updating user's pseudonym, 
            first name, last name, location, and bio.""";
    public static final String UPDATE_USER_IMAGE_SUM = "Update user profile image";
    public static final String UPDATE_USER_IMAGE_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User profile image updated successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            - `403 FORBIDDEN`: Access denied
            - `500 INTERNAL_SERVER_ERROR`: When there is problem with cloud storage
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            - `AuthorizationException`: When the user's email does not 
            match the authenticated user's email
            - `StorageException`: When there is a problem with saving files to cloud storage
            4. **Request fields constraints (according to validation):**
            - `image`: Must be a valid image file
            5. **Other related information:**
            - The endpoint allows users to update their profile image. 
            If an image is not provided, the current image will be removed.
            - The service handles image storage and deletion operations.""";
    public static final String REQUEST_UPDATE_USER_EMAIL_SUM = "Request update of user email";
    public static final String REQUEST_UPDATE_USER_EMAIL_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Email update request successful, confirmation code sent
            - `404 NOT FOUND`: Can't find user by id: {id}
            - `403 FORBIDDEN`: Access denied
            - `500 INTERNAL SERVER ERROR`: Can't send message
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            - `AuthorizationException`: When the user's email does not 
            match the authenticated user's email
            - `EmailServiceException`: When there is an error sending the email
            4. **Request fields constraints (according to validation):**
            - `email`: Must not be blank and must be a valid email format
            5. **Other related information:**
            - The endpoint initiates an email update process by sending 
            a confirmation code to the new email.
            - The `emailConfirmCode` is generated and sent to the user's 
            new email address.""";
    public static final String UPDATE_USER_EMAIL_SUM = "Update user email";
    public static final String UPDATE_USER_EMAIL_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User email updated successfully, new token issued
            - `404 NOT FOUND`: Can't find user by id: {id}
            - `403 FORBIDDEN`: Access denied
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            - `AuthorizationException`: When the user's email does not 
            match the authenticated user's email
            4. **Request fields constraints (according to validation):**
            - `email`: Must not be blank and must be a valid email format
            5. **Other related information:**
            - The endpoint updates the user's email and issues a new authentication token.""";
    public static final String UPDATE_USER_PASSWORD_SUM = "Update user password";
    public static final String UPDATE_USER_PASSWORD_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User password updated successfully, new token issued
            - `404 NOT FOUND`: Can't find user by id: {id}
            - `403 FORBIDDEN`: Access denied
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            - `AuthorizationException`: When the user's email does not match the 
            authenticated user's email
            4. **Request fields constraints (according to validation):**
            - `oldPassword`: Must not be blank and must be at least 8 characters
            - `password`: Must not be blank and must be at least 8 characters
            - `repeatPassword`: Must not be blank, be at least 8 characters and 
            match password field
            5. **Other related information:**
            - The endpoint updates the user's password after validating the 
            old password and issues a new authentication token.""";
    public static final String UPDATE_USER_ROLES_SUM = "Update user roles";
    public static final String UPDATE_USER_ROLES_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User roles updated successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            2. **Authorities that have access to the endpoint:**
            - `ROOT`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            4. **Request fields constraints (according to validation):**
            - `roleIds`: Must not be empty. Must contain required role and 
            all lower roles. For example, administrator should have roles 
            2 (ADMIN) and 3 (USER).
            5. **Other related information:**
            - The endpoint updates the roles of a user based on the provided role IDs.""";
    public static final String DELETE_USER_SUM = "Delete a user";
    public static final String DELETE_USER_DESC = """
            1. **Possible response codes and messages:**
            - `204 NO CONTENT`: User deleted successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            - `403 FORBIDDEN`: Access denied
            2. **Authorities that have access to the endpoint:**
            - `USER`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            - `AuthorizationException`: When the user's email does not 
            match the authenticated user's email
            4. **Request fields constraints (according to validation):**
            - There are no request body fields for this DELETE operation.
            5. **Other related information:**
            - The endpoint deletes the user with the specified ID after authorization checks.""";
    public static final String BAN_USER_SUM = "Ban a user";
    public static final String BAN_USER_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User banned successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            2. **Authorities that have access to the endpoint:**
            - `ADMIN`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            4. **Request fields constraints (according to validation):**
            - There are no request body fields for this GET operation.
            5. **Other related information:**
            - The endpoint sets the `banned` status of a user to `true`.""";
    public static final String UNBAN_USER_SUM = "Unban a user";
    public static final String UNBAN_USER_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: User unbanned successfully
            - `404 NOT FOUND`: Can't find user by id: {id}
            2. **Authorities that have access to the endpoint:**
            - `ADMIN`
            3. **Exceptions that can be thrown:**
            - `EntityNotFoundException`: When the user is not found by id
            4. **Request fields constraints (according to validation):**
            - There are no request body fields for this GET operation.
            5. **Other related information:**
            - The endpoint sets the `banned` status of a user to `false`, 
            effectively unbanning them.""";









}
