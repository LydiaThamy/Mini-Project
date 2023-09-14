package mini_project.server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import mini_project.server.model.User;
import mini_project.server.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Value("${secret.key}")
    private String secretKeyString;

    public String generateToken(Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        Instant now = Instant.now();

        String token = JWT.create()
                .withIssuer("shophouse")
                .withSubject(user.getUserId())
                .withClaim("name", user.getUsername())
                .withClaim("scope", "ROLE_USER")
                .withIssuedAt(now)
                .withExpiresAt(now.plus(24, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256(secretKeyString));
        return token;
    }

    public Optional<String> getUserId(String token) {
        try {
            return Optional.of(
                    jwtDecoder.decode(token).getClaim("sub"));
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}
