package mini_project.server.configuration;

import java.util.List;

import mini_project.server.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private AuthService authService;

    @Value("${secret.key}")
    private String secretKeyString;

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withSecretKey(new SecretKeySpec(secretKeyString.getBytes(), "HmacSHA256"))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*", "http://localhost:4200", "https://sg.shop-house.club"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/checkout/**").authenticated()
                        .requestMatchers("/payment/**").authenticated()
                        .requestMatchers("/confirmation/**").authenticated()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                 .userDetailsService(authService)
                // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                 .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // .oauth2Login(oauth2 -> oauth2
                //         .failureUrl("%s/login".formatted(baseUrl))
                //         .defaultSuccessUrl("%s/authorise".formatted(baseUrl), true))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();

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
    }
}
