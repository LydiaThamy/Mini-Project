package mini_project.server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

// import mini_project.server.model.UserPrincipal;

@Service
public class SecurityTokenService {
    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateToken(String userId) {
    // public String generateToken(OAuth2User principal) {
    // public String generateToken(Authentication auth) {
        // UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Instant now = Instant.now();
        // String scope = principal.getAuthorities()
        //         .stream()
        //         .map(ga -> ga.getAuthority())
        //         .collect(Collectors.joining(","));
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("shophouse")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(userId)
                // .subject(principal.getAttribute("username"))
                // .subject(userPrincipal.getUser().getUsername())
                // .claim("scope", scope)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}
