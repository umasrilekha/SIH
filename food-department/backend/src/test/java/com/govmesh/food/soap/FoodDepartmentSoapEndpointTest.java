package com.govmesh.food.soap;

import com.govmesh.food.entity.Application;
import com.govmesh.food.entity.AuditLog;
import com.govmesh.food.entity.RationRecord;
import com.govmesh.food.repository.ApplicationRepository;
import com.govmesh.food.repository.AuditLogRepository;
import com.govmesh.food.repository.RationRecordRepository;
import com.govmesh.food.service.ApplicationService;
import com.govmesh.food.soap.dto.UpdateRationAddress;
import com.govmesh.food.soap.dto.UpdateRationAddressResponse;
import com.govmesh.food.soap.endpoint.FoodDepartmentSoapEndpoint;
import com.govmesh.food.soap.exception.SoapServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FoodDepartmentSoapEndpointTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private RationRecordRepository rationRecordRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private com.govmesh.food.repository.NotificationRepository notificationRepository;

    private com.govmesh.food.govmesh.service.FoodCallbackService foodCallbackService;
    private ApplicationService applicationService;
    private FoodDepartmentSoapEndpoint soapEndpoint;

    private Application sampleApp;
    private RationRecord sampleRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        foodCallbackService = new com.govmesh.food.govmesh.service.FoodCallbackService("http://localhost:9999/callback") {
            @Override
            public void dispatchStatusCallback(String applicationId, String correlationId, Integer requestVersion, String status, String acknowledgementId, String receivedAt, String validatedAt, String acceptedAt, String processingStartedAt, String completedAt, String canonicalRequestHash, String documentHash) {
                // no-op for tests
            }
        };
        applicationService = new ApplicationService(applicationRepository, rationRecordRepository, auditLogRepository, notificationRepository, foodCallbackService);
        soapEndpoint = new FoodDepartmentSoapEndpoint(applicationService);

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
    }

    @Test
    void testUpdateRationAddress_Success() {
        when(applicationRepository.findByApplicationId("GM-2026-000124")).thenReturn(Optional.of(sampleApp));
        when(rationRecordRepository.findByRationCardNo("MH12-2026-000124")).thenReturn(Optional.of(sampleRecord));
        when(applicationRepository.save(any(Application.class))).thenAnswer(i -> i.getArgument(0));
        when(rationRecordRepository.save(any(RationRecord.class))).thenAnswer(i -> i.getArgument(0));

        UpdateRationAddress request = new UpdateRationAddress();
        request.setApplicationId("GM-2026-000124");
        request.setCitizenName("Rajesh Kumar");
        request.setRationCardNo("MH12-2026-000124");
        request.setAddress("44 Example Road, Shivajinagar, Pune - 411005");
        request.setDistrictCode("DIST-PUN");
        request.setTalukaCode("TAL-PUN-04");
        request.setRevenueVerified(true);
        request.setConsentId("CONSENT-00124");
        request.setCorrelationId("REQ-2026-000124");

        UpdateRationAddressResponse response = soapEndpoint.updateRationAddress(request);

        assertNotNull(response);
        assertEquals("GM-2026-000124", response.getApplicationId());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("REQ-2026-000124", response.getCorrelationId());
        assertEquals("PENDING", sampleRecord.getUpdateStatus());
        assertEquals("PENDING", sampleApp.getCurrentStatus());

        verify(rationRecordRepository, times(1)).save(sampleRecord);
        verify(applicationRepository, times(1)).save(sampleApp);
        verify(auditLogRepository, atLeast(2)).save(any(AuditLog.class));
    }

    @Test
    void testUpdateRationAddress_ApplicationNotFound_ThrowsFault() {
        when(applicationRepository.findByApplicationId("GM-2026-999999")).thenReturn(Optional.empty());

        UpdateRationAddress request = new UpdateRationAddress();
        request.setApplicationId("GM-2026-999999");
        request.setRationCardNo("MH12-2026-000124");
        request.setAddress("44 Example Road");
        request.setRevenueVerified(true);
        request.setCorrelationId("REQ-2026-999999");

        SoapServiceException ex = assertThrows(SoapServiceException.class, () ->
                soapEndpoint.updateRationAddress(request)
        );

        assertEquals("APPLICATION_NOT_FOUND", ex.getFaultCode());
        assertTrue(ex.getMessage().contains("Application not found"));
    }

    @Test
    void testUpdateRationAddress_RevenueNotVerified_ThrowsFault() {
        UpdateRationAddress request = new UpdateRationAddress();
        request.setApplicationId("GM-2026-000124");
        request.setRevenueVerified(false);
        request.setCorrelationId("REQ-2026-000124");

        SoapServiceException ex = assertThrows(SoapServiceException.class, () ->
                soapEndpoint.updateRationAddress(request)
        );

        assertEquals("VALIDATION_FAILED", ex.getFaultCode());
        assertTrue(ex.getMessage().contains("Revenue verification flag is required and must be true"));
    }

    @Test
    void testUpdateRationAddress_RationCardMismatch_ThrowsFault() {
        when(applicationRepository.findByApplicationId("GM-2026-000124")).thenReturn(Optional.of(sampleApp));

        UpdateRationAddress request = new UpdateRationAddress();
        request.setApplicationId("GM-2026-000124");
        request.setRationCardNo("MH99-DIFFERENT-CARD");
        request.setAddress("44 Example Road");
        request.setRevenueVerified(true);
        request.setCorrelationId("REQ-2026-000124");

        SoapServiceException ex = assertThrows(SoapServiceException.class, () ->
                soapEndpoint.updateRationAddress(request)
        );

        assertEquals("VALIDATION_FAILED", ex.getFaultCode());
        assertTrue(ex.getMessage().contains("does not match application record"));
    }
}
