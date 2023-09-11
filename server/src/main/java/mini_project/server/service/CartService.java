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
import mini_project.server.repository.BusinessRepository;
import mini_project.server.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private BusinessRepository bizRepo;

    public void addToCart(String customerId, String serviceId) {
        cartRepo.addToCart(customerId, serviceId);
    }

    public Optional<JsonArray> getCart(String customerId) {
        Optional<Map<Object, Object>> result = cartRepo.getCart(customerId);
        if (result.isEmpty())
            return Optional.empty();

        JsonArrayBuilder json = Json.createArrayBuilder();
        result.get().forEach((key, value) -> {

            // get business name, service title from sql repo
            Map<String, Object> business = bizRepo.getBusinessByServiceId(key.toString());

            // create json
            json.add(Json.createObjectBuilder()
                    .add("serviceId", key.toString())
                    .add("quantity", value.toString())
                    .add("businessName", business.get("business_name").toString())
                    .add("title", business.get("title").toString())
                    .add("logo", business.get("logo").toString())
                    .add("price", business.get("price").toString())
                    .build());
        });

        return Optional.of(json.build());
    }

    public void updateCart(String customerId, List<Map<String, String>> cart) {
        cartRepo.updateCart(customerId, cart);
    }

    public void deleteItem(String customerId, String serviceId) {
        cartRepo.deleteItem(customerId, serviceId);
    }

    public JsonObject getItem(String customerId, String serviceId) {
        Integer quantity = cartRepo.getItemQuantity(customerId, serviceId);
        System.out.println(quantity);

        // get business name, service title from sql repo
        Map<String, Object> business = bizRepo.getBusinessByServiceId(serviceId);
        return Json.createObjectBuilder()
                .add("serviceId", serviceId)
                .add("quantity", quantity)
                .add("businessName", business.get("business_name").toString())
                .add("title", business.get("title").toString())
                .add("logo", business.get("logo").toString())
                .add("price", business.get("price").toString())
                .build();
    }
}
