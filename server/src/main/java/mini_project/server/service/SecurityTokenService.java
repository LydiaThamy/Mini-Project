package mini_project.server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import mini_project.server.model.UserPrincipal;

@Service
public class SecurityTokenService {
    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateToken(Authentication auth) {
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Instant now = Instant.now();
        String scope = auth.getAuthorities()
                .stream()
                .map(ga -> ga.getAuthority())
                .collect(Collectors.joining(","));
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("INSERT APP NAME HERE")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(userPrincipal.getUser().getUsername())
                .claim("scope", scope)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}
