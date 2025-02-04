package wander.wise.application.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import wander.wise.application.dto.comment.CommentDto;
import wander.wise.application.dto.comment.CreateCommentRequestDto;
import wander.wise.application.dto.comment.ReportCommentRequestDto;
import wander.wise.application.mapper.CommentMapper;
import wander.wise.application.model.Comment;
import wander.wise.application.model.User;
import wander.wise.application.repository.comment.CommentRepository;
import wander.wise.application.service.api.email.EmailService;
import wander.wise.application.service.comment.CommentServiceImpl;
import wander.wise.application.service.user.UserService;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    private String email;
    private Long id;
    private CreateCommentRequestDto createCommentRequestDto;
    private CommentDto commentDto;
    private Comment comment;
    private User user;
    private ReportCommentRequestDto reportCommentRequestDto;
    private Collection collection;
    @Value("${support.mail.address}")
    private String supportEmail;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void setUp() {
        email = "email";
        id = 1L;
        createCommentRequestDto = mock(CreateCommentRequestDto.class);
        commentDto = mock(CommentDto.class);
        user = mock(User.class);
        comment = mock(Comment.class);
        reportCommentRequestDto = mock(ReportCommentRequestDto.class);
        collection = mock(Collection.class);
    }

    @Test
    public void save_ValidData_ReturnsCommentDto() {
        when(userService.findUserEntityByEmail(anyString())).thenReturn(user);
        when(commentMapper.toModel(any(CreateCommentRequestDto.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        CommentDto actual = commentService.save(email, createCommentRequestDto);

        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    public void update_ValidData_ReturnsCommentDto() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(comment.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(id);
        when(userService.findUserAndAuthorize(anyLong(), anyString())).thenReturn(user);
        when(commentMapper.updateCommentFromDto(
                any(Comment.class),
                any(CreateCommentRequestDto.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        CommentDto actual = commentService.update(id, email, createCommentRequestDto);

        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    public void report_ValidData_ReturnsNothing() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(comment.getReports()).thenReturn(id);
        when(comment.getId()).thenReturn(id);
        when(reportCommentRequestDto.commentAuthor()).thenReturn(email);
        when(reportCommentRequestDto.commentText()).thenReturn(email);
        when(reportCommentRequestDto.reportText()).thenReturn(email);
        doNothing().when(emailService).sendEmail(eq(supportEmail), anyString(), anyString());
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertAll(() -> commentService.report(id, email, reportCommentRequestDto));
    }

    @Test
    public void deleteById_ValidData_ReturnsNothing() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(comment.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(id);
        when(userService.findUserAndAuthorize(anyLong(), anyString())).thenReturn(user);
        when(user.getAuthorities()).thenReturn(collection);
        when(collection.size()).thenReturn(2);
        doNothing().when(commentRepository).deleteById(anyLong());

        assertAll(() -> commentService.deleteById(id, email));
    }
}
