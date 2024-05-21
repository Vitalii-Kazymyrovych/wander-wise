package wander.wise.application.service.comment;

import wander.wise.application.dto.comment.CommentDto;
import wander.wise.application.dto.comment.CreateCommentRequestDto;
import wander.wise.application.dto.comment.ReportCommentRequestDto;

public interface CommentService {
    CommentDto save(String email, CreateCommentRequestDto requestDto);

    CommentDto update(Long id, String email, CreateCommentRequestDto requestDto);

    void report(Long id, String email, ReportCommentRequestDto requestDto);

    void deleteById(Long id, String email);
}
