package wander.wise.application.repository.user.pseudonym;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Random;

@Repository
@RequiredArgsConstructor
public class PseudonymRepositoryImpl implements PseudonymRepository {
    private static final String GET_ADJECTIVE = "SELECT name FROM username_adjectives where id = ";
    private static final String GET_COLOR = "SELECT name FROM username_colors where id = ";
    private static final String GET_ANIMAL = "SELECT name FROM username_animals where id = ";
    private static final Random RANDOM = new Random();
    private final EntityManager entityManager;

    @Override
    public String getAdjective() {
        return (String) entityManager
                .createNativeQuery(
                        GET_ADJECTIVE
                                + RANDOM.nextInt(1, 100),
                        String.class)
                .getSingleResult();
    }

    @Override
    public String getColor() {
        return (String) entityManager
                .createNativeQuery(
                        GET_COLOR
                                + RANDOM.nextInt(1, 100),
                        String.class)
                .getSingleResult();
    }

    @Override
    public String getAnimal() {
        return (String) entityManager
                .createNativeQuery(
                        GET_ANIMAL
                                + RANDOM.nextInt(1, 100),
                        String.class)
                .getSingleResult();
    }
}
