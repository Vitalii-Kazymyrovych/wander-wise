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
import wander.wise.application.repository.SpecificationProvider;

import java.util.List;


@DataJpaTest
@ComponentScan(basePackages = "wander.wise.application.repository.card")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CardSpecificationProviderManagerTest {
    @Autowired
    private CardSpecificationProviderManager specificationProviderManager;

    @Test
    public void build_ValidData_ReturnCardSpecification() {
        String key = "tripTypes";

        SpecificationProvider<Card> spec = specificationProviderManager
                .getSpecificationProvider(key);

        Assertions.assertThat(spec).isNotNull();
    }
}