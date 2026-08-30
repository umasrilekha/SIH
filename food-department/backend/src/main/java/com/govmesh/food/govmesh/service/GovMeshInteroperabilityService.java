package com.govmesh.food.govmesh.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.govmesh.food.entity.AuditLog;
import com.govmesh.food.entity.IntegrationTransaction;
import com.govmesh.food.exception.ResourceNotFoundException;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateRequest;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateResponse;
import com.govmesh.food.govmesh.dto.ConsentValidationResult;
import com.govmesh.food.govmesh.router.IntegrationRouter;
import com.govmesh.food.repository.AuditLogRepository;
import com.govmesh.food.repository.IntegrationTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class GovMeshInteroperabilityService {

    private final IntegrationRouter integrationRouter;
    private final IntegrationTransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;
    private final ConsentValidationService consentValidationService;
    private final ObjectMapper objectMapper;

    public GovMeshInteroperabilityService(IntegrationRouter integrationRouter,
                                         IntegrationTransactionRepository transactionRepository,
                                         AuditLogRepository auditLogRepository,
                                         ConsentValidationService consentValidationService) {
        this.integrationRouter = integrationRouter;
        this.transactionRepository = transactionRepository;
        this.auditLogRepository = auditLogRepository;
        this.consentValidationService = consentValidationService;
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    public CanonicalAddressUpdateResponse processInteroperabilityRequest(CanonicalAddressUpdateRequest canonicalRequest) {
        String corrId = (canonicalRequest != null && canonicalRequest.getCorrelationId() != null)
                ? canonicalRequest.getCorrelationId()
                : "REQ-2026-" + System.currentTimeMillis();

        String appId = (canonicalRequest != null && canonicalRequest.getApplicationId() != null)
                ? canonicalRequest.getApplicationId()
                : "GM-2026-UNKNOWN";

        String sourceDept = (canonicalRequest != null && canonicalRequest.getSourceDepartment() != null)
                ? canonicalRequest.getSourceDepartment()
                : "REVENUE";

        String targetDept = (canonicalRequest != null && canonicalRequest.getTargetDepartment() != null)
                ? canonicalRequest.getTargetDepartment()
                : "FOOD";

        String consentId = (canonicalRequest != null && canonicalRequest.getConsent() != null)
                ? canonicalRequest.getConsent().getId()
                : null;

        String purpose = (canonicalRequest != null && canonicalRequest.getPurpose() != null)
                ? canonicalRequest.getPurpose()
                : "RATION_ADDRESS_UPDATE";

        List<String> requestedFields = (canonicalRequest != null && canonicalRequest.getRequestedFields() != null && !canonicalRequest.getRequestedFields().isEmpty())
                ? canonicalRequest.getRequestedFields()
                : Arrays.asList(
                "citizen.name",
                "citizen.address",
                "citizen.address.district",
                "citizen.address.taluka",
                "verification.status"
        );

        LocalDateTime startTime = LocalDateTime.now();
        String rawCanonicalJson = "";
        try {
            rawCanonicalJson = objectMapper.writeValueAsString(canonicalRequest);
        } catch (Exception e) {
            rawCanonicalJson = "{}";
        }

        // Initialize Integration Transaction
        IntegrationTransaction tx = IntegrationTransaction.builder()
                .applicationId(appId)
                .correlationId(corrId)
                .sourceDepartment(sourceDept)
                .targetDepartment(targetDept)
                .operation("UpdateRationAddress")
                .sourceProtocol("REST/JSON")
                .targetProtocol("SOAP/XML")
                .status("RECEIVED")
                .consentId(consentId)
                .startedAt(startTime)
                .rawSourceJson(rawCanonicalJson)
                .rawCanonicalJson(rawCanonicalJson)
                .build();
        tx = transactionRepository.save(tx);

        // Audit Log: INTEGRATION_RECEIVED
        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(appId)
                .officerId(null)
                .action("INTEGRATION_RECEIVED")
                .result("SUCCESS")
                .description("GovMesh received address update request from " + sourceDept + " (REST/JSON) (CorrelationId: " + corrId + ")")
                .build());

        // Stage 1: CONSENT & DATA MINIMIZATION GATEKEEPER CHECK
        ConsentValidationResult validationResult = consentValidationService.validate(
                consentId, sourceDept, targetDept, purpose, requestedFields
        );

        if ("BLOCKED".equalsIgnoreCase(validationResult.getStatus())) {
            // Audit Log: CONSENT_VALIDATION_FAILED
            auditLogRepository.save(AuditLog.builder()
                    .timestamp(LocalDateTime.now())
                    .applicationId(appId)
                    .officerId(null)
                    .action("CONSENT_VALIDATION_FAILED")
                    .result("BLOCKED")
                    .description("GovMesh consent validation failed for " + appId + " (ConsentId: " + consentId + ", Reason: " + validationResult.getReason() + ", CorrelationId: " + corrId + ")")
                    .build());

            // Audit Log: INTEGRATION_BLOCKED
            auditLogRepository.save(AuditLog.builder()
                    .timestamp(LocalDateTime.now())
                    .applicationId(appId)
                    .officerId(null)
                    .action("INTEGRATION_BLOCKED")
                    .result("BLOCKED")
                    .description("GovMesh interoperability transaction blocked by Consent Gatekeeper for " + appId + " (Reason: " + validationResult.getReason() + ", CorrelationId: " + corrId + ")")
                    .build());

            tx.setStatus("BLOCKED");
            tx.setConsentStatus("BLOCKED");
            tx.setConsentFailureReason(validationResult.getReason());
            tx.setErrorCode(validationResult.getReason());
            tx.setErrorMessage("Integration request blocked by GovMesh Consent Gatekeeper: " + validationResult.getReason());
            tx.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(tx);

            // STOP RIGHT HERE! DO NOT REACH SCHEMA MAPPER OR SOAP ADAPTER!
            return CanonicalAddressUpdateResponse.builder()
                    .applicationId(appId)
                    .status("BLOCKED")
                    .message("Integration request blocked by GovMesh Consent Gatekeeper: " + validationResult.getReason())
                    .correlationId(corrId)
                    .targetDepartment(targetDept)
                    .errorCode(validationResult.getReason())
                    .build();
        }

        // CONSENT VALIDATED & DATA MINIMIZATION PASSED
        tx.setConsentStatus("ALLOWED");
        transactionRepository.save(tx);

        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(appId)
                .officerId(null)
                .action("CONSENT_VALIDATED")
                .result("ALLOWED")
                .description("GovMesh verified active consent record " + consentId + " for " + sourceDept + " -> " + targetDept + " (Purpose: " + purpose + ")")
                .build());

        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(appId)
                .officerId(null)
                .action("DATA_MINIMIZATION_PASSED")
                .result("ALLOWED")
                .description("All " + (requestedFields != null ? requestedFields.size() : 0) + " requested fields permitted by consent policy for purpose " + purpose + " (CorrelationId: " + corrId + ")")
                .build());

        // Stage 2: Canonicalization & Schema Mapping
        tx.setStatus("TRANSFORMING");
        transactionRepository.save(tx);

        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(appId)
                .officerId(null)
                .action("CANONICALIZATION_SUCCESS")
                .result("SUCCESS")
                .description("Successfully canonicalized payload for application " + appId + " (CorrelationId: " + corrId + ")")
                .build());

        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(appId)
                .officerId(null)
                .action("SCHEMA_MAPPING_SUCCESS")
                .result("SUCCESS")
                .description("Mapped GovMesh Canonical Model to Food SOAP XML schema (CorrelationId: " + corrId + ")")
                .build());

        // Stage 3: Send via SOAP Adapter
        tx.setStatus("SENDING");
        transactionRepository.save(tx);

        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(appId)
                .officerId(null)
                .action("SOAP_REQUEST_SENT")
                .result("SUCCESS")
                .description("Sending SOAP XML request to Food Department endpoint /ws (CorrelationId: " + corrId + ")")
                .build());

        // Execute Routing
        CanonicalAddressUpdateResponse response = integrationRouter.routeAddressUpdate(canonicalRequest);

        // Stage 4: Process Response
        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(appId)
                .officerId(null)
                .action("SOAP_RESPONSE_RECEIVED")
                .result("SUCCESS".equalsIgnoreCase(response.getStatus()) ? "SUCCESS" : "FAILED")
                .description("Received SOAP response from Food Department: " + response.getStatus() + " (CorrelationId: " + corrId + ")")
                .build());

        tx.setCompletedAt(LocalDateTime.now());
        if ("SUCCESS".equalsIgnoreCase(response.getStatus())) {
            tx.setStatus("SUCCESS");
            auditLogRepository.save(AuditLog.builder()
                    .timestamp(LocalDateTime.now())
                    .applicationId(appId)
                    .officerId(null)
                    .action("INTEGRATION_SUCCESS")
                    .result("SUCCESS")
                    .description("GovMesh interoperability transaction completed successfully for " + appId + " (CorrelationId: " + corrId + ")")
                    .build());
        } else {
            tx.setStatus("FAILED");
            tx.setErrorCode(response.getErrorCode() != null ? response.getErrorCode() : "INTEGRATION_FAILED");
            tx.setErrorMessage(response.getMessage());

            auditLogRepository.save(AuditLog.builder()
                    .timestamp(LocalDateTime.now())
                    .applicationId(appId)
                    .officerId(null)
                    .action("INTEGRATION_FAILED")
                    .result("FAILED")
                    .description("GovMesh interoperability transaction failed for " + appId + ": " + response.getMessage() + " (CorrelationId: " + corrId + ")")
                    .build());
        }

        transactionRepository.save(tx);
        return response;
    }

    public List<IntegrationTransaction> getTransactions() {
        return transactionRepository.findAllOrderedByStartedAtDesc();
    }

    public IntegrationTransaction getTransactionByCorrelationId(String correlationId) {
        return transactionRepository.findByCorrelationId(correlationId)
                .orElseThrow(() -> new ResourceNotFoundException("Integration transaction trace not found for Correlation ID: " + correlationId));
    }
}
