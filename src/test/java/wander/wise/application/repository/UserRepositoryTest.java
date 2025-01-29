package wander.wise.application.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wander.wise.application.model.User;
import wander.wise.application.repository.user.UserRepository;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {
    private User user1;
    private User user2;
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    public void setUp() {
        user1 = User.builder()
                .pseudonym("user1")
                .email("@user1")
                .password("uuuuuuuuser1")
                .firstName("user1")
                .lastName("user1")
                .profileImage("image1")
                .location("location1")
                .bio("bio1")
                .build();
        user2 = User.builder()
                .pseudonym("user2")
                .email("@user2")
                .password("uuuuuuuuser2")
                .firstName("user2")
                .lastName("user2")
                .profileImage("image2")
                .location("location2")
                .bio("bio2")
                .build();
    }

    @Test
    public void save_validData_ReturnSavedCard() {
        //when
        User saved = userRepository.save(user1);
        //then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void saveAll_validData_ReturnSavedCard() {
        //when
        List<User> saved = userRepository
                .saveAll(List.of(user1, user2));
        //then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.size()).isGreaterThan(0);
    }

    @Test
    public void findAll_validData_ReturnedSavedCards() {
        //given
        userRepository.save(user1);
        userRepository.save(user2);
        //when
        List<User> foundUsers = userRepository.findAll();
        //then
        Assertions.assertThat(foundUsers).isNotNull();
        Assertions.assertThat(foundUsers.size()).isEqualTo(2);
    }

    @Test
    public void findById_validData_ReturnedSavedCard() {
        //given
        userRepository.save(user1);
        //when
        User found = userRepository.findById(user1.getId()).get();
        //then
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void count_validData_ReturnedAmountOfSavedCards() {
        //given
        userRepository.save(user1);
        userRepository.save(user2);
        //when
        Long amount = userRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(2);
    }

    @Test
    public void deleteById_validData_DeletedCard() {
        //given
        userRepository.save(user1);
        //when
        userRepository.deleteById(user1.getId());
        Long amount = userRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(0L);
    }

    @Test
    public void existsByEmail_validData_ReturnedTrue() {
        //given
        userRepository.save(user1);
        //when
        boolean actual = userRepository.existsByEmail(user1.getEmail());
        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    public void existsByPseudonym_validData_ReturnedTrue() {
        //given
        userRepository.save(user1);
        //when
        boolean actual = userRepository.existsByPseudonym(user1.getPseudonym());
        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    public void findByEmail_validData_ReturnedSavedCard() {
        //given
        userRepository.save(user1);
        //when
        User found = userRepository.findByEmail(user1.getEmail()).get();
        //then
        Assertions.assertThat(found).isNotNull();
    }
}
