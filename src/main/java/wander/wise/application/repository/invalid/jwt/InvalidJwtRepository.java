package wander.wise.application.repository.invalid.jwt;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wander.wise.application.model.InvalidJwt;

public interface InvalidJwtRepository extends JpaRepository<InvalidJwt, Long> {
    boolean existsByJwt(String jwt);

    @Modifying
    @Query("delete from InvalidJwt i where i.timeStamp <= :cutoffDate")
    void deleteAllExpiredSince(@Param("cutoffDate") LocalDateTime cutoffDate);
}
