package com.govmesh.food.govmesh;

import com.govmesh.food.entity.Application;
import com.govmesh.food.entity.AuditLog;
import com.govmesh.food.entity.Consent;
import com.govmesh.food.entity.IntegrationTransaction;
import com.govmesh.food.entity.RationRecord;
import com.govmesh.food.entity.User;
import com.govmesh.food.govmesh.controller.GovMeshIntegrationController;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateRequest;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateResponse;
import com.govmesh.food.govmesh.router.IntegrationRouter;
import com.govmesh.food.govmesh.service.ConsentValidationService;
import com.govmesh.food.govmesh.service.FoodCallbackService;
import com.govmesh.food.govmesh.service.GovMeshInteroperabilityService;
import com.govmesh.food.repository.ApplicationRepository;
import com.govmesh.food.repository.AuditLogRepository;
import com.govmesh.food.repository.ConsentRepository;
import com.govmesh.food.repository.IntegrationTransactionRepository;
import com.govmesh.food.repository.NotificationRepository;
import com.govmesh.food.repository.RationRecordRepository;
import com.govmesh.food.security.UserPrincipal;
import com.govmesh.food.service.ApplicationService;
import com.govmesh.food.service.ConsentPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GovMeshLiveInteroperabilityTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private RationRecordRepository rationRecordRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private IntegrationTransactionRepository transactionRepository;

    @Mock
    private ConsentRepository consentRepository;

    @Mock
    private NotificationRepository notificationRepository;

    private IntegrationRouter integrationRouter;
    private FoodCallbackService foodCallbackService;
    private ConsentPolicyService consentPolicyService;
    private ConsentValidationService consentValidationService;
    private GovMeshInteroperabilityService interoperabilityService;
    private GovMeshIntegrationController controller;
    private ApplicationService applicationService;

    private final String VALID_API_KEY = "gm-secret-key-2026-interop";
    private UserPrincipal testOfficer;
    private AtomicInteger callbackCount;
    private AtomicReference<String> lastDispatchedStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        callbackCount = new AtomicInteger(0);
        lastDispatchedStatus = new AtomicReference<>(null);

        foodCallbackService = new FoodCallbackService("http://localhost:9999/callback") {
            @Override
            public void dispatchStatusCallback(String applicationId,
                                               String correlationId,
                                               Integer requestVersion,
                                               String status,
                                               String acknowledgementId,
                                               String receivedAt,
                                               String validatedAt,
                                               String acceptedAt,
                                               String processingStartedAt,
                                               String completedAt,
                                               String canonicalRequestHash,
                                               String documentHash) {
                callbackCount.incrementAndGet();
                lastDispatchedStatus.set(status);
            }
        };

        com.govmesh.food.govmesh.adapter.FoodDepartmentAdapter mockAdapter = new com.govmesh.food.govmesh.adapter.FoodDepartmentAdapter(null) {
            @Override
            public CanonicalAddressUpdateResponse sendAddressUpdate(CanonicalAddressUpdateRequest canonicalRequest, String soapEndpointUrl) {
                return CanonicalAddressUpdateResponse.builder()
                        .applicationId(canonicalRequest.getApplicationId())
                        .correlationId(canonicalRequest.getCorrelationId())
                        .status("SUCCESS")
                        .message("Address update processed successfully")
                        .targetDepartment("FOOD")
                        .build();
            }
        };

        integrationRouter = new IntegrationRouter(mockAdapter, "http://localhost:8080/ws");

        consentPolicyService = new ConsentPolicyService();
        consentValidationService = new ConsentValidationService(consentRepository, consentPolicyService);
        interoperabilityService = new GovMeshInteroperabilityService(
                integrationRouter,
                transactionRepository,
                auditLogRepository,
                consentValidationService,
                applicationRepository,
                rationRecordRepository,
                consentRepository
        );

        controller = new GovMeshIntegrationController(interoperabilityService, VALID_API_KEY);
        applicationService = new ApplicationService(
                applicationRepository,
                rationRecordRepository,
                auditLogRepository,
                notificationRepository,
                foodCallbackService
        );

        User officerUser = new User();
        officerUser.setId(1L);
        officerUser.setUsername("fso_officer");
        officerUser.setFullName("Food Supply Officer");
        officerUser.setRole("SENIOR_OFFICER");
        officerUser.setEmployeeId("EMP-FOOD-001");
        testOfficer = new UserPrincipal(officerUser);

        when(transactionRepository.save(any(IntegrationTransaction.class))).thenAnswer(i -> {
            IntegrationTransaction t = i.getArgument(0);
            if (t.getId() == null) t.setId(100L);
            return t;
        });

        when(auditLogRepository.save(any(AuditLog.class))).thenAnswer(i -> i.getArgument(0));
    }

    private CanonicalAddressUpdateRequest createSampleRequest(String appId, String corrId) {
        return CanonicalAddressUpdateRequest.builder()
                .applicationId(appId)
                .correlationId(corrId)
                .requestVersion(1)
                .serviceCode("FOOD_RATION_ADDRESS_CHANGE")
                .requestType("ADDRESS_UPDATE")
                .sourceDepartment("REVENUE")
                .targetDepartment("FOOD")
                .canonicalRequestHash("a1b2c3d4e5f67890123456789abcdef0123456789abcdef0123456789abcdef0")
                .documentHash("f0e1d2c3b4a59876543210fedcba9876543210fedcba9876543210fedcba9876")
                .createdAt("2026-09-04T10:00:00.000Z")
                .sentAt("2026-09-04T10:00:01.000Z")
                .purpose("RATION_ADDRESS_UPDATE")
                .citizen(new CanonicalAddressUpdateRequest.CitizenInfo(
                        "CIT-MH-998811",
                        "Rajesh Kumar",
                        new CanonicalAddressUpdateRequest.AddressInfo("Plot 42, Green Park, Pune - 411001", "DIST-PUN", "TAL-PUN-01")
                ))
                .verification(new CanonicalAddressUpdateRequest.VerificationInfo("VALID", "REVENUE"))
                .consent(new CanonicalAddressUpdateRequest.ConsentInfo("CONSENT-GM-2026-01"))
                .documents(Collections.singletonList(
                        new CanonicalAddressUpdateRequest.DocumentInfo(
                                "DOC-001",
                                "Electricity_Bill.pdf",
                                "application/pdf",
                                "154200",
                                "f0e1d2c3b4a59876543210fedcba9876543210fedcba9876543210fedcba9876"
                        )
                ))
                .build();
    }

    @Test
    void testAddressUpdate_MissingApiKey_Returns401Unauthorized() {
        CanonicalAddressUpdateRequest req = createSampleRequest("GM-2026-001", "REQ-001");

        // Null API key
        ResponseEntity<CanonicalAddressUpdateResponse> respNull = controller.processAddressUpdate(null, null, req);
        assertEquals(HttpStatus.UNAUTHORIZED, respNull.getStatusCode());
        assertNotNull(respNull.getBody());
        assertEquals("FAILED", respNull.getBody().getStatus());
        assertEquals("UNAUTHORIZED", respNull.getBody().getErrorCode());

        // Blank API key
        ResponseEntity<CanonicalAddressUpdateResponse> respBlank = controller.processAddressUpdate("   ", null, req);
        assertEquals(HttpStatus.UNAUTHORIZED, respBlank.getStatusCode());
        assertEquals("FAILED", respBlank.getBody().getStatus());
        assertEquals("UNAUTHORIZED", respBlank.getBody().getErrorCode());
    }

    @Test
    void testAddressUpdate_InvalidApiKey_Returns403Forbidden() {
        CanonicalAddressUpdateRequest req = createSampleRequest("GM-2026-001", "REQ-001");

        ResponseEntity<CanonicalAddressUpdateResponse> resp = controller.processAddressUpdate("invalid-token-123", null, req);
        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("FAILED", resp.getBody().getStatus());
        assertEquals("FORBIDDEN", resp.getBody().getErrorCode());
    }

    @Test
    void testAddressUpdate_ValidApiKey_Returns200WithAcknowledgementAndVerifications() {
        CanonicalAddressUpdateRequest req = createSampleRequest("GM-2026-001", "REQ-001");

        Consent consent = Consent.builder()
                .id(1L)
                .consentId("CONSENT-GM-2026-01")
                .citizenReference("CIT-MH-998811")
                .requestingDepartment("REVENUE")
                .receivingDepartment("FOOD")
                .purpose("RATION_ADDRESS_UPDATE")
                .status("ACTIVE")
                .issuedAt(LocalDateTime.now().minusDays(1))
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();
        when(consentRepository.findByConsentId("CONSENT-GM-2026-01")).thenReturn(Optional.of(consent));
        when(applicationRepository.findByApplicationId("GM-2026-001")).thenReturn(Optional.empty());
        when(applicationRepository.save(any(Application.class))).thenAnswer(i -> {
            Application a = i.getArgument(0);
            a.setId(10L);
            return a;
        });

        ResponseEntity<CanonicalAddressUpdateResponse> resp = controller.processAddressUpdate(VALID_API_KEY, "2026-09-04T10:00:01.000Z", req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        CanonicalAddressUpdateResponse body = resp.getBody();

        assertEquals("GM-2026-001", body.getApplicationId());
        assertEquals("REQ-001", body.getCorrelationId());
        assertEquals("ACK-FOOD-GM-2026-001", body.getAcknowledgementId());
        assertEquals("FOOD", body.getTargetDepartment());
        assertEquals("SUCCESS", body.getStatus());
        assertEquals("a1b2c3d4e5f67890123456789abcdef0123456789abcdef0123456789abcdef0", body.getCanonicalRequestHash());
        assertEquals("f0e1d2c3b4a59876543210fedcba9876543210fedcba9876543210fedcba9876", body.getDocumentHash());
        assertEquals("VERIFIED", body.getHashStatus());

        // Monotonic Timestamps
        assertNotNull(body.getCreatedAt());
        assertNotNull(body.getSentAt());
        assertNotNull(body.getReceivedAt());
        assertNotNull(body.getValidatedAt());
        assertNotNull(body.getAcceptedAt());

        // Verify Application persistence
        verify(applicationRepository, atLeastOnce()).save(any(Application.class));
    }

    @Test
    void testAddressUpdate_IdempotentReplay_ReturnsExistingState() {
        CanonicalAddressUpdateRequest req = createSampleRequest("GM-2026-002", "REQ-002");

        Application completedApp = Application.builder()
                .id(2L)
                .applicationId("GM-2026-002")
                .correlationId("REQ-002")
                .currentStatus("COMPLETED")
                .acknowledgementId("ACK-FOOD-GM-2026-002")
                .canonicalRequestHash("a1b2c3d4e5f67890123456789abcdef0123456789abcdef0123456789abcdef0")
                .documentHash("f0e1d2c3b4a59876543210fedcba9876543210fedcba9876543210fedcba9876")
                .hashStatus("VERIFIED")
                .sentAt("2026-09-04T10:00:01.000Z")
                .receivedAt("2026-09-04T10:00:02.100Z")
                .completedAt("2026-09-04T10:05:00.000Z")
                .build();

        when(applicationRepository.findByApplicationId("GM-2026-002")).thenReturn(Optional.of(completedApp));

        ResponseEntity<CanonicalAddressUpdateResponse> resp = controller.processAddressUpdate(VALID_API_KEY, null, req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        CanonicalAddressUpdateResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals("SUCCESS", body.getStatus());
        assertTrue(body.getMessage().contains("already processed"));
        assertEquals("ACK-FOOD-GM-2026-002", body.getAcknowledgementId());
    }

    @Test
    void testOfficerWorkflow_StatusCallbackSentToGovMeshCore() {
        Application pendingApp = Application.builder()
                .id(5L)
                .applicationId("GM-2026-005")
                .correlationId("REQ-005")
                .currentStatus("PENDING")
                .rationCardNo("MH12-2026-005")
                .requestedAddress("New Address, Pune")
                .acknowledgementId("ACK-FOOD-GM-2026-005")
                .build();

        RationRecord rationRecord = RationRecord.builder()
                .id(1L)
                .rationCardNo("MH12-2026-005")
                .houseAddress("Old Address, Pune")
                .build();

        when(applicationRepository.findById(5L)).thenReturn(Optional.of(pendingApp));
        when(applicationRepository.save(any(Application.class))).thenAnswer(i -> i.getArgument(0));
        when(rationRecordRepository.findByRationCardNo("MH12-2026-005")).thenReturn(Optional.of(rationRecord));
        when(rationRecordRepository.save(any(RationRecord.class))).thenAnswer(i -> i.getArgument(0));

        // 1. Start Review -> PROCESSING
        applicationService.startReview(5L, testOfficer);
        assertEquals(1, callbackCount.get());
        assertEquals("PROCESSING", lastDispatchedStatus.get());

        // 2. Approve Application -> COMPLETED
        applicationService.approveApplication(5L, "Address verified by FSO", testOfficer);
        assertEquals(2, callbackCount.get());
        assertEquals("COMPLETED", lastDispatchedStatus.get());
    }
}
