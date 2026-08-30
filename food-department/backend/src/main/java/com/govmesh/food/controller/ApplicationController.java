package com.govmesh.food.controller;

import com.govmesh.food.dto.ApplicationDTOs.*;
import com.govmesh.food.entity.AuditLog;
import com.govmesh.food.security.UserPrincipal;
import com.govmesh.food.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getApplications(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(applicationService.getApplications(query, status, type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDetailDTO> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/code/{applicationId}")
    public ResponseEntity<ApplicationDetailDTO> getApplicationByApplicationId(@PathVariable String applicationId) {
        return ResponseEntity.ok(applicationService.getApplicationByApplicationId(applicationId));
    }

    @PostMapping("/{id}/start-review")
    public ResponseEntity<ApplicationDetailDTO> startReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(applicationService.startReview(id, currentUser));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApplicationDetailDTO> approveApplication(
            @PathVariable Long id,
            @RequestBody(required = false) ActionRequestDTO body,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        String comments = body != null ? body.getComments() : null;
        return ResponseEntity.ok(applicationService.approveApplication(id, comments, currentUser));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApplicationDetailDTO> rejectApplication(
            @PathVariable Long id,
            @RequestBody ActionRequestDTO body,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        String reason = body != null ? body.getReason() : null;
        return ResponseEntity.ok(applicationService.rejectApplication(id, reason, currentUser));
    }

    @PostMapping("/{id}/request-information")
    public ResponseEntity<ApplicationDetailDTO> requestInformation(
            @PathVariable Long id,
            @RequestBody ActionRequestDTO body,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        String comments = body != null ? body.getComments() : null;
        return ResponseEntity.ok(applicationService.requestInformation(id, comments, currentUser));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<AuditLog>> getApplicationHistory(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationHistory(id));
    }
}
