package com.govmesh.food.govmesh.controller;

import com.govmesh.food.entity.IntegrationTransaction;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateRequest;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateResponse;
import com.govmesh.food.govmesh.service.GovMeshInteroperabilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/govmesh")
public class GovMeshIntegrationController {

    private static final Logger log = LoggerFactory.getLogger(GovMeshIntegrationController.class);

    private final GovMeshInteroperabilityService interoperabilityService;
    private final String configuredApiKey;

    public GovMeshIntegrationController(GovMeshInteroperabilityService interoperabilityService,
                                        @Value("${govmesh.api-key:gm-secret-key-2026-interop}") String configuredApiKey) {
        this.interoperabilityService = interoperabilityService;
        this.configuredApiKey = configuredApiKey != null ? configuredApiKey.trim() : "gm-secret-key-2026-interop";
    }

    @PostMapping("/interoperability/address-update")
    public ResponseEntity<CanonicalAddressUpdateResponse> processAddressUpdate(
            @RequestHeader(value = "X-GovMesh-API-Key", required = false) String apiKey,
            @RequestHeader(value = "X-GovMesh-Sent-At", required = false) String sentAtHeader,
            @RequestBody CanonicalAddressUpdateRequest request) {

        log.info("Incoming GovMesh interoperability request for App ID: {}, Correlation ID: {}",
                request != null ? request.getApplicationId() : "N/A",
                request != null ? request.getCorrelationId() : "N/A");

        // 1. Mandatory API Key Authentication
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.warn("GovMesh request rejected: Missing or blank X-GovMesh-API-Key header.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    CanonicalAddressUpdateResponse.builder()
                            .applicationId(request != null ? request.getApplicationId() : null)
                            .correlationId(request != null ? request.getCorrelationId() : null)
                            .targetDepartment("FOOD")
                            .status("FAILED")
                            .errorCode("UNAUTHORIZED")
                            .message("Missing or empty X-GovMesh-API-Key authentication header")
                            .build()
            );
        }

        if (!configuredApiKey.equals(apiKey.trim())) {
            log.warn("GovMesh request rejected: Invalid X-GovMesh-API-Key credentials provided.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    CanonicalAddressUpdateResponse.builder()
                            .applicationId(request != null ? request.getApplicationId() : null)
                            .correlationId(request != null ? request.getCorrelationId() : null)
                            .targetDepartment("FOOD")
                            .status("FAILED")
                            .errorCode("FORBIDDEN")
                            .message("Invalid X-GovMesh-API-Key header credentials")
                            .build()
            );
        }

        // If sentAt is not in body but present in header, attach it
        if (request != null && request.getSentAt() == null && sentAtHeader != null) {
            request.setSentAt(sentAtHeader);
        }

        CanonicalAddressUpdateResponse response = interoperabilityService.processInteroperabilityRequest(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<IntegrationTransaction>> getTransactions() {
        return ResponseEntity.ok(interoperabilityService.getTransactions());
    }

    @GetMapping("/transactions/{correlationId}")
    public ResponseEntity<IntegrationTransaction> getTransactionByCorrelationId(
            @PathVariable String correlationId) {
        return ResponseEntity.ok(interoperabilityService.getTransactionByCorrelationId(correlationId));
    }
}
