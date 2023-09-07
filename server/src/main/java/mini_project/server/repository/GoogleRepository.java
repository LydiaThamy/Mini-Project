package mini_project.server.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
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
            .queryParam("address", address)
            .queryParam("key", apiKey)
            .toUriString();

        RequestEntity req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();

        return template.exchange(req, String.class);
    }    
}