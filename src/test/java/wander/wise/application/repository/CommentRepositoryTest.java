package wander.wise.application.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wander.wise.application.model.Card;
import wander.wise.application.model.Comment;
import wander.wise.application.model.User;
import wander.wise.application.repository.card.CardRepository;
import wander.wise.application.repository.comment.CommentRepository;
import wander.wise.application.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentRepositoryTest {
    private Comment comment1;
    private Comment comment2;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    
    @BeforeEach
    public void setUp() {
        Card card = Card.builder()
                .fullName("fullname1")
                .author("AI1")
                .tripTypes("types1")
                .climate("climate1")
                .specialRequirements("recs1")
                .description("desc1")
                .whyThisPlace("why1")
                .imageLinks("links1")
                .mapLink("link1")
                .latitude(101.3939)
                .longitude(101.2424)
                .likes(0L)
                .reports(0L)
                .build();
        cardRepository.save(card);
        User user = User.builder()
                .pseudonym("user")
                .email("@user")
                .password("uuuuuuuuser")
                .firstName("user")
                .lastName("user")
                .profileImage("image")
                .location("location")
                .bio("bio")
                .build();
        userRepository.save(user);
        comment1 = Comment.builder()
                .user(user)
                .card(card)
                .timeStamp(LocalDateTime.now())
                .text("text1")
                .stars(5)
                .reports(0L)
                .build();
        comment2 = Comment.builder()
                .user(user)
                .card(card)
                .timeStamp(LocalDateTime.now())
                .text("text2")
                .stars(5)
                .reports(0L)
                .build();
    }

    @Test
    public void save_validData_ReturnSavedCard() {
        //when
        Comment saved = commentRepository.save(comment1);
        //then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void saveAll_validData_ReturnSavedCard() {
        //when
        List<Comment> saved = commentRepository
                .saveAll(List.of(comment1, comment2));
        //then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.size()).isGreaterThan(0);
    }

    @Test
    public void findAll_validData_ReturnedSavedCards() {
        //given
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        //when
        List<Comment> foundComments = commentRepository.findAll();
        //then
        Assertions.assertThat(foundComments).isNotNull();
        Assertions.assertThat(foundComments.size()).isEqualTo(2);
    }

    @Test
    public void findById_validData_ReturnedSavedCard() {
        //given
        commentRepository.save(comment1);
        //when
        Comment found = commentRepository.findById(comment1.getId()).get();
        //then
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void findAllByUserEmail_validData_ReturnedSavedCard() {
        //given
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        //when
        List<Comment> found = commentRepository
                .getAllByUserEmail(comment1.getUser().getEmail());
        //then
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found).size().isEqualTo(2L);
    }

    @Test
    public void count_validData_ReturnedAmountOfSavedCards() {
        //given
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        //when
        Long amount = commentRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(2);
    }

    @Test
    public void deleteById_validData_DeletedCard() {
        //given
        commentRepository.save(comment1);
        //when
        commentRepository.deleteById(comment1.getId());
        Long amount = commentRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(0L);
    }
}
