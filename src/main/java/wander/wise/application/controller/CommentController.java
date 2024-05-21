package wander.wise.application.controller;

import static wander.wise.application.constants.SwaggerConstants.DELETE_COMMENT_DESC;
import static wander.wise.application.constants.SwaggerConstants.DELETE_COMMENT_SUM;
import static wander.wise.application.constants.SwaggerConstants.REPORT_COMMENT_DESC;
import static wander.wise.application.constants.SwaggerConstants.REPORT_COMMENT_SUM;
import static wander.wise.application.constants.SwaggerConstants.SAVE_COMMENT_DESC;
import static wander.wise.application.constants.SwaggerConstants.SAVE_COMMENT_SUM;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_COMMENT_DESC;
import static wander.wise.application.constants.SwaggerConstants.UPDATE_COMMENT_SUM;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import wander.wise.application.dto.comment.CommentDto;
import wander.wise.application.dto.comment.CreateCommentRequestDto;
import wander.wise.application.dto.comment.ReportCommentRequestDto;
import wander.wise.application.service.comment.CommentService;

@Tag(name = "Comment management endpoints")
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private static final String REPORT_EMAIL = "budzetbudzet4@gmail.com";
    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = SAVE_COMMENT_SUM, description = SAVE_COMMENT_DESC)
    public CommentDto save(Authentication authentication,
                           @Valid @RequestBody CreateCommentRequestDto requestDto) {
        return commentService.save(authentication.getName(), requestDto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = UPDATE_COMMENT_SUM, description = UPDATE_COMMENT_DESC)
    public CommentDto update(@PathVariable Long id, Authentication authentication,
                             @Valid @RequestBody CreateCommentRequestDto requestDto) {
        return commentService.update(id, authentication.getName(), requestDto);
    }

    @PutMapping("/report/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = REPORT_COMMENT_SUM, description = REPORT_COMMENT_DESC)
    public ResponseEntity<String> report(@PathVariable Long id,
                                         Authentication authentication,
                                         @Valid @RequestBody ReportCommentRequestDto requestDto) {
        commentService.report(id, authentication.getName(), requestDto);
        return new ResponseEntity<>(
                "Report message was sent to email: "
                        + REPORT_EMAIL,
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = DELETE_COMMENT_SUM, description = DELETE_COMMENT_DESC)
    public ResponseEntity<String> deleteComment(@PathVariable Long id,
                                                Authentication authentication) {
        commentService.deleteById(id, authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
