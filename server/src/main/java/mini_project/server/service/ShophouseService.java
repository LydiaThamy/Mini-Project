package mini_project.server.service;

import java.io.IOException;
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
import mini_project.server.repository.GoogleRepository;
import mini_project.server.repository.RedisRepository;
import mini_project.server.repository.SqlRepository;

@Service
public class ShophouseService {

    @Autowired
    private SqlRepository sqlRepo;

    @Autowired
    private GoogleRepository googleRepo;

    // @Autowired
    // private GeoapifyRepository geoapifyRepo;

    // @Autowired
    // private MongoRespository mongoRepo;

    @Autowired
    private RedisRepository redisRepo;

    public JsonArray autocompleteKeyword(String keyword) {

        List<String> autocomplete = sqlRepo.autocompleteKeyword(keyword);

        JsonArrayBuilder json = Json.createArrayBuilder();
        for (String c : autocomplete)
            json.add(c);

        return json.build();
    }

    public JsonArray getCategories() {

        List<String> categories = sqlRepo.getCategories();
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (String c : categories)
            json.add(c);

        return json.build();
    }

    public JsonArray getAllBusinesses() {

        List<Business> businesses = sqlRepo.getAllBusinesses();

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

        Optional<List<Business>> result = sqlRepo.getBusinessesByKeyword(search);
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

        Optional<List<Business>> result = sqlRepo.getBusinessesByCategory(category);
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

        Optional<Business> result = sqlRepo.getBusinessById(id);

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

        List<mini_project.server.model.Service> result = sqlRepo.getServicesByBusinessId(id);

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

        List<Review> result = sqlRepo.getReviewsByBusinessId(id);

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

    public String getGeocode(String address) throws IOException, InterruptedException {
        // return geoapifyRepo.getGeocode(address);
        return googleRepo.getGeocode(address);
    }

    public void addToCart(String customerId, String serviceId) {
        redisRepo.addToCart(customerId, serviceId);
    }

    public Optional<JsonArray> getCart(String customerId) {
        Optional<Map<Object, Object>> result = redisRepo.getCart(customerId);
        if (result.isEmpty())
            return null;

        JsonArrayBuilder json = Json.createArrayBuilder();
        result.get().forEach((key, value) -> 
            json.add(
                Json.createObjectBuilder()
                .add(key.toString(), value.toString())
                .build()
                )
        );

        return Optional.of(json.build());
    }
}
