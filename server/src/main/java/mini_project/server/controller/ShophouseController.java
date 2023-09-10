package mini_project.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import mini_project.server.model.Payment;
import mini_project.server.model.Search;
import mini_project.server.service.ShophouseService;

@RestController
@RequestMapping("/api/shophouse")
public class ShophouseController {

    // @Value("${stripe.api.key.secret}")
    // private static String stripeSecretKey;

    @Autowired
    private ShophouseService service;

    private static void init() {
        Stripe.apiKey = "sk_test_51NoOROLLMJMhSmwKu69e7EXDjEK6HOfO74nfIKgSW5Nq1Ziwr9WWGzSYFlfQX3rXY29cnWek6ubePJNxzMEcIuj9001vpaIc6H";
    }

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
    public ResponseEntity<String> getCart(@PathVariable String customerId) {
        Optional<JsonArray> result = service.getCart(customerId);

        if (result == null)
            return ResponseEntity.internalServerError().build();

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @PostMapping("/cart/add/{customerId}")
    public ResponseEntity<String> addCart(@PathVariable String customerId,
            @RequestBody Map<String, String> payload) {
        service.addToCart(customerId, payload.get("serviceId"));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cart/update/{customerId}")
    public ResponseEntity<String> updateCart(@PathVariable String customerId,
            @RequestBody List<Map<String, String>> payload) {
        service.updateCart(customerId, payload);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<String> saveUser(@AuthenticationPrincipal OAuth2User principal) {
        Optional<JsonObject> result = service.saveUser(principal);
        if (result.isEmpty())
            return ResponseEntity.badRequest().body("User cannot be saved");

        return ResponseEntity.ok(result.get().toString());
    }

    @PostMapping("/payment")
    public ResponseEntity<String> paymentWithCheckoutPage(@RequestBody Payment payment) throws StripeException {
        
        System.out.println(payment);
        // We initilize stripe object with the api key
        init();

        // create a Gson object
        Gson gson = new Gson();

        // We create a stripe session parameters
        SessionCreateParams params = SessionCreateParams.builder()
                // We will use the credit card payment method
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(payment.getSuccessUrl())
                .setCancelUrl(
                        payment.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setQuantity(payment.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(payment.getCurrency()).setUnitAmount(payment.getAmount())
                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                        .builder().setName(payment.getName()).build())
                                                .build())
                                .build())
                .build();

        // create a stripe session
        Session session = Session.create(params);

        // We get the sessionId and we putted inside the response data you can get more
        // info from the session object
        Map<String, String> responseData = new HashMap<>();
        responseData.put("id", session.getId());

        // We can return only the sessionId as a String
        return ResponseEntity.ok(gson.toJson(responseData));
    }

}
