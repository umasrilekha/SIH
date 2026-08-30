package com.govmesh.food.controller;

import com.govmesh.food.dto.SystemHealthDTO;
import com.govmesh.food.service.SystemHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system-health")
public class SystemHealthController {

    private final SystemHealthService systemHealthService;

    public SystemHealthController(SystemHealthService systemHealthService) {
        this.systemHealthService = systemHealthService;
    }

    @GetMapping
    public ResponseEntity<SystemHealthDTO> getSystemHealth() {
        return ResponseEntity.ok(systemHealthService.getSystemHealth());
    }
}
