package wander.wise.application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wander.wise.application.exception.custom.AuthorizationException;
import wander.wise.application.exception.custom.JwtValidationException;
import wander.wise.application.repository.invalid.jwt.InvalidJwtRepository;
import wander.wise.application.service.invalid.jwt.InvalidJwtService;

@Component
public class JwtUtil {
    private static final Long EXPIRATION = 1000000000L;
    private final Key secret;
    @Autowired
    private InvalidJwtService invalidJwtService;

    public JwtUtil(@Value("${jwt.secret}") String secretString) {
        secret = Keys.hmacShaKeyFor((secretString).getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(EXPIRATION)))
                .signWith(secret)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            invalidJwtService.checkIfJwtInvalidated(token);
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException | AuthorizationException e) {
            throw new JwtValidationException("Expired or invalid JWT token: " + token, e);
        }
    }

    public String getUsername(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimsFromToken(
            String token,
            Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
