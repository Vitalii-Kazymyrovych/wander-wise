package wander.wise.application.service.comment;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wander.wise.application.dto.comment.CommentDto;
import wander.wise.application.dto.comment.CreateCommentRequestDto;
import wander.wise.application.dto.comment.ReportCommentRequestDto;
import wander.wise.application.exception.custom.AuthorizationException;
import wander.wise.application.mapper.CommentMapper;
import wander.wise.application.model.Comment;
import wander.wise.application.model.User;
import wander.wise.application.repository.comment.CommentRepository;
import wander.wise.application.service.api.email.EmailService;
import wander.wise.application.service.user.UserService;

import static wander.wise.application.constants.GlobalConstants.SEPARATOR;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EmailService emailService;
    private final UserService userService;
    @Value("${support.mail.address}")
    private String supportEmail;

    @Override
    @Transactional
    public CommentDto save(String email, CreateCommentRequestDto requestDto) {
        User user = userService.findUserEntityByEmail(email);
        if (!user.isBanned()) {
            Comment newComment = commentMapper.toModel(requestDto);
            newComment.setTimeStamp(LocalDateTime.now());
            newComment.setUser(user);
            return commentMapper.toDto(commentRepository.save(newComment));
        } else {
            throw new AuthorizationException("Access denied. User is banned.");
        }
    }

    @Override
    @Transactional
    public CommentDto update(Long id, String email, CreateCommentRequestDto requestDto) {
        Comment updatedComment = findCommentEntityById(id);
        userService.findUserAndAuthorize(updatedComment.getUser().getId(), email);
        updatedComment = commentMapper.updateCommentFromDto(updatedComment, requestDto);
        return commentMapper.toDto(commentRepository.save(updatedComment));
    }

    @Override
    @Transactional
    public void report(Long id, String email, ReportCommentRequestDto requestDto) {
        Comment reportedComment = findCommentEntityById(id);
        reportedComment.setReports(reportedComment.getReports() + 1);
        String message = new StringBuilder()
                .append("User email: ").append(email)
                .append(SEPARATOR)
                .append("Comment author: ").append(requestDto.commentAuthor())
                .append(SEPARATOR)
                .append("Comment text: ").append(requestDto.commentText())
                .append(SEPARATOR)
                .append("Report text: ").append(requestDto.reportText())
                .append(SEPARATOR)
                .append("Comment was reported: ")
                .append(reportedComment.getReports()).append(" times")
                .toString();
        emailService.sendEmail(
                supportEmail,
                "Report for comment: "
                        + reportedComment.getId(),
                message);
        commentRepository.save(reportedComment);
    }

    @Override
    @Transactional
    public void deleteById(Long id, String email) {
        Comment deletedComment = findCommentEntityById(id);
        User deletingUser = userService.findUserEntityById(id);
        if (deletingUser.getAuthorities().size() > 1
                || deletingUser.getEmail().equals(email)) {
            userService.findUserAndAuthorize(deletedComment.getUser().getId(), email);
            commentRepository.deleteById(id);
        } else {
            throw new AuthorizationException("Access denied. You can't "
                    + "delete comments of this user");
        }
    }

    private Comment findCommentEntityById(Long id) {
        Comment updatedComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find comment by id: " + id));
        return updatedComment;
    }
}
