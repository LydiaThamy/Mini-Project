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
import mini_project.server.service.BusinessService;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Autowired
    private BusinessService service;

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

    @GetMapping("/")
    public ResponseEntity<String> getAllBusinesses() {
        return ResponseEntity.ok(service.getAllBusinesses().toString());
    }

    @GetMapping("/category")
    public ResponseEntity<String> getBusinessesByCategory(@RequestParam String category) {

        Optional<JsonArray> result = service.getBusinessesByCategory(category);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/keyword")
    public ResponseEntity<String> getBusinesses(@ModelAttribute Search search) {
        System.out.println("Search:" + search.toString());

        Optional<JsonArray> result = service.getBusinessesByKeyword(search);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<String> getBusinessByBusinessId(@PathVariable String businessId) {

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

    @GetMapping("/{businessId}/services")
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

    @GetMapping("/{businessId}/reviews")
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

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<String> getBusinessByServiceId(@PathVariable String serviceId) {

        Optional<JsonObject> json = service.getBusinessByServiceId(serviceId);

        if (json.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(json.get().toString());
    }

}
