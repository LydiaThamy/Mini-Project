package mini_project.server.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/businesses")
    public ResponseEntity<String> getAllBusinesses() {
        return ResponseEntity.ok(service.getAllBusinesses().toString());
    }

    @GetMapping("/businesses/category")
    public ResponseEntity<String> getBusinessesByCategory(@RequestParam String category) {

        Optional<JsonArray> result = service.getBusinessesByCategory(category);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/businesses/keyword")
    public ResponseEntity<String> getBusinesses(@ModelAttribute Search search) {
        System.out.println("Search: " + search.toString());

        Optional<JsonArray> result = service.getBusinessesByKeyword(search);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<String> getBusinessById(@PathVariable String businessId) {

        Integer bId;
        try {
            bId = Integer.parseInt(businessId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Business Id must be a number");
        }

        Optional<JsonObject> result = service.getBusinessById(bId);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/business/{businessId}/services")
    public ResponseEntity<String> getServicesByBusinessId(@PathVariable String businessId) {

        Integer bId;
        try {
            bId = Integer.parseInt(businessId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Business Id must be a number");
        }

        Optional<JsonArray> result = service.getServicesByBusinessId(bId);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/business/{businessId}/reviews")
    public ResponseEntity<String> getReviewsByBusinessId(@PathVariable String businessId) {

        Integer bId;
        try {
            bId = Integer.parseInt(businessId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Business Id must be a number");
        }

        Optional<JsonArray> result = service.getReviewsByBusinessId(bId);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/geocode")
    public ResponseEntity<String> getGeocode(@RequestParam String address) {
        String result;
        try {
            result = service.getGeocode(address);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cart/{customerId}")
    public ResponseEntity<String> getCartByCustomerId(@PathVariable String customerId) {
        Optional<JsonArray> result = service.getCart(customerId);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result.get().toString());
    }

    @PostMapping("/add-to-cart/{customerId}")
    public ResponseEntity<String> updateCart(@PathVariable String customerId, @RequestBody Map<String, String> payload) {
        service.addToCart(customerId, payload.get("serviceId"));
        return ResponseEntity.ok().build();
    }
}
