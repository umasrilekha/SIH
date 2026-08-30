package com.govmesh.food.govmesh;

import com.govmesh.food.entity.Application;
import com.govmesh.food.entity.AuditLog;
import com.govmesh.food.entity.Consent;
import com.govmesh.food.entity.IntegrationTransaction;
import com.govmesh.food.entity.RationRecord;
import com.govmesh.food.govmesh.adapter.FoodDepartmentAdapter;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateRequest;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateResponse;
import com.govmesh.food.govmesh.mapper.FoodDepartmentSchemaMapper;
import com.govmesh.food.govmesh.router.IntegrationRouter;
import com.govmesh.food.govmesh.service.ConsentValidationService;
import com.govmesh.food.govmesh.service.GovMeshInteroperabilityService;
import com.govmesh.food.repository.ApplicationRepository;
import com.govmesh.food.repository.AuditLogRepository;
import com.govmesh.food.repository.ConsentRepository;
import com.govmesh.food.repository.IntegrationTransactionRepository;
import com.govmesh.food.repository.RationRecordRepository;
import com.govmesh.food.service.ApplicationService;
import com.govmesh.food.service.ConsentPolicyService;
import com.govmesh.food.soap.dto.UpdateRationAddress;
import com.govmesh.food.soap.endpoint.FoodDepartmentSoapEndpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GovMeshIntegrationTest {

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

    private ConsentPolicyService consentPolicyService;
    private ConsentValidationService consentValidationService;
    private FoodDepartmentSchemaMapper schemaMapper;
    private ApplicationService applicationService;
    private FoodDepartmentSoapEndpoint soapEndpoint;
    private FoodDepartmentAdapter foodAdapter;
    private IntegrationRouter integrationRouter;
    private GovMeshInteroperabilityService interoperabilityService;

    private Application sampleApp;
    private RationRecord sampleRecord;
    private Consent sampleConsent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        consentPolicyService = new ConsentPolicyService();
        consentValidationService = new ConsentValidationService(consentRepository, consentPolicyService);
        schemaMapper = new FoodDepartmentSchemaMapper(applicationRepository);
        applicationService = new ApplicationService(applicationRepository, rationRecordRepository, auditLogRepository, mock());
        soapEndpoint = new FoodDepartmentSoapEndpoint(applicationService);

        foodAdapter = new FoodDepartmentAdapter(schemaMapper) {
            @Override
            public CanonicalAddressUpdateResponse sendAddressUpdate(CanonicalAddressUpdateRequest canonicalRequest, String soapEndpointUrl) {
                UpdateRationAddress soapReq = schemaMapper.mapCanonicalToSoapRequest(canonicalRequest);
                try {
                    var soapResp = soapEndpoint.updateRationAddress(soapReq);
                    return CanonicalAddressUpdateResponse.builder()
                            .applicationId(soapResp.getApplicationId())
                            .status(soapResp.getStatus())
                            .message(soapResp.getMessage())
                            .correlationId(soapResp.getCorrelationId())
                            .targetDepartment("FOOD")
                            .build();
                } catch (Exception ex) {
                    return CanonicalAddressUpdateResponse.builder()
                            .applicationId(canonicalRequest.getApplicationId())
                            .status("FAILED")
                            .message(ex.getMessage())
                            .correlationId(canonicalRequest.getCorrelationId())
                            .targetDepartment("FOOD")
                            .errorCode("SOAP_FAULT")
                            .build();
                }
            }
        };

        integrationRouter = new IntegrationRouter(foodAdapter, "http://localhost:8080/ws");
        interoperabilityService = new GovMeshInteroperabilityService(integrationRouter, transactionRepository, auditLogRepository, consentValidationService);

        sampleRecord = RationRecord.builder()
                .id(1L)
                .rationCardNo("MH12-2026-000124")
                .holderName("Rajesh Kumar")
                .houseAddress("12, M.G. Road, Shivajinagar, Pune")
                .districtCode("DIST-PUN")
                .talukaCode("TAL-PUN-04")
                .verificationFlag(true)
                .updateStatus("ACTIVE")
                .build();

        sampleApp = Application.builder()
                .id(1L)
                .applicationId("GM-2026-000124")
                .citizenReference("CIT-MH-998811")
                .rationCardNo("MH12-2026-000124")
                .applicationType("ADDRESS_UPDATE")
                .currentStatus("PENDING")
                .sourceDepartment("REVENUE")
                .requestedAddress("44 Example Road, Shivajinagar, Pune - 411005")
                .build();

        sampleConsent = Consent.builder()
                .id(1L)
                .consentId("CONSENT-00124")
                .citizenReference("CIT-MH-998811")
                .requestingDepartment("REVENUE")
                .receivingDepartment("FOOD")
                .purpose("RATION_ADDRESS_UPDATE")
                .status("ACTIVE")
                .issuedAt(LocalDateTime.now().minusDays(1))
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();

        when(consentRepository.findByConsentId("CONSENT-00124")).thenReturn(Optional.of(sampleConsent));

        when(transactionRepository.save(any(IntegrationTransaction.class))).thenAnswer(i -> {
            IntegrationTransaction t = i.getArgument(0);
            if (t.getId() == null) t.setId(100L);
            return t;
        });
    }

    @Test
    void testEndToEndInteroperability_Success() {
        when(applicationRepository.findByApplicationId("GM-2026-000124")).thenReturn(Optional.of(sampleApp));
        when(rationRecordRepository.findByRationCardNo("MH12-2026-000124")).thenReturn(Optional.of(sampleRecord));
        when(applicationRepository.save(any(Application.class))).thenAnswer(i -> i.getArgument(0));
        when(rationRecordRepository.save(any(RationRecord.class))).thenAnswer(i -> i.getArgument(0));

        CanonicalAddressUpdateRequest request = new CanonicalAddressUpdateRequest(
                "GM-2026-000124",
                "REVENUE",
                "FOOD",
                "REQ-2026-000124",
                new CanonicalAddressUpdateRequest.CitizenInfo("CIT-MH-998811", "Rajesh Kumar",
                        new CanonicalAddressUpdateRequest.AddressInfo("44 Example Road, Shivajinagar, Pune - 411005", "DIST-PUN", "TAL-PUN-04")),
                new CanonicalAddressUpdateRequest.VerificationInfo("VALID", "REVENUE"),
                new CanonicalAddressUpdateRequest.ConsentInfo("CONSENT-00124")
        );

        CanonicalAddressUpdateResponse response = interoperabilityService.processInteroperabilityRequest(request);

        assertNotNull(response);
        assertEquals("GM-2026-000124", response.getApplicationId());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("REQ-2026-000124", response.getCorrelationId());
        assertEquals("44 Example Road, Shivajinagar, Pune - 411005", sampleRecord.getHouseAddress());
        assertEquals("APPROVED", sampleApp.getCurrentStatus());

        verify(transactionRepository, atLeast(2)).save(any(IntegrationTransaction.class));
        verify(auditLogRepository, atLeast(4)).save(any(AuditLog.class));
    }

    @Test
    void testEndToEndInteroperability_UnknownApplication_ReturnsFailed() {
        when(applicationRepository.findByApplicationId("GM-2026-999999")).thenReturn(Optional.empty());

        CanonicalAddressUpdateRequest request = new CanonicalAddressUpdateRequest(
                "GM-2026-999999",
                "REVENUE",
                "FOOD",
                "REQ-2026-999999",
                new CanonicalAddressUpdateRequest.CitizenInfo("CIT-MH-999999", "Unknown",
                        new CanonicalAddressUpdateRequest.AddressInfo("Unknown Address", "DIST-PUN", "TAL-PUN-04")),
                new CanonicalAddressUpdateRequest.VerificationInfo("VALID", "REVENUE"),
                new CanonicalAddressUpdateRequest.ConsentInfo("CONSENT-00124")
        );

        CanonicalAddressUpdateResponse response = interoperabilityService.processInteroperabilityRequest(request);

        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
        assertEquals("REQ-2026-999999", response.getCorrelationId());
    }

    @Test
    void testSchemaMapper_ExplicitMapping() {
        when(applicationRepository.findByApplicationId("GM-2026-000124")).thenReturn(Optional.of(sampleApp));

        CanonicalAddressUpdateRequest request = new CanonicalAddressUpdateRequest(
                "GM-2026-000124",
                "REVENUE",
                "FOOD",
                "REQ-2026-000124",
                new CanonicalAddressUpdateRequest.CitizenInfo("CIT-MH-998811", "Rajesh Kumar",
                        new CanonicalAddressUpdateRequest.AddressInfo("44 Example Road, Pune", "DIST-PUN", "TAL-PUN-04")),
                new CanonicalAddressUpdateRequest.VerificationInfo("VALID", "REVENUE"),
                new CanonicalAddressUpdateRequest.ConsentInfo("CONSENT-00124")
        );

        UpdateRationAddress soapReq = schemaMapper.mapCanonicalToSoapRequest(request);

        assertEquals("GM-2026-000124", soapReq.getApplicationId());
        assertEquals("Rajesh Kumar", soapReq.getCitizenName());
        assertEquals("44 Example Road, Pune", soapReq.getAddress());
        assertEquals("DIST-PUN", soapReq.getDistrictCode());
        assertEquals("TAL-PUN-04", soapReq.getTalukaCode());
        assertTrue(soapReq.isRevenueVerified());
        assertEquals("CONSENT-00124", soapReq.getConsentId());
        assertEquals("REQ-2026-000124", soapReq.getCorrelationId());
        assertEquals("MH12-2026-000124", soapReq.getRationCardNo());
    }
}
