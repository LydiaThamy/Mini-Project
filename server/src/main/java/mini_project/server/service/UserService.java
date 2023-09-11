package mini_project.server.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import mini_project.server.exception.AccessTokenException;
import mini_project.server.exception.UserAccessException;
import mini_project.server.model.User;
import mini_project.server.repository.UserRepository;

@Service
public class UserService {

    @Value("${spring.security.oauth2.client.registration.github.clientId}")
    private String githubClientId;

    @Value("${spring.security.oauth2.client.registration.github.clientSecret}")
    private String githubClientSecret;

    // private static final String GITHUB_TOKEN_ENDPOINT = "https://github.com/login/oauth/access_token";

    @Autowired
    private UserRepository userRepo;

    public Optional<JsonObject> saveUser(User user) {
        // public Optional<JsonObject> saveUser(OAuth2User principal) {

        // String userId = principal.getAttribute("id");
        // String username = principal.getAttribute("login");
        // String email = principal.getAttribute("email");

        if (userRepo.saveUser(user) == 0)
            // if (userRepo.saveUser(userId, email, username) == 0)
            return Optional.empty();

        return Optional.of(
                Json.createObjectBuilder()
                        .add("userId", user.getUserId())
                        .add("username", user.getUsername())
                        .add("email", user.getEmail())
                        .build());
    }

    public Optional<User> getUser(String userId) {
        // public Optional<JsonObject> getUser(String userId) {
        return userRepo.getUser(userId);
    }

    // @Transactional(rollbackFor = { AccessTokenException.class })
    // public String getAccessToken(String code) throws IOException, AccessTokenException {
    //     RestTemplate restTemplate = new RestTemplate();

    //     // Prepare the request payload for GitHub token endpoint
    //     MultiValueMap<String, String> requestPayload = new LinkedMultiValueMap<>();
    //     requestPayload.add("client_id", githubClientId);
    //     requestPayload.add("client_secret", githubClientSecret);
    //     requestPayload.add("code", code);

    //     RequestEntity<MultiValueMap<String, String>> req = RequestEntity
    //             .post(GITHUB_TOKEN_ENDPOINT)
    //             .header("Accept", MediaType.APPLICATION_JSON_VALUE)
    //             .body(requestPayload);
    //     System.out.println("Request entity for access token call: " + req);

    //     // Exchange the code for access token
    //     ResponseEntity<String> response = restTemplate.exchange(req, String.class);

    //     if (response.getStatusCode() != HttpStatus.OK)
    //         throw new AccessTokenException(response.getBody().toString());

    //     String accessToken;
    //     try (InputStream is = new ByteArrayInputStream(response.getBody().getBytes())) {
    //         JsonReader reader = Json.createReader(is);
    //         JsonObject data = reader.readObject();
    //         accessToken = data.getString("access_token");
    //     }

    //     return accessToken;

    //     // ResponseEntity<String> response =
    //     // restTemplate.postForEntity(GITHUB_TOKEN_ENDPOINT, requestPayload,
    //     // String.class);
    //     // return extractAccessToken(response.getBody());
    // }

    // private String extractAccessToken(String responseBody) throws IOException {
    // ObjectMapper mapper = new ObjectMapper();
    // JsonNode rootNode = mapper.readTree(responseBody);
    // return rootNode.path("access_token").asText();
    // }

    @Transactional(rollbackFor = { UserAccessException.class })
    public User getUserFromGithub(String userId) throws IOException, UserAccessException {
    // public User getUserFromGithub(String accessToken) throws IOException, UserAccessException {
        RestTemplate restTemplate = new RestTemplate();

        // Fetch the user's GitHub data using the access token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, entity,
                String.class);

        if (response.getStatusCode() != HttpStatus.OK)
            throw new UserAccessException(response.getBody().toString());

        User user = new User();

        try (InputStream is = new ByteArrayInputStream(response.getBody().getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
            user.setUsername(data.getString("login"));
            user.setUserId(data.getString("id"));
            user.setEmail(data.getString("email"));
        }

        // Check if the user exists in your DB, if not, create them
        Optional<User> existingUser = getUser(user.getUserId());
        if (existingUser.isEmpty()) {
            saveUser(user);
        }

        return user;

        // parse the user data
        // String githubId = parseGithubId(userResponse.getBody());
        // String githubName = parseGithubName(userResponse.getBody());
    }

    // private String parseGithubId(String userJson) throws IOException {
    // ObjectMapper mapper = new ObjectMapper();
    // JsonNode rootNode = mapper.readTree(userJson);
    // return rootNode.path("id").asText();
    // }

    // private String parseGithubName(String userJson) throws IOException {
    // ObjectMapper mapper = new ObjectMapper();
    // JsonNode rootNode = mapper.readTree(userJson);
    // return rootNode.path("login").asText(); // or rootNode.path("name").asText()
    // if you want the full name
    // }
}
