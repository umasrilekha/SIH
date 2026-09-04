package com.govmesh.food.govmesh;

import com.govmesh.food.entity.Consent;
import com.govmesh.food.entity.IntegrationTransaction;
import com.govmesh.food.govmesh.adapter.FoodDepartmentAdapter;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateRequest;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateResponse;
import com.govmesh.food.govmesh.dto.ConsentValidationResult;
import com.govmesh.food.govmesh.router.IntegrationRouter;
import com.govmesh.food.govmesh.service.ConsentValidationService;
import com.govmesh.food.govmesh.service.GovMeshInteroperabilityService;
import com.govmesh.food.repository.AuditLogRepository;
import com.govmesh.food.repository.ConsentRepository;
import com.govmesh.food.repository.IntegrationTransactionRepository;
import com.govmesh.food.service.ConsentPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GovMeshConsentValidationTest {

    @Mock
    private ConsentRepository consentRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private IntegrationTransactionRepository transactionRepository;

    @Mock
    private com.govmesh.food.repository.ApplicationRepository applicationRepository;

    @Mock
    private com.govmesh.food.repository.RationRecordRepository rationRecordRepository;

    private ConsentPolicyService consentPolicyService;
    private ConsentValidationService consentValidationService;
    private FoodDepartmentAdapter foodDepartmentAdapter;
    private IntegrationRouter integrationRouter;
    private GovMeshInteroperabilityService interoperabilityService;

    private Consent activeConsent;
    private Consent expiredConsent;
    private Consent revokedConsent;
    private AtomicInteger soapCallCount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        soapCallCount = new AtomicInteger(0);

        consentPolicyService = new ConsentPolicyService();
        consentValidationService = new ConsentValidationService(consentRepository, consentPolicyService);

        foodDepartmentAdapter = new FoodDepartmentAdapter(null) {
            @Override
            public CanonicalAddressUpdateResponse sendAddressUpdate(CanonicalAddressUpdateRequest canonicalRequest, String soapEndpointUrl) {
                soapCallCount.incrementAndGet();
                return CanonicalAddressUpdateResponse.builder()
                        .applicationId(canonicalRequest.getApplicationId())
                        .status("SUCCESS")
                        .message("Address update processed successfully")
                        .correlationId(canonicalRequest.getCorrelationId())
                        .targetDepartment("FOOD")
                        .build();
            }
        };

        integrationRouter = new IntegrationRouter(foodDepartmentAdapter, "http://localhost:8080/ws");
        interoperabilityService = new GovMeshInteroperabilityService(integrationRouter, transactionRepository, auditLogRepository, consentValidationService, applicationRepository, rationRecordRepository, consentRepository);

        LocalDateTime now = LocalDateTime.now();

        activeConsent = Consent.builder()
                .id(1L)
                .consentId("CONSENT-00124")
                .citizenReference("CIT-MH-998811")
                .requestingDepartment("REVENUE")
                .receivingDepartment("FOOD")
                .purpose("RATION_ADDRESS_UPDATE")
                .status("ACTIVE")
                .issuedAt(now.minusDays(5))
                .expiresAt(now.plusDays(25))
                .build();

        expiredConsent = Consent.builder()
                .id(2L)
                .consentId("CONSENT-EXPIRED-001")
                .citizenReference("CIT-MH-998801")
                .requestingDepartment("REVENUE")
                .receivingDepartment("FOOD")
                .purpose("RATION_ADDRESS_UPDATE")
                .status("EXPIRED")
                .issuedAt(now.minusDays(60))
                .expiresAt(now.minusDays(30))
                .build();

        revokedConsent = Consent.builder()
                .id(3L)
                .consentId("CONSENT-REVOKED-001")
                .citizenReference("CIT-MH-998802")
                .requestingDepartment("REVENUE")
                .receivingDepartment("FOOD")
                .purpose("RATION_ADDRESS_UPDATE")
                .status("REVOKED")
                .issuedAt(now.minusDays(20))
                .expiresAt(now.plusDays(10))
                .revokedAt(now.minusDays(10))
                .build();

        when(consentRepository.findByConsentId("CONSENT-00124")).thenReturn(Optional.of(activeConsent));
        when(consentRepository.findByConsentId("CONSENT-EXPIRED-001")).thenReturn(Optional.of(expiredConsent));
        when(consentRepository.findByConsentId("CONSENT-REVOKED-001")).thenReturn(Optional.of(revokedConsent));

        when(transactionRepository.save(any(IntegrationTransaction.class))).thenAnswer(i -> {
            IntegrationTransaction t = i.getArgument(0);
            if (t.getId() == null) t.setId(100L);
            return t;
        });
    }

    // SCENARIO 1: Valid consent
    @Test
    void testValidConsent_Allowed() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-00124", "REVENUE", "FOOD", "RATION_ADDRESS_UPDATE",
                Arrays.asList("citizen.name", "citizen.address", "citizen.address.district", "citizen.address.taluka", "verification.status")
        );

        assertEquals("ALLOWED", result.getStatus());
        assertEquals("CONSENT_VALIDATED", result.getReason());
    }

    // SCENARIO 2: Missing consent
    @Test
    void testMissingConsent_Denied() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-NON-EXISTENT", "REVENUE", "FOOD", "RATION_ADDRESS_UPDATE", Collections.emptyList()
        );

        assertEquals("BLOCKED", result.getStatus());
        assertEquals("CONSENT_NOT_FOUND", result.getReason());
    }

    // SCENARIO 3: Expired consent
    @Test
    void testExpiredConsent_Denied() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-EXPIRED-001", "REVENUE", "FOOD", "RATION_ADDRESS_UPDATE", Collections.emptyList()
        );

        assertEquals("BLOCKED", result.getStatus());
        assertEquals("CONSENT_EXPIRED", result.getReason());
    }

    // SCENARIO 4: Revoked consent
    @Test
    void testRevokedConsent_Denied() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-REVOKED-001", "REVENUE", "FOOD", "RATION_ADDRESS_UPDATE", Collections.emptyList()
        );

        assertEquals("BLOCKED", result.getStatus());
        assertEquals("CONSENT_INACTIVE", result.getReason());
    }

    // SCENARIO 5: Wrong requesting department
    @Test
    void testWrongRequestingDepartment_Denied() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-00124", "PANCHAYAT", "FOOD", "RATION_ADDRESS_UPDATE", Collections.emptyList()
        );

        assertEquals("BLOCKED", result.getStatus());
        assertEquals("CONSENT_PARTY_MISMATCH", result.getReason());
    }

    // SCENARIO 6: Wrong receiving department
    @Test
    void testWrongReceivingDepartment_Denied() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-00124", "REVENUE", "HEALTH", "RATION_ADDRESS_UPDATE", Collections.emptyList()
        );

        assertEquals("BLOCKED", result.getStatus());
        assertEquals("CONSENT_PARTY_MISMATCH", result.getReason());
    }

    // SCENARIO 7: Wrong purpose
    @Test
    void testWrongPurpose_Denied() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-00124", "REVENUE", "FOOD", "LOAN_APPLICATION", Collections.emptyList()
        );

        assertEquals("BLOCKED", result.getStatus());
        assertEquals("PURPOSE_NOT_ALLOWED", result.getReason());
    }

    // SCENARIO 8: Unauthorized field requested
    @Test
    void testUnauthorizedField_Denied() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-00124", "REVENUE", "FOOD", "RATION_ADDRESS_UPDATE",
                Arrays.asList("citizen.name", "citizen.address", "citizen.phone")
        );

        assertEquals("BLOCKED", result.getStatus());
        assertEquals("FIELD_NOT_PERMITTED", result.getReason());
    }

    // SCENARIO 9: All fields permitted
    @Test
    void testAllFieldsPermitted_Allowed() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-00124", "REVENUE", "FOOD", "RATION_ADDRESS_UPDATE",
                Arrays.asList("citizen.name", "citizen.address")
        );

        assertEquals("ALLOWED", result.getStatus());
    }

    // SCENARIO 10: Empty requested field list
    @Test
    void testEmptyRequestedFieldList_Allowed() {
        ConsentValidationResult result = consentValidationService.validate(
                "CONSENT-00124", "REVENUE", "FOOD", "RATION_ADDRESS_UPDATE", Collections.emptyList()
        );

        assertEquals("ALLOWED", result.getStatus());
    }

    // SCENARIO 11: Valid consent + valid fields reaches SOAP
    @Test
    void testValidConsentReachesSOAP() {
        CanonicalAddressUpdateRequest request = new CanonicalAddressUpdateRequest(
                "GM-2026-000124", "REVENUE", "FOOD", "REQ-2026-000124", "RATION_ADDRESS_UPDATE",
                Arrays.asList("citizen.name", "citizen.address", "citizen.address.district", "citizen.address.taluka", "verification.status"),
                new CanonicalAddressUpdateRequest.CitizenInfo("CIT-MH-998811", "Rajesh Kumar", new CanonicalAddressUpdateRequest.AddressInfo("44 Road", "DIST-PUN", "TAL-PUN-04")),
                new CanonicalAddressUpdateRequest.VerificationInfo("VALID", "REVENUE"),
                new CanonicalAddressUpdateRequest.ConsentInfo("CONSENT-00124")
        );

        CanonicalAddressUpdateResponse response = interoperabilityService.processInteroperabilityRequest(request);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals("REQ-2026-000124", response.getCorrelationId());
        assertEquals(1, soapCallCount.get(), "SOAP adapter should have been called exactly once for valid consent");
        verify(auditLogRepository, atLeastOnce()).save(argThat(log -> "CONSENT_VALIDATED".equals(log.getAction())));
    }

    // SCENARIO 12: Invalid consent does NOT reach SOAP (CRITICAL INTEGRATION TEST)
    @Test
    void testInvalidConsentDoesNotReachSOAP() {
        CanonicalAddressUpdateRequest request = new CanonicalAddressUpdateRequest(
                "GM-2026-000124", "REVENUE", "FOOD", "REQ-2026-EXPIRED", "RATION_ADDRESS_UPDATE",
                Arrays.asList("citizen.name", "citizen.address"),
                new CanonicalAddressUpdateRequest.CitizenInfo("CIT-MH-998801", "Aarti Patil", new CanonicalAddressUpdateRequest.AddressInfo("45 Chowk", "DIST-PUN", "TAL-HAV-02")),
                new CanonicalAddressUpdateRequest.VerificationInfo("VALID", "REVENUE"),
                new CanonicalAddressUpdateRequest.ConsentInfo("CONSENT-EXPIRED-001")
        );

        CanonicalAddressUpdateResponse response = interoperabilityService.processInteroperabilityRequest(request);

        assertEquals("BLOCKED", response.getStatus());
        assertEquals("CONSENT_EXPIRED", response.getErrorCode());
        assertEquals("REQ-2026-EXPIRED", response.getCorrelationId());

        // VERIFY: Food SOAP endpoint was NEVER called!
        assertEquals(0, soapCallCount.get(), "SOAP adapter MUST NOT be called when consent validation fails!");
    }

    // SCENARIO 13: Correlation ID preserved in response
    @Test
    void testCorrelationIdPreservedOnBlockedRequest() {
        CanonicalAddressUpdateRequest request = new CanonicalAddressUpdateRequest(
                "GM-2026-000124", "REVENUE", "FOOD", "REQ-2026-CORRELATION-99", "RATION_ADDRESS_UPDATE",
                Arrays.asList("citizen.name"),
                new CanonicalAddressUpdateRequest.CitizenInfo("CIT-MH-998802", "Ganesh Kulkarni", new CanonicalAddressUpdateRequest.AddressInfo("88 Station", "DIST-PUN", "TAL-HAV-02")),
                new CanonicalAddressUpdateRequest.VerificationInfo("VALID", "REVENUE"),
                new CanonicalAddressUpdateRequest.ConsentInfo("CONSENT-REVOKED-001")
        );

        CanonicalAddressUpdateResponse response = interoperabilityService.processInteroperabilityRequest(request);

        assertEquals("REQ-2026-CORRELATION-99", response.getCorrelationId());
    }

    // SCENARIO 14 & 15: Audit log created for allowed and blocked requests
    @Test
    void testAuditLogCreatedForAllowedAndBlockedRequests() {
        // Test Allowed Request Audit Log
        CanonicalAddressUpdateRequest allowedReq = new CanonicalAddressUpdateRequest(
                "GM-2026-000124", "REVENUE", "FOOD", "REQ-ALLOWED", "RATION_ADDRESS_UPDATE",
                Arrays.asList("citizen.name"),
                new CanonicalAddressUpdateRequest.CitizenInfo("CIT-MH-998811", "Rajesh Kumar", new CanonicalAddressUpdateRequest.AddressInfo("44 Road", "DIST-PUN", "TAL-PUN-04")),
                new CanonicalAddressUpdateRequest.VerificationInfo("VALID", "REVENUE"),
                new CanonicalAddressUpdateRequest.ConsentInfo("CONSENT-00124")
        );

        interoperabilityService.processInteroperabilityRequest(allowedReq);
        verify(auditLogRepository, atLeastOnce()).save(argThat(log -> "CONSENT_VALIDATED".equals(log.getAction()) && "ALLOWED".equals(log.getResult())));

        // Test Blocked Request Audit Log
        CanonicalAddressUpdateRequest blockedReq = new CanonicalAddressUpdateRequest(
                "GM-2026-000124", "REVENUE", "FOOD", "REQ-BLOCKED", "RATION_ADDRESS_UPDATE",
                Arrays.asList("citizen.name"),
                new CanonicalAddressUpdateRequest.CitizenInfo("CIT-MH-998801", "Aarti Patil", new CanonicalAddressUpdateRequest.AddressInfo("45 Chowk", "DIST-PUN", "TAL-HAV-02")),
                new CanonicalAddressUpdateRequest.VerificationInfo("VALID", "REVENUE"),
                new CanonicalAddressUpdateRequest.ConsentInfo("CONSENT-EXPIRED-001")
        );

        interoperabilityService.processInteroperabilityRequest(blockedReq);
        verify(auditLogRepository, atLeastOnce()).save(argThat(log -> "CONSENT_VALIDATION_FAILED".equals(log.getAction()) && "BLOCKED".equals(log.getResult())));
        verify(auditLogRepository, atLeastOnce()).save(argThat(log -> "INTEGRATION_BLOCKED".equals(log.getAction()) && "BLOCKED".equals(log.getResult())));
    }
}
