package wander.wise.application.repository.card.spec;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import wander.wise.application.model.Card;
import wander.wise.application.repository.SpecificationProvider;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Card> {
    private static final String AI_SEARCH_KEY = "AI";
    private static final String KEY = "author";
    private static final String USER_SEARCH_KEY = "User";
    public static final int AUTHOR_NAME_INDEX = 0;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Card> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params[AUTHOR_NAME_INDEX].equals(AI_SEARCH_KEY)) {
                predicates.add(criteriaBuilder.equal(root.get(KEY), AI_SEARCH_KEY));
            }
            if (params[AUTHOR_NAME_INDEX].equals(USER_SEARCH_KEY)) {
                predicates.add(criteriaBuilder.notEqual(root.get(KEY), AI_SEARCH_KEY));
            }
            if (!params[AUTHOR_NAME_INDEX].equals(AI_SEARCH_KEY)
                    && !params[AUTHOR_NAME_INDEX].equals(USER_SEARCH_KEY)) {
                predicates.add(criteriaBuilder.equal(root.get(KEY), params[AUTHOR_NAME_INDEX]));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
