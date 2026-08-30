package com.govmesh.food.controller;

import com.govmesh.food.entity.Consent;
import com.govmesh.food.exception.ResourceNotFoundException;
import com.govmesh.food.govmesh.dto.ConsentValidationResult;
import com.govmesh.food.govmesh.service.ConsentValidationService;
import com.govmesh.food.repository.ConsentRepository;
import com.govmesh.food.service.ConsentPolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consent")
public class ConsentController {

    private final ConsentRepository consentRepository;
    private final ConsentPolicyService consentPolicyService;
    private final ConsentValidationService consentValidationService;

    public ConsentController(ConsentRepository consentRepository,
                             ConsentPolicyService consentPolicyService,
                             ConsentValidationService consentValidationService) {
        this.consentRepository = consentRepository;
        this.consentPolicyService = consentPolicyService;
        this.consentValidationService = consentValidationService;
    }

    @GetMapping
    public ResponseEntity<List<Consent>> getAllConsents() {
        return ResponseEntity.ok(consentRepository.findAll());
    }

    @GetMapping("/{consentId}")
    public ResponseEntity<Map<String, Object>> getConsentDetail(@PathVariable String consentId) {
        Consent consent = consentRepository.findByConsentId(consentId)
                .orElseThrow(() -> new ResourceNotFoundException("Consent record not found: " + consentId));

        List<String> allowedFields = consentPolicyService.getAllowedFields(consent.getPurpose());
        List<String> restrictedFields = consentPolicyService.getRestrictedFields(consent.getPurpose());

        Map<String, Object> response = new HashMap<>();
        response.put("consent", consent);
        response.put("permittedData", allowedFields);
        response.put("restrictedData", restrictedFields);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<ConsentValidationResult> validateConsent(@RequestBody Map<String, Object> request) {
        String consentId = (String) request.get("consentId");
        String requestingDept = (String) request.get("requestingDepartment");
        String receivingDept = (String) request.get("receivingDepartment");
        String purpose = (String) request.get("purpose");

        @SuppressWarnings("unchecked")
        List<String> requestedFields = (List<String>) request.get("requestedFields");

        ConsentValidationResult result = consentValidationService.validate(
                consentId, requestingDept, receivingDept, purpose, requestedFields
        );

        return ResponseEntity.ok(result);
    }
}
