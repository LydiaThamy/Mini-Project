package mini_project.server.configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // @Value("${rsa.public.key}")
    // private RSAPublicKey rsaPublicKey;

    // @Value("${rsa.private.key}")
    // private RSAPrivateKey rsaPrivateKey;

    @Value("${base.url}")
    private String baseUrl;

    @Value("${rsa.public.key}")
    private String publicKeyString;

    @Value("${rsa.private.key}")
    private String privateKeyString;

    private RSAPublicKey rsaPublicKey;
    private RSAPrivateKey rsaPrivateKey;

    @PostConstruct
    public void init() {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            // Decoder decoder = Base64.getUrlDecoder();
            Decoder decoder = Base64.getDecoder();

            // decode public key
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(
                    decoder.decode(publicKeyString.trim()));
            // decoder.decode(publicKeyString.getBytes()));
            rsaPublicKey = (RSAPublicKey) kf.generatePublic(X509publicKey);

            // decode the base64 private key
            PKCS8EncodedKeySpec PKCS8privateKey = new PKCS8EncodedKeySpec(
                    decoder.decode(privateKeyString.trim()));
            // decoder.decode(privateKeyString.getBytes()));

            rsaPrivateKey = (RSAPrivateKey) kf.generatePrivate(PKCS8privateKey);

        } catch (Exception e) {
            throw new RuntimeException(e); // or some other exception handling
        }
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "https://sg.shop-house.club"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // RequestMatcher checkoutMatcher = new AntPathRequestMatcher("/checkout/**");
        
        // http
        //         .csrf(csrf -> csrf.disable())
        //         .cors(withDefaults())
        //         // .requiresChannel()
        //         // .anyRequest().requiresSecure()
        //         // .and()
        //         .authorizeHttpRequests((authz) -> authz
        //                 .requestMatchers(checkoutMatcher).authenticated()
        //                 .anyRequest().permitAll())
        //         .oauth2Login(oauth2 -> oauth2
        //                 .failureUrl("%s/#/login".formatted(baseUrl))
        //                 .defaultSuccessUrl("%s/#/authorise".formatted(baseUrl), true))
        //         // .sessionManagement().sessionFixation().none()
        //         ;

        // return http.build();

        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/checkout/**").authenticated()
                        .requestMatchers("/payment/**").authenticated()
                        .requestMatchers("/confirmation/**").authenticated()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                // .userDetailsService(authService)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // .oauth2Login(oauth2 -> oauth2
                //         .failureUrl("%s/#/login".formatted(baseUrl))
                //         .defaultSuccessUrl("%s/#/authorise".formatted(baseUrl), true))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
