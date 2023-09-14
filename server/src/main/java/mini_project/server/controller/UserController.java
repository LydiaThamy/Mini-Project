package mini_project.server.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/login")
    public ResponseEntity<String> loginUser(Authentication authentication) {
        System.out.println("logged in successfully as %s".formatted(authentication.getName()));
        String jwt = tokenService.generateToken(authentication);
        return ResponseEntity.ok(
                Json.createObjectBuilder()
                        .add("userId", authentication.getName())
                        .add("token", jwt)
                        .build().toString());
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(User user) {
        Optional<JsonObject> newUser = userService.saveUser(user);
        if (newUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(newUser.get().toString());
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/password")
    public ResponseEntity<String> password(@RequestParam String pw) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return ResponseEntity.ok(
            Json.createObjectBuilder()
            .add("password", passwordEncoder.encode(pw))
            .build()
            .toString()
        );
    }

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
                User user = new User();
        user.setUserId(principal.getAttribute("id").toString());
        user.setUsername(principal.getAttribute("login"));
        user.setUsername(principal.getAttribute("email"));

        // Check if the user exists in your DB, if not, create them
        Optional<User> existingUser = userService.getUser(user.getUserId());
        if (existingUser.isEmpty()) {
            userService.saveUser(user);
        }

        // Generate a JWT for this user
        // String jwt = jwtService.generateToken(user);
//        String jwt = tokenService.generateToken(user.getUserId());
//        System.out.println("jwt token: " + jwt);

        // Redirect to the checkout page with the JWT
        return ResponseEntity.ok(
        Json.createObjectBuilder()
        .add("userId", user.getUserId())
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
