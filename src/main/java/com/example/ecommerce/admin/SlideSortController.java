package com.example.ecommerce.admin;

import com.example.ecommerce.services.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class SlideSortController {
    @Autowired
    private SlideService slideService;

    @PostMapping("/update-slide-order")
    public ResponseEntity<?> updateOderSlide(@RequestBody Map<String, List<Long>> request) {
        List<Long> order = request.get("order");
        boolean success = slideService.updateSlideOrder(order);
        return success ? ResponseEntity.ok().body(Map.of("success", true)) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
    }
}
