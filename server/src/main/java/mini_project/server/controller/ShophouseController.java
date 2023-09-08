package mini_project.server.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import mini_project.server.model.Search;
import mini_project.server.service.ShophouseService;

@RestController
@RequestMapping("/api/shophouse")
public class ShophouseController {

    @Autowired
    private ShophouseService service;

    @GetMapping("/categories")
    public ResponseEntity<String> getCategories() {
        return ResponseEntity.ok(service.getCategories().toString());
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<String> autocompleteKeyword(@RequestParam String keyword) {

        String json = Json.createArrayBuilder().build().toString();

        if (keyword.length() > 0)
            json = service.autocompleteKeyword(keyword).toString();

        return ResponseEntity.ok(json);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<String> getBusinessesByCategory(@PathVariable String category) {

        Optional<JsonArray> result = service.getBusinessesByCategory(category);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/businesses")
    public ResponseEntity<String> getBusinesses(@ModelAttribute Search search) {

        Optional<JsonArray> result = service.getBusinesses(search);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/business/{id}")
    public ResponseEntity<String> getBusinessById(@PathVariable String id) {

        Integer businessId;
        try {
            businessId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Business Id must be a number");
        }

        Optional<JsonObject> result = service.getBusinessById(businessId);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/business/{id}/services")
    public ResponseEntity<String> getServicesByBusinessId(@PathVariable String id) {

        Integer businessId;
        try {
            businessId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Business Id must be a number");
        }

        Optional<JsonArray> result = service.getServicesByBusinessId(businessId);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/business/{id}/reviews")
    public ResponseEntity<String> getReviewsByBusinessId(@PathVariable String id) {

        Integer businessId;
        try {
            businessId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Business Id must be a number");
        }

        Optional<JsonArray> result = service.getReviewsByBusinessId(businessId);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/geocode")
    public ResponseEntity<String> getGeocode(@RequestParam String address) {
        ResponseEntity<String> result = service.getGeocode(address);
        return ResponseEntity.status(result.getStatusCode()).body(result.getBody());
    }
}
