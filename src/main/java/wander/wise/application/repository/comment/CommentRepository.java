package wander.wise.application.repository.comment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wander.wise.application.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getAllByUserEmail(String email);
}
