package mini_project.server.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import mini_project.server.exception.AccessTokenException;
import mini_project.server.exception.UserAccessException;
import mini_project.server.model.User;
import mini_project.server.service.TokenService;
import mini_project.server.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Value("${base.url}")
    private String baseUrl;

    @GetMapping("/authorise")
    public ResponseEntity<String> authoriseUser(@AuthenticationPrincipal OAuth2User principal)
            // public ResponseEntity<String> authenticateUser(@RequestParam String code)
            throws IOException, AccessTokenException, UserAccessException {

        if (principal == null) {
            System.out.println("is null".formatted(baseUrl));
            return ResponseEntity.badRequest().build();
        }

        // extract access token from GitHub
        // String accessToken = userService.getAccessToken(code);
        // System.out.println("access token: " + accessToken);

        // Fetch the user's GitHub data using the access token
        // User user = userService.getUserFromGithub(accessToken);
        // System.out.println("User: " + user);
        User user = new User(principal.getAttribute("id").toString(), principal.getAttribute("login"),
                principal.getAttribute("email"));

        // Check if the user exists in your DB, if not, create them
        Optional<User> existingUser = userService.getUser(user.getUserId());
        if (existingUser.isEmpty()) {
            userService.saveUser(user);
        }

        // Generate a JWT for this user
        // String jwt = jwtService.generateToken(user);
        String jwt = tokenService.generateToken(user.getUserId());
        System.out.println("jwt token: " + jwt);

        // Redirect to the checkout page with the JWT
        return ResponseEntity.ok(
        Json.createObjectBuilder()
        .add("userId", user.getUserId())
        .add("token", jwt)
        .build().toString());
        // return ResponseEntity.status(HttpStatus.FOUND)
        //         .location(URI.create("%s/authorise".formatted(baseUrl)))
        //         // .location(URI.create("http://localhost:4200/checkout?token=" + jwt))
        //         .body(Json.createObjectBuilder()
        //                 .add("token", jwt)
        //                 .add("userId", user.getUserId())
        //                 .build().toString());
    }

    @GetMapping("/{token}")
    public ResponseEntity<String> getUser(@PathVariable String token) {
        // public ResponseEntity<String> getUser(@AuthenticationPrincipal OAuth2User
        // principal) {

        System.out.println("Token: " + token);
        if (token.equals("null")) {
            System.out.println("token is null... sending back to client...");
            ResponseEntity.badRequest().body("token invalid");
        }

        Optional<String> userId = tokenService.getUserId(token);

        if (userId.isEmpty())
            return ResponseEntity.badRequest().body("user not found");

        Optional<User> user = userService.getUser(userId.get());

        if (user.isEmpty())
            return ResponseEntity.notFound().build();

        JsonObject json = Json.createObjectBuilder()
                .add("userId", user.get().getUserId())
                .add("username", user.get().getUsername())
                .add("email", user.get().getEmail())
                .build();

        System.out.println(json.toString());

        return ResponseEntity.ok(json.toString());
    }
}
