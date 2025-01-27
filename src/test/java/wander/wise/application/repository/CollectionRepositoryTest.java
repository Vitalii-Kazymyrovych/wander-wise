package wander.wise.application.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wander.wise.application.model.Card;
import wander.wise.application.model.Collection;
import wander.wise.application.model.User;
import wander.wise.application.repository.collection.CollectionRepository;
import wander.wise.application.repository.user.UserRepository;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CollectionRepositoryTest {
    private Collection collection1;
    private Collection collection2;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
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
        collection1 = Collection.builder()
                .user(user)
                .name("collection1")
                .imageLink("link1")
                .build();
        collection2 = Collection.builder()
                .user(user)
                .name("collection2")
                .imageLink("link2")
                .build();
    }

    @Test
    public void save_validData_ReturnSavedCard() {
        //when
        Collection saved = collectionRepository.save(collection1);
        //then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void saveAll_validData_ReturnSavedCard() {
        //when
        List<Collection> saved = collectionRepository
                .saveAll(List.of(collection1, collection2));
        //then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.size()).isGreaterThan(0);
    }

    @Test
    public void findAll_validData_ReturnedSavedCards() {
        //given
        collectionRepository.save(collection1);
        collectionRepository.save(collection2);
        //when
        List<Collection> foundCollections = collectionRepository.findAll();
        //then
        Assertions.assertThat(foundCollections).isNotNull();
        Assertions.assertThat(foundCollections.size()).isEqualTo(2);
    }

    @Test
    public void findById_validData_ReturnedSavedCard() {
        //given
        collectionRepository.save(collection1);
        //when
        Collection found = collectionRepository.findById(collection1.getId()).get();
        //then
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void findAllByUserEmail_validData_ReturnedSavedCard() {
        //given
        collectionRepository.save(collection1);
        collectionRepository.save(collection2);
        //when
        List<Collection> found = collectionRepository
                .findAllByUserEmail(collection1.getUser().getEmail());
        //then
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found).size().isEqualTo(2L);
    }

    @Test
    public void count_validData_ReturnedAmountOfSavedCards() {
        //given
        collectionRepository.save(collection1);
        collectionRepository.save(collection2);
        //when
        Long amount = collectionRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(2);
    }

    @Test
    public void deleteById_validData_DeletedCard() {
        //given
        collectionRepository.save(collection1);
        //when
        collectionRepository.deleteById(collection1.getId());
        Long amount = collectionRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(0L);
    }
}
