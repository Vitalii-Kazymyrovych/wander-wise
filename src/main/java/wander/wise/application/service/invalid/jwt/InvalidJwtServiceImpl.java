package wander.wise.application.service.invalid.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wander.wise.application.exception.custom.AuthorizationException;
import wander.wise.application.model.InvalidJwt;
import wander.wise.application.repository.invalid.jwt.InvalidJwtRepository;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvalidJwtServiceImpl implements InvalidJwtService {
    public static final int TOKEN_EXPIRATION_PERIOD = 12;
    private final InvalidJwtRepository invalidJwtRepository;

    @Override
    public void invalidateJwt(String jwt) {
        InvalidJwt invalidJwt = new InvalidJwt(jwt, LocalDateTime.now());
        invalidJwtRepository.save(invalidJwt);
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void performTokenCleanup() {
        LocalDateTime cutoffDate = LocalDateTime.now()
                .minusDays(TOKEN_EXPIRATION_PERIOD);
        invalidJwtRepository.deleteAllExpiredSince(cutoffDate);
    }

    @Override
    public void checkIfJwtInvalidated(String jwt) {
        if (invalidJwtRepository.existsByJwt(jwt)) {
            throw new AuthorizationException(
                    "Token was invalidated, as user logged out.");
        }
    }
}
