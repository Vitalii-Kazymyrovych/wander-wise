package wander.wise.application.repository.collection;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wander.wise.application.model.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findAllByUserEmail(String email);
}
