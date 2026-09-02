package com.govmesh.food.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping(value = {"/", "/health", "/api/health"})
    public ResponseEntity<Map<String, Object>> rootHealth() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "govmesh-food-department-backend");
        response.put("department", "Food, Civil Supplies & Consumer Protection Department");
        response.put("environment", "production");
        response.put("endpoints", Map.of(
            "interoperability", "/api/govmesh/interoperability/address-update",
            "soapWebService", "/ws",
            "transactions", "/api/govmesh/transactions",
            "systemHealth", "/api/system-health"
        ));
        return ResponseEntity.ok(response);
    }
}
