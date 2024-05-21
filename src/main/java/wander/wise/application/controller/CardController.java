package wander.wise.application.controller;

import static wander.wise.application.constants.SwaggerConstants.ADD_CARD_TO_SAVED_DESC;
import static wander.wise.application.constants.SwaggerConstants.ADD_CARD_TO_SAVED_SUM;
import static wander.wise.application.constants.SwaggerConstants.ADD_IMAGES_TO_CARD_BY_ID_DESC;
import static wander.wise.application.constants.SwaggerConstants.ADD_IMAGES_TO_CARD_BY_ID_SUM;
import static wander.wise.application.constants.SwaggerConstants.CREATE_NEW_CARD_DESC;
import static wander.wise.application.constants.SwaggerConstants.CREATE_NEW_CARD_SUM;
import static wander.wise.application.constants.SwaggerConstants.DELETE_CARD_DESC;
import static wander.wise.application.constants.SwaggerConstants.DELETE_CARD_SUM;
import static wander.wise.application.constants.SwaggerConstants.FIND_CARD_BY_ID_AS_ADMIN_DESC;
import static wander.wise.application.constants.SwaggerConstants.FIND_CARD_BY_ID_AS_ADMIN_SUM;
import static wander.wise.application.constants.SwaggerConstants.FIND_CARD_BY_ID_DESC;
import static wander.wise.application.constants.SwaggerConstants.FIND_CARD_BY_ID_SUM;
import static wander.wise.application.constants.SwaggerConstants.HIDE_CARD_DESC;
import static wander.wise.application.constants.SwaggerConstants.HIDE_CARD_SUM;
import static wander.wise.application.constants.SwaggerConstants.POST_LIKE_TO_CARD_DESC;
import static wander.wise.application.constants.SwaggerConstants.POST_LIKE_TO_CARD_SUM;
import static wander.wise.application.constants.SwaggerConstants.REMOVE_CARD_FROM_SAVED_DESC;
import static wander.wise.application.constants.SwaggerConstants.REMOVE_CARD_FROM_SAVED_SUM;
import static wander.wise.application.constants.SwaggerConstants.REMOVE_LIKE_FROM_CARD_DESC;
import static wander.wise.application.constants.SwaggerConstants.REMOVE_LIKE_FROM_CARD_SUM;
import static wander.wise.application.constants.SwaggerConstants.REPORT_CARD_DESC;
import static wander.wise.application.constants.SwaggerConstants.REPORT_CARD_SUM;
import static wander.wise.application.constants.SwaggerConstants.REVEAL_CARD_DESC;
import static wander.wise.application.constants.SwaggerConstants.REVEAL_CARD_SUM;
import static wander.wise.application.constants.SwaggerConstants.SEARCH_CARDS_DESC;
import static wander.wise.application.constants.SwaggerConstants.SEARCH_CARDS_SUM;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_CARD_BY_ID_DESC;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_CARD_BY_ID_SUM;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
import wander.wise.application.dto.card.CardDto;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.dto.card.CreateCardRequestDto;
import wander.wise.application.dto.card.ReportCardRequestDto;
import wander.wise.application.dto.card.SearchCardsResponseDto;
import wander.wise.application.service.card.CardService;

@Tag(name = "Card management endpoints")
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {
    private static final String REPORT_EMAIL = "budzetbudzet4@gmail.com";
    private final CardService cardService;

    @PostMapping("/search")
    @Operation(summary = SEARCH_CARDS_SUM, description = SEARCH_CARDS_DESC)
    public SearchCardsResponseDto search(@RequestBody CardSearchParameters searchParameters,
                                         Pageable pageable) {
        return cardService.search(pageable, searchParameters);
    }

    @GetMapping("/details/{id}")
    @Operation(summary = FIND_CARD_BY_ID_SUM, description = FIND_CARD_BY_ID_DESC)
    public CardDto findById(@PathVariable Long id) {
        return cardService.findById(id);
    }

    @GetMapping("/as-admin/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = FIND_CARD_BY_ID_AS_ADMIN_SUM, description = FIND_CARD_BY_ID_AS_ADMIN_DESC)
    public CardDto findByIdAsAdmin(@PathVariable Long id) {
        return cardService.findByIdAsAdmin(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = CREATE_NEW_CARD_SUM, description = CREATE_NEW_CARD_DESC)
    public CardDto createNewCard(
            Authentication authentication,
            @Valid @RequestBody CreateCardRequestDto requestDto) {
        return cardService.createNewCard(authentication.getName(), requestDto);
    }

    @PutMapping("/add-to-saved/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = ADD_CARD_TO_SAVED_SUM, description = ADD_CARD_TO_SAVED_DESC)
    public ResponseEntity<String> addCardToSaved(@PathVariable Long id,
                                                 Authentication authentication) {
        if (cardService.addCardToSaved(id, authentication.getName())) {
            return new ResponseEntity<>(
                    "Card was successfully added to \"Saved cards\" collection.",
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Duplicate request. Card has already been added to \"Saved cards\" collection.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/remove-from-saved/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = REMOVE_CARD_FROM_SAVED_SUM, description = REMOVE_CARD_FROM_SAVED_DESC)
    public ResponseEntity<String> removeCardFromSaved(@PathVariable Long id,
                                                      Authentication authentication) {
        if (cardService.removeCardFromSaved(id, authentication.getName())) {
            return new ResponseEntity<>(
                    "Card was successfully removed from \"Saved cards\" collection.",
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Error. \"Saved cards\" collection does not contain this card.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = UPDATE_CARD_BY_ID_SUM, description = UPDATE_CARD_BY_ID_DESC)
    public CardDto updateById(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody CreateCardRequestDto requestDto) {
        return cardService.updateById(id, authentication.getName(), requestDto);
    }

    @PutMapping("/add-images/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = ADD_IMAGES_TO_CARD_BY_ID_SUM, description = ADD_IMAGES_TO_CARD_BY_ID_DESC)
    public CardDto addImagesToCardById(
            @PathVariable Long id,
            Authentication authentication,
            @RequestParam(value = "images") MultipartFile[] images) {
        return cardService.addImagesToCardById(id,
                authentication.getName(),
                Arrays.stream(images).toList());
    }

    @PutMapping("/post-like/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = POST_LIKE_TO_CARD_SUM, description = POST_LIKE_TO_CARD_DESC)
    public ResponseEntity<String> postLike(@PathVariable Long id, Authentication authentication) {
        if (cardService.postLike(id, authentication.getName())) {
            return new ResponseEntity<>(
                    "Card was liked.",
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Denied. Can't like one card multiple times.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/remove-like/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = REMOVE_LIKE_FROM_CARD_SUM, description = REMOVE_LIKE_FROM_CARD_DESC)
    public ResponseEntity<String> removeLike(@PathVariable Long id, Authentication authentication) {
        if (cardService.removeLike(id, authentication.getName())) {
            return new ResponseEntity<>(
                    "Like was removed.",
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Denied. Can't remove like from card, that wasn't liked yet.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/report/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = REPORT_CARD_SUM, description = REPORT_CARD_DESC)
    public ResponseEntity<String> report(@PathVariable Long id,
                                         Authentication authentication,
                                         @Valid @RequestBody ReportCardRequestDto requestDto) {
        cardService.report(id, authentication.getName(), requestDto);
        return new ResponseEntity<>(
                "Report message was sent to email: " 
                        + REPORT_EMAIL,
                HttpStatus.OK);
    }

    @GetMapping("/hide-card/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = HIDE_CARD_SUM, description = HIDE_CARD_DESC)
    public ResponseEntity<String> hideCard(@PathVariable Long id) {
        if (cardService.hideCard(id)) {
            return new ResponseEntity<>(
                    "Card was successfully hidden.",
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Duplicate request. Card has already been hidden.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/reveal-card/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = REVEAL_CARD_SUM, description = REVEAL_CARD_DESC)
    public ResponseEntity<String> revealCard(@PathVariable Long id) {
        if (cardService.revealCard(id)) {
            return new ResponseEntity<>(
                    "Card was successfully revealed.",
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Duplicate request. Card has already been revealed.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = DELETE_CARD_SUM, description = DELETE_CARD_DESC)
    public ResponseEntity<String> deleteCard(@PathVariable Long id, Authentication authentication) {
        cardService.deleteById(id, authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
