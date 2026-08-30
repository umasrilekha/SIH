package com.govmesh.food.govmesh.controller;

import com.govmesh.food.entity.IntegrationTransaction;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateRequest;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateResponse;
import com.govmesh.food.govmesh.service.GovMeshInteroperabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/govmesh")
public class GovMeshIntegrationController {

    private final GovMeshInteroperabilityService interoperabilityService;

    public GovMeshIntegrationController(GovMeshInteroperabilityService interoperabilityService) {
        this.interoperabilityService = interoperabilityService;
    }

    @PostMapping("/interoperability/address-update")
    public ResponseEntity<CanonicalAddressUpdateResponse> processAddressUpdate(
            @RequestBody CanonicalAddressUpdateRequest request) {
        return ResponseEntity.ok(interoperabilityService.processInteroperabilityRequest(request));
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
