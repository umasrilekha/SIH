package com.govmesh.food.service;

import com.govmesh.food.dto.ApplicationDTOs.*;
import com.govmesh.food.entity.Application;
import com.govmesh.food.entity.AuditLog;
import com.govmesh.food.entity.RationRecord;
import com.govmesh.food.entity.User;
import com.govmesh.food.exception.UnauthorizedException;
import com.govmesh.food.repository.ApplicationRepository;
import com.govmesh.food.repository.AuditLogRepository;
import com.govmesh.food.repository.NotificationRepository;
import com.govmesh.food.repository.RationRecordRepository;
import com.govmesh.food.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private RationRecordRepository rationRecordRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private UserPrincipal seniorOfficer;
    private UserPrincipal auditor;
    private Application sampleApplication;
    private RationRecord sampleRationRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User seniorUser = User.builder()
                .id(1L)
                .username("senior.officer")
                .role("SENIOR_OFFICER")
                .employeeId("FOOD-EMP-002")
                .build();
        seniorOfficer = UserPrincipal.create(seniorUser);

        User auditorUser = User.builder()
                .id(2L)
                .username("auditor")
                .role("AUDITOR")
                .employeeId("AUD-EMP-101")
                .build();
        auditor = UserPrincipal.create(auditorUser);

        sampleRationRecord = RationRecord.builder()
                .id(100L)
                .rationCardNo("MH12-2026-000124")
                .holderName("Rajesh Kumar")
                .houseAddress("12, M.G. Road, Shivajinagar, Pune")
                .talukaCode("TAL-PUN-04")
                .districtCode("DIST-PUN")
                .verificationFlag(true)
                .updateStatus("ACTIVE")
                .build();

        sampleApplication = Application.builder()
                .id(10L)
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
    void testApproveApplication_Success() {
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(sampleApplication));
        when(rationRecordRepository.findByRationCardNo("MH12-2026-000124")).thenReturn(Optional.of(sampleRationRecord));
        when(applicationRepository.save(any(Application.class))).thenAnswer(i -> i.getArgument(0));

        ApplicationDetailDTO result = applicationService.approveApplication(10L, "Approved address change", seniorOfficer);

        assertNotNull(result);
        assertEquals("APPROVED", result.getApplication().getCurrentStatus());
        assertEquals("44 Example Road, Shivajinagar, Pune - 411005", sampleRationRecord.getHouseAddress());
        assertEquals("UPDATED", sampleRationRecord.getUpdateStatus());

        verify(rationRecordRepository, times(1)).save(sampleRationRecord);
        verify(applicationRepository, times(1)).save(sampleApplication);
        verify(auditLogRepository, times(2)).save(any(AuditLog.class));
    }

    @Test
    void testApproveApplication_AlreadyApproved_ThrowsConflict() {
        sampleApplication.setCurrentStatus("APPROVED");
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(sampleApplication));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                applicationService.approveApplication(10L, "Re-approval attempt", seniorOfficer)
        );

        assertTrue(ex.getMessage().contains("already been approved"));
    }

    @Test
    void testApproveApplication_AuditorRole_ThrowsUnauthorized() {
        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                applicationService.approveApplication(10L, "Auditor approval", auditor)
        );

        assertTrue(ex.getMessage().contains("Only Senior Officers or Department Admins"));
    }

    @Test
    void testRejectApplication_MissingReason_ThrowsBadRequest() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                applicationService.rejectApplication(10L, " ", seniorOfficer)
        );

        assertTrue(ex.getMessage().contains("Rejection reason is mandatory"));
    }

    @Test
    void testRejectApplication_Success() {
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(sampleApplication));
        when(applicationRepository.save(any(Application.class))).thenAnswer(i -> i.getArgument(0));

        ApplicationDetailDTO result = applicationService.rejectApplication(10L, "Address verification document unreadable.", seniorOfficer);

        assertEquals("REJECTED", result.getApplication().getCurrentStatus());
        assertEquals("Address verification document unreadable.", result.getApplication().getOfficerComments());
        verify(rationRecordRepository, never()).save(any());
    }
}
