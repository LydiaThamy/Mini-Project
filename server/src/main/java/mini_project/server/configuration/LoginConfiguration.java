package mini_project.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class LoginConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("http://localhost:4200/#/checkout", true)
                // .defaultFailureURL("http://localhost:4200/#/login-failure")
                    //         .redirectionEndpoint(redirection -> redirection
                    //                 .baseUri("/login/oauth2/callback/*"))

                    // Customizer.withDefaults()
                );

        return http.build();
    }
}
