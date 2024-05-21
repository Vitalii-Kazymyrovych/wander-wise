package wander.wise.application.controller;

import static wander.wise.application.constants.SwaggerConstants.DELETE_COLLECTION_BY_ID_DESC;
import static wander.wise.application.constants.SwaggerConstants.DELETE_COLLECTION_BY_ID_SUM;
import static wander.wise.application.constants.SwaggerConstants.FIND_COLLECTION_BY_ID_DESC;
import static wander.wise.application.constants.SwaggerConstants.FIND_COLLECTION_BY_ID_SUM;
import static wander.wise.application.constants.SwaggerConstants.SAVE_COLLECTION_DESC;
import static wander.wise.application.constants.SwaggerConstants.SAVE_COLLECTION_SUM;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_COLLECTION_BY_ID_DESC;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_COLLECTION_BY_ID_SUM;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;
import wander.wise.application.dto.collection.CollectionDto;
import wander.wise.application.dto.collection.CollectionWithoutCardsDto;
import wander.wise.application.dto.collection.CreateCollectionRequestDto;
import wander.wise.application.dto.collection.UpdateCollectionRequestDto;
import wander.wise.application.service.collection.CollectionService;

@Tag(name = "Collection management endpoints")
@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = SAVE_COLLECTION_SUM, description = SAVE_COLLECTION_DESC)
    public CollectionWithoutCardsDto save(Authentication authentication,
                                          @Valid @RequestBody CreateCollectionRequestDto requestDto) {
        return collectionService.save(authentication.getName(), requestDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = FIND_COLLECTION_BY_ID_SUM, description = FIND_COLLECTION_BY_ID_DESC)
    public CollectionDto findById(@PathVariable Long id,
                                  Authentication authentication) {
        return collectionService.findById(id, authentication.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = UPDATE_COLLECTION_BY_ID_SUM, description = UPDATE_COLLECTION_BY_ID_DESC)
    public CollectionDto updateById(@PathVariable Long id,
                                    Authentication authentication,
                                    @Valid @RequestBody UpdateCollectionRequestDto requestDto) {
        return collectionService.updateById(id, authentication.getName(), requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = DELETE_COLLECTION_BY_ID_SUM, description = DELETE_COLLECTION_BY_ID_DESC)
    public ResponseEntity<String> deleteById(@PathVariable Long id,
                                             Authentication authentication) {
        collectionService.deleteById(id, authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
