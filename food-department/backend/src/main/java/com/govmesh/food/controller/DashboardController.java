package com.govmesh.food.controller;

import com.govmesh.food.dto.ApplicationDTOs.ApplicationDTO;
import com.govmesh.food.dto.DashboardDTOs.RecentActivityDTO;
import com.govmesh.food.dto.DashboardDTOs.SummaryDTO;
import com.govmesh.food.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }

    @GetMapping("/recent-applications")
    public ResponseEntity<List<ApplicationDTO>> getRecentApplications() {
        return ResponseEntity.ok(dashboardService.getRecentApplications());
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<RecentActivityDTO>> getRecentActivities() {
        return ResponseEntity.ok(dashboardService.getRecentActivities());
    }
}
