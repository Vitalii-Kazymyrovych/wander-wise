package wander.wise.application.repository.card;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wander.wise.application.model.Card;
import wander.wise.application.repository.SpecificationProvider;
import wander.wise.application.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class CardSpecificationProviderManager implements SpecificationProviderManager<Card> {
    private final List<SpecificationProvider<Card>> cardSpecificationProviders;

    @Override
    public SpecificationProvider<Card> getSpecificationProvider(String key) {
        return cardSpecificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Can't find correct specification provided by key: "
                                + key));
    }
}
