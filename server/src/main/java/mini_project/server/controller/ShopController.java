package mini_project.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import mini_project.server.service.ShopService;

@RestController
@RequestMapping("/api/shop")
public class ShopController {
    
    @Autowired
    private ShopService service;

    @GetMapping("/category")
    public ResponseEntity<String> getCategories() {

        List<String> categories = service.getCategories();
        JsonArrayBuilder json = Json.createArrayBuilder();

        for(String c: categories)
            json.add(c);

        return ResponseEntity.ok(json.build().toString());
        
    }
}
