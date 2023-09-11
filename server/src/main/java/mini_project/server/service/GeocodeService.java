package mini_project.server.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodeService {

    @Value("${google.geocode.key}")
    private String apiKey;

    public String getGeocode(String address) throws IOException, InterruptedException {
        String url = UriComponentsBuilder
                .fromUriString("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("address", address.toLowerCase())
                .queryParam("key", apiKey)
                .toUriString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();

        return client.send(request, BodyHandlers.ofString()).body();
    }
}
