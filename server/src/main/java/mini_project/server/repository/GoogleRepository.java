package mini_project.server.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class GoogleRepository {

    @Value("${google.geocoding.api.key}")
    private String apiKey;

    public ResponseEntity<String> getGeocode(String address) {
        String url = UriComponentsBuilder
            .fromUriString("https://maps.googleapis.com/maps/api/geocode/json")
            .queryParam("address", address.toLowerCase())
            .queryParam("key", apiKey)
            .toUriString();
        System.out.println(url);

        RestTemplate template = new RestTemplate();
        System.out.println(template.getForEntity(url, String.class));
        return template.getForEntity(url, String.class);
    }    
}

