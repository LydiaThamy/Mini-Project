package mini_project.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import mini_project.server.model.Business;
import mini_project.server.repository.SqlRepository;

@Service
public class ShophouseService {
    
    @Autowired
    private SqlRepository sqlRepo;


    public JsonArray getCategories() {

        List<String> categories = sqlRepo.getCategories();
        JsonArrayBuilder json = Json.createArrayBuilder();
        for(String c: categories)
            json.add(c);

        return json.build();
    }

    public Optional<JsonArray> getBusinesses(String keyword) {

        Optional<List<Business>> result = sqlRepo.getBusinesses(keyword);
        if (result.isEmpty())
            return Optional.empty();
        List<Business> businesses = result.get();

        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Business b: businesses)
            json.add(
                Json.createObjectBuilder()
                    .add("businessId", b.getBusinessId())
                    .add("businessName", b.getBusinessName())
                    .add("address", b.getAddress())
                    .add("phone", b.getPhone())
                    .add("email", b.getEmail())
                    .add("website", b.getWebsite())
                    .add("logo", b.getLogo())
                    .build()
            );

        return Optional.of(json.build());
    }

    public Optional<JsonArray> getBusinessesByCategory(String category) {

        Optional<List<Business>> result = sqlRepo.getBusinessesByCategory(category);
        if (result.isEmpty())
            return Optional.empty();
        List<Business> businesses = result.get();

        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Business b: businesses)
            json.add(
                Json.createObjectBuilder()
                    .add("businessId", b.getBusinessId())
                    .add("businessName", b.getBusinessName())
                    .add("address", b.getAddress())
                    .add("phone", b.getPhone())
                    .add("email", b.getEmail())
                    .add("website", b.getWebsite())
                    .add("logo", b.getLogo())
                    .build()
            );

        return Optional.of(json.build());
    }

    // public Optional<JsonArray> getBusinesses(String category, Optional<String> region) {

    //     Optional<List<Business>> result = sqlRepo.getBusinesses(category, region);
    //     if (result.isEmpty())
    //         return Optional.empty();
    //     List<Business> businesses = result.get();

    //     JsonArrayBuilder json = Json.createArrayBuilder();
    //     for (Business b: businesses)
    //         json.add(
    //             Json.createObjectBuilder()
    //                 .add("businessId", b.getBusinessId())
    //                 .add("businessName", b.getBusinessName())
    //                 .add("address", b.getAddress())
    //                 .add("phone", b.getPhone())
    //                 .add("email", b.getEmail())
    //                 .add("website", b.getWebsite())
    //                 .add("logo", b.getLogo())
    //                 .build()
    //         );

    //     return Optional.of(json.build());
    // }
}
