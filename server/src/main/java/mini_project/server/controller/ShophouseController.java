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

        if (keyword.length() < 1)
            return ResponseEntity.ok().build();

        Optional<JsonArray> result = service.autocompleteKeyword(keyword);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/businesses")
    public ResponseEntity<String> getBusinesses(@RequestParam String keyword) {

        Optional<JsonArray> result = service.getBusinesses(keyword);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/category")
    public ResponseEntity<String> getBusinessesByCategory(@RequestParam String c) {

        Optional<JsonArray> result = service.getBusinessesByCategory(c);
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
