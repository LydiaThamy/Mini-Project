// package mini_project.server.repository;

// import java.io.IOException;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse.BodyHandlers;

// import org.springframework.beans.factory.annotation.Value;
// // import org.springframework.http.RequestEntity;
// import org.springframework.stereotype.Repository;
// import org.springframework.web.util.UriComponentsBuilder;

// @Repository
// public class GeoapifyRepository {

//     @Value("${geoapify.geocode.key}")
//     private String apiKey;

//     public String getGeocode(String address) throws IOException, InterruptedException {
//         // public ResponseEntity<String> getGeocode(String address) {
//         // RestTemplate template = new RestTemplate();
//         String url = UriComponentsBuilder
//         .fromUriString("https://api.geoapify.com/v1/geocode/search")
//         .queryParam("text", address.trim().toLowerCase())
//         .queryParam("apiKey", apiKey)
//         .toUriString();

//         // RequestEntity req =
//         // RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
//         // // return template.getForEntity(url, String.class).getBody();
//         // return template.exchange(req, String.class).getBody();

//         HttpClient client = HttpClient.newHttpClient();
//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(url))
//                 .header("Content-Type", "application/json")
//                 .build();

//         return client.send(request, BodyHandlers.ofString()).body();
//     }
// }
