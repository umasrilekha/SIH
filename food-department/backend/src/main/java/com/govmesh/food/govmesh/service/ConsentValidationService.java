package com.govmesh.food.govmesh.service;

import com.govmesh.food.entity.Consent;
import com.govmesh.food.govmesh.dto.ConsentValidationResult;
import com.govmesh.food.repository.ConsentRepository;
import com.govmesh.food.service.ConsentPolicyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsentValidationService {

    private final ConsentRepository consentRepository;
    private final ConsentPolicyService consentPolicyService;

    public ConsentValidationService(ConsentRepository consentRepository, ConsentPolicyService consentPolicyService) {
        this.consentRepository = consentRepository;
        this.consentPolicyService = consentPolicyService;
    }

    public ConsentValidationResult validate(String consentId,
                                            String requestingDepartment,
                                            String receivingDepartment,
                                            String purpose,
                                            List<String> requestedFields) {
        LocalDateTime now = LocalDateTime.now();

        // RULE 1 — CONSENT MUST EXIST
        if (consentId == null || consentId.trim().isEmpty()) {
            return ConsentValidationResult.builder()
                    .status("BLOCKED")
                    .reason("CONSENT_NOT_FOUND")
                    .consentId(consentId)
                    .purpose(purpose)
                    .requestedFields(requestedFields)
                    .allowedFields(consentPolicyService.getAllowedFields(purpose))
                    .timestamp(now)
                    .build();
        }

        Optional<Consent> consentOpt = consentRepository.findByConsentId(consentId);
        if (consentOpt.isEmpty()) {
            return ConsentValidationResult.builder()
                    .status("BLOCKED")
                    .reason("CONSENT_NOT_FOUND")
                    .consentId(consentId)
                    .purpose(purpose)
                    .requestedFields(requestedFields)
                    .allowedFields(consentPolicyService.getAllowedFields(purpose))
                    .timestamp(now)
                    .build();
        }

        Consent consent = consentOpt.get();

        // RULE 2 — CONSENT MUST BELONG TO THE REQUEST
        if (!isDepartmentMatch(requestingDepartment, consent.getRequestingDepartment()) ||
            !isDepartmentMatch(receivingDepartment, consent.getReceivingDepartment())) {
            return ConsentValidationResult.builder()
                    .status("BLOCKED")
                    .reason("CONSENT_PARTY_MISMATCH")
                    .consentId(consentId)
                    .purpose(purpose)
                    .requestedFields(requestedFields)
                    .allowedFields(consentPolicyService.getAllowedFields(consent.getPurpose()))
                    .timestamp(now)
                    .build();
        }

        // RULE 3 — PURPOSE MUST MATCH
        if (purpose == null || !purpose.equalsIgnoreCase(consent.getPurpose())) {
            return ConsentValidationResult.builder()
                    .status("BLOCKED")
                    .reason("PURPOSE_NOT_ALLOWED")
                    .consentId(consentId)
                    .purpose(purpose)
                    .requestedFields(requestedFields)
                    .allowedFields(consentPolicyService.getAllowedFields(consent.getPurpose()))
                    .timestamp(now)
                    .build();
        }

        // RULE 4 — CONSENT MUST BE ACTIVE
        if ("REVOKED".equalsIgnoreCase(consent.getStatus()) || !"ACTIVE".equalsIgnoreCase(consent.getStatus())) {
            if ("EXPIRED".equalsIgnoreCase(consent.getStatus())) {
                return ConsentValidationResult.builder()
                        .status("BLOCKED")
                        .reason("CONSENT_EXPIRED")
                        .consentId(consentId)
                        .purpose(purpose)
                        .requestedFields(requestedFields)
                        .allowedFields(consentPolicyService.getAllowedFields(purpose))
                        .timestamp(now)
                        .build();
            }
            return ConsentValidationResult.builder()
                    .status("BLOCKED")
                    .reason("CONSENT_INACTIVE")
                    .consentId(consentId)
                    .purpose(purpose)
                    .requestedFields(requestedFields)
                    .allowedFields(consentPolicyService.getAllowedFields(purpose))
                    .timestamp(now)
                    .build();
        }

        // RULE 5 — EXPIRY
        if (consent.getExpiresAt() != null && now.isAfter(consent.getExpiresAt())) {
            return ConsentValidationResult.builder()
                    .status("BLOCKED")
                    .reason("CONSENT_EXPIRED")
                    .consentId(consentId)
                    .purpose(purpose)
                    .requestedFields(requestedFields)
                    .allowedFields(consentPolicyService.getAllowedFields(purpose))
                    .timestamp(now)
                    .build();
        }

        // RULE 6 — FIELD MINIMIZATION
        List<String> allowedFields = consentPolicyService.getAllowedFields(purpose);
        if (requestedFields != null && !requestedFields.isEmpty()) {
            for (String field : requestedFields) {
                if (!allowedFields.contains(field)) {
                    return ConsentValidationResult.builder()
                            .status("BLOCKED")
                            .reason("FIELD_NOT_PERMITTED")
                            .consentId(consentId)
                            .purpose(purpose)
                            .requestedFields(requestedFields)
                            .allowedFields(allowedFields)
                            .timestamp(now)
                            .build();
                }
            }
        }

        // ALL CHECKS PASSED -> ALLOW
        return ConsentValidationResult.builder()
                .status("ALLOWED")
                .reason("CONSENT_VALIDATED")
                .consentId(consentId)
                .purpose(purpose)
                .requestedFields(requestedFields != null && !requestedFields.isEmpty() ? requestedFields : allowedFields)
                .allowedFields(allowedFields)
                .timestamp(now)
                .build();
    }

    private boolean isDepartmentMatch(String reqDept, String consentDept) {
        if (reqDept == null || consentDept == null) return false;
        String r = reqDept.trim().toUpperCase();
        String c = consentDept.trim().toUpperCase();
        if (r.equals(c)) return true;
        // Normalize alias names if needed (e.g., FOOD vs FOOD_SUPPLIES)
        if ((r.contains("FOOD") && c.contains("FOOD"))) return true;
        return false;
    }
}
