package mini_project.server.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import mini_project.server.exception.AccessTokenException;
import mini_project.server.exception.UserAccessException;
import mini_project.server.model.User;
import mini_project.server.service.SecurityTokenService;
import mini_project.server.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
  @Autowired
    private UserService userService;

    @Autowired
    private SecurityTokenService securityTokenService;

    @GetMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestParam String code)
            throws IOException, AccessTokenException, UserAccessException {

        // extract access token from GitHub
        String accessToken = userService.getAccessToken(code);

        // Fetch the user's GitHub data using the access token
        User user = userService.getUserFromGithub(accessToken);

        // Generate a JWT for this user
        // String jwt = jwtService.generateToken(user);
        String jwt = securityTokenService.generateToken(user.getUserId());

        // Redirect to the checkout page with the JWT
        return ResponseEntity.ok(
                Json.createObjectBuilder()
                .add("userId", user.getUserId())
                        .add("token", jwt)
                        .build().toString());
        // return ResponseEntity.status(HttpStatus.FOUND)
        // .location(URI.create("http://localhost:4200/#/checkout"))
        // // .location(URI.create("http://localhost:4200/checkout?token=" + jwt))
        // .body(Json.createObjectBuilder()
        // .add("token", jwt)
        // .build().toString());
        // .build();
    }

    @GetMapping("/")
    public ResponseEntity<String> getUser(@AuthenticationPrincipal OAuth2User principal) {
        String userId = principal.getAttribute("id");
        Optional<User> result = userService.getUser(userId);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        JsonObject json = Json.createObjectBuilder()
                .add("userId", userId)
                .add("username", result.get().getUsername())
                .add("email", result.get().getEmail())
                .build();

        return ResponseEntity.ok(json.toString());
    }
}
