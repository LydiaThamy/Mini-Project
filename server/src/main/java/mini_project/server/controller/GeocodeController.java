package mini_project.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mini_project.server.service.GeocodeService;

@RestController
@RequestMapping("/api/geocode")
public class GeocodeController {

    @Autowired
    private GeocodeService service;

    @GetMapping("/")
    public ResponseEntity<String> getGeocode(@RequestParam String address) {
        String result;
        try {
            result = service.getGeocode(address);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }
}
