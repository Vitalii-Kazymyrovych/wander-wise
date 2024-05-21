package wander.wise.application.repository.card;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.model.Card;
import wander.wise.application.repository.SpecificationBuilder;
import wander.wise.application.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class CardSpecificationBuilder implements SpecificationBuilder<Card, CardSearchParameters> {
    private final SpecificationProviderManager<Card> cardSpecificationProviderManager;

    @Override
    public Specification<Card> build(CardSearchParameters searchParameters) {
        Specification<Card> spec = Specification.where(null);
        if (notEmpty(searchParameters.tripTypes())) {
            spec = spec.and(cardSpecificationProviderManager
                    .getSpecificationProvider("tripTypes")
                    .getSpecification(searchParameters.tripTypes()));
        }
        if (notEmpty(searchParameters.climate())) {
            spec = spec.and(cardSpecificationProviderManager
                    .getSpecificationProvider("climate")
                    .getSpecification(searchParameters.climate()));
        }
        if (notEmpty(searchParameters.specialRequirements())) {
            spec = spec.and(cardSpecificationProviderManager
                    .getSpecificationProvider("specialRequirements")
                    .getSpecification(searchParameters.specialRequirements()));
        }
        if (notEmpty(searchParameters.travelDistance())) {
            spec = spec.and(cardSpecificationProviderManager
                    .getSpecificationProvider("fullName")
                    .getSpecification(searchParameters.travelDistance()));
        }
        if (notEmpty(searchParameters.author())) {
            spec = spec.and(cardSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParameters.author()));
        }
        return spec;
    }

    private static boolean notEmpty(String[] searchParameters) {
        return searchParameters != null
                && searchParameters.length > 0;
    }
}
