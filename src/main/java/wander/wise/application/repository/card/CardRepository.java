package wander.wise.application.repository.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import wander.wise.application.model.Card;

public interface CardRepository extends
        JpaRepository<Card, Long>,
        JpaSpecificationExecutor<Card> {
    boolean existsByFullName(String name);
}
