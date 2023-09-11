package mini_project.server.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonArray;
import mini_project.server.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{customerId}")
    public ResponseEntity<String> getCart(@PathVariable String customerId) {
        System.out.println(customerId);
        Optional<JsonArray> result = cartService.getCart(customerId);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get().toString());
    }

    @PostMapping("/add/{customerId}")
    public ResponseEntity<String> addCart(@PathVariable String customerId,
            @RequestBody Map<String, String> payload) {
        cartService.addToCart(customerId, payload.get("serviceId"));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<String> updateCart(@PathVariable String customerId,
            @RequestBody List<Map<String, String>> payload) {
        cartService.updateCart(customerId, payload);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<String> deleteItem(@PathVariable String serviceId,
            @RequestParam String customerId) {
        cartService.deleteItem(customerId, serviceId);
        return ResponseEntity.ok("Item deleted");
    }

}
