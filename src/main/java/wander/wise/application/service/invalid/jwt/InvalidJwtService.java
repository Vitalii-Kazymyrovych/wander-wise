package wander.wise.application.service.invalid.jwt;

public interface InvalidJwtService {
    void invalidateJwt(String jwt);

    void checkIfJwtInvalidated(String jwt);

    void performTokenCleanup();
}
