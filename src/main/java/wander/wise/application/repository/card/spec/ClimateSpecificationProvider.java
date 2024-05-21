package wander.wise.application.repository.card.spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import wander.wise.application.model.Card;
import wander.wise.application.repository.SpecificationProvider;

@Component
public class ClimateSpecificationProvider implements SpecificationProvider<Card> {
    private static final String KEY = "climate";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Card> getSpecification(String[] params) {
        return (((root, query, criteriaBuilder) ->
                root.get(KEY).in((Object[]) params)));
    }
}
