package wander.wise.application.repository.card;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wander.wise.application.model.Card;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CardRepositoryTest {
    private Card card1;
    private Card card2;
    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    public void setUp() {
        card1 = Card.builder()
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
        card2 = Card.builder()
                .fullName("fullname2")
                .author("AI12")
                .tripTypes("types12")
                .climate("climate12")
                .specialRequirements("recs12")
                .description("desc12")
                .whyThisPlace("why12")
                .imageLinks("links12")
                .mapLink("link12")
                .latitude(101.3939)
                .longitude(101.2424)
                .likes(0L)
                .reports(0L)
                .build();
    }

    @Test
    public void save_validData_ReturnSavedCard() {
        //when
        Card saved = cardRepository.save(card1);
        //then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void findAll_validData_ReturnedSavedCards() {
        //given
        cardRepository.save(card1);
        cardRepository.save(card2);
        //when
        List<Card> foundCards = cardRepository.findAll();
        //then
        Assertions.assertThat(foundCards).isNotNull();
        Assertions.assertThat(foundCards.size()).isEqualTo(2);
    }

    @Test
    public void findById_validData_ReturnedSavedCard() {
        //given
        cardRepository.save(card1);
        //when
        Card foundCard = cardRepository.findById(card1.getId()).get();
        //then
        Assertions.assertThat(foundCard).isNotNull();
    }

    @Test
    public void count_validData_ReturnedAmountOfSavedCards() {
        //given
        cardRepository.save(card1);
        cardRepository.save(card2);
        //when
        Long amount = cardRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(2);
    }

    @Test
    public void existsByFullName_validData_ReturnedTrue() {
        //given
        cardRepository.save(card1);
        //when
        boolean actual = cardRepository.existsByFullName(card1.getFullName());
        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    public void deleteById_validData_DeletedCard() {
        //given
        cardRepository.save(card1);
        //when
        cardRepository.deleteById(card1.getId());
        Long amount = cardRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(0L);
    }
}
