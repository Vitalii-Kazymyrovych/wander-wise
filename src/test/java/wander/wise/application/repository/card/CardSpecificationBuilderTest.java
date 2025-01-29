package wander.wise.application.repository.card;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specification;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.model.Card;

import java.util.List;


@DataJpaTest
@ComponentScan(basePackages = "wander.wise.application.repository.card")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CardSpecificationBuilderTest {
    @Autowired
    private CardSpecificationBuilder specificationBuilder;

    @Test
    public void build_ValidData_ReturnCardSpecification() {
        CardSearchParameters params = new CardSearchParameters(
                "start",
                List.of("type1", "type2"),
                new String[]{"climate1", "climate2"},
                new String[]{"req1", "req2"},
                new String[]{"distance"},
                new String[]{"author"});

        Specification<Card> spec = specificationBuilder.build(params);

        Assertions.assertThat(spec).isNotNull();
    }
}