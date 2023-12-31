package mini_project.server.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import mini_project.server.model.Business;
import mini_project.server.model.Review;
import mini_project.server.model.Search;
import mini_project.server.repository.BusinessRepository;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository bizRepo;

    public JsonArray autocompleteKeyword(String keyword) {

        List<String> autocomplete = bizRepo.autocompleteKeyword(keyword);

        JsonArrayBuilder json = Json.createArrayBuilder();
        for (String c : autocomplete)
            json.add(c);

        return json.build();
    }

    public JsonArray getCategories() {

        List<String> categories = bizRepo.getCategories();
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (String c : categories)
            json.add(c);

        return json.build();
    }

    public JsonArray getAllBusinesses() {

        List<Business> businesses = bizRepo.getAllBusinesses();

        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Business b : businesses)
            json.add(
                    Json.createObjectBuilder()
                            .add("businessId", b.getBusinessId())
                            .add("businessName", b.getBusinessName())
                            .add("address", b.getAddress())
                            .add("phone", b.getPhone())
                            .add("email", b.getEmail())
                            .add("website", b.getWebsite())
                            .add("logo", b.getLogo())
                            .build());

        return json.build();
    }

    public Optional<JsonArray> getBusinessesByKeyword(Search search) {

        Optional<List<Business>> result = bizRepo.getBusinessesByKeyword(search);
        if (result.isEmpty())
            return Optional.empty();
        List<Business> businesses = result.get();

        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Business b : businesses)
            json.add(
                    Json.createObjectBuilder()
                            .add("businessId", b.getBusinessId())
                            .add("businessName", b.getBusinessName())
                            .add("address", b.getAddress())
                            .add("phone", b.getPhone())
                            .add("email", b.getEmail())
                            .add("website", b.getWebsite())
                            .add("logo", b.getLogo())
                            .build());

        return Optional.of(json.build());
    }

    public Optional<JsonArray> getBusinessesByCategory(String category) {

        Optional<List<Business>> result = bizRepo.getBusinessesByCategory(category);
        if (result.isEmpty())
            return Optional.empty();
        List<Business> businesses = result.get();

        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Business b : businesses)
            json.add(
                    Json.createObjectBuilder()
                            .add("businessId", b.getBusinessId())
                            .add("businessName", b.getBusinessName())
                            .add("address", b.getAddress())
                            .add("phone", b.getPhone())
                            .add("email", b.getEmail())
                            .add("website", b.getWebsite())
                            .add("logo", b.getLogo())
                            .build());

        return Optional.of(json.build());
    }

    public Optional<JsonObject> getBusinessById(Integer id) {

        Optional<Business> result = bizRepo.getBusinessById(id);

        if (result.isEmpty())
            return Optional.empty();

        Business b = result.get();

        JsonObject json = Json.createObjectBuilder()
                .add("businessId", b.getBusinessId())
                .add("businessName", b.getBusinessName())
                .add("address", b.getAddress())
                .add("phone", b.getPhone())
                .add("email", b.getEmail())
                .add("website", b.getWebsite())
                .add("logo", b.getLogo())
                .build();

        return Optional.of(json);
    }

    public Optional<JsonArray> getServicesByBusinessId(Integer id) {

        List<mini_project.server.model.Service> result = bizRepo.getServicesByBusinessId(id);

        if (result.isEmpty())
            return Optional.empty();

        JsonArrayBuilder json = Json.createArrayBuilder();

        for (mini_project.server.model.Service s : result)
            json.add(Json.createObjectBuilder()
                    .add("serviceId", s.getServiceId())
                    .add("title", s.getTitle())
                    .add("description", s.getDescription())
                    .add("price", s.getPrice())
                    .build());

        return Optional.of(json.build());
    }

    public Optional<JsonArray> getReviewsByBusinessId(Integer id) {

        List<Review> result = bizRepo.getReviewsByBusinessId(id);

        if (result.isEmpty())
            return Optional.empty();

        JsonArrayBuilder json = Json.createArrayBuilder();

        for (Review r : result)
            json.add(Json.createObjectBuilder()
                    .add("reviewId", r.getReviewId())
                    .add("reviewer", r.getReviewer())
                    .add("content", r.getContent())
                    .add("rating", r.getRating())
                    .add("reviewDate", r.getReviewDate().toString())
                    .build());

        return Optional.of(json.build());
    }

    public Optional<JsonObject> getBusinessByServiceId(String serviceId) {
        Map<String, Object> business = bizRepo.getBusinessByServiceId(serviceId);
        if (business.isEmpty())
            return Optional.empty();

        return Optional.of(Json.createObjectBuilder()
            .add("email", business.get("email").toString())
            .add("businessName", business.get("businessName").toString())
            .build());
    }
}
