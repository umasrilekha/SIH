package com.govmesh.food.service;

import com.govmesh.food.dto.ApplicationDTOs.*;
import com.govmesh.food.dto.RationRecordDTOs.RationRecordDTO;
import com.govmesh.food.entity.Application;
import com.govmesh.food.entity.AuditLog;
import com.govmesh.food.entity.Notification;
import com.govmesh.food.entity.RationRecord;
import com.govmesh.food.exception.ResourceNotFoundException;
import com.govmesh.food.exception.UnauthorizedException;
import com.govmesh.food.govmesh.service.FoodCallbackService;
import com.govmesh.food.repository.ApplicationRepository;
import com.govmesh.food.repository.AuditLogRepository;
import com.govmesh.food.repository.NotificationRepository;
import com.govmesh.food.repository.RationRecordRepository;
import com.govmesh.food.security.UserPrincipal;
import com.govmesh.food.soap.dto.SoapResultDTO;
import com.govmesh.food.soap.dto.UpdateRationAddressCommand;
import com.govmesh.food.soap.exception.SoapServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final RationRecordRepository rationRecordRepository;
    private final AuditLogRepository auditLogRepository;
    private final NotificationRepository notificationRepository;
    private final FoodCallbackService foodCallbackService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              RationRecordRepository rationRecordRepository,
                              AuditLogRepository auditLogRepository,
                              NotificationRepository notificationRepository,
                              FoodCallbackService foodCallbackService) {
        this.applicationRepository = applicationRepository;
        this.rationRecordRepository = rationRecordRepository;
        this.auditLogRepository = auditLogRepository;
        this.notificationRepository = notificationRepository;
        this.foodCallbackService = foodCallbackService;
    }

    public List<ApplicationDTO> getApplications(String query, String status, String type) {
        List<Application> apps = applicationRepository.filterApplications(query, status, type);
        return apps.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ApplicationDetailDTO getApplicationById(Long id) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));

        RationRecordDTO rationRecordDTO = null;
        if (app.getRationCardNo() != null) {
            RationRecord record = rationRecordRepository.findByRationCardNo(app.getRationCardNo()).orElse(null);
            if (record != null) {
                rationRecordDTO = mapToRationDTO(record);
            }
        }

        return ApplicationDetailDTO.builder()
                .application(mapToDTO(app))
                .currentRationRecord(rationRecordDTO)
                .build();
    }

    public ApplicationDetailDTO getApplicationByApplicationId(String applicationId) {
        Application app = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with Application ID: " + applicationId));

        RationRecordDTO rationRecordDTO = null;
        if (app.getRationCardNo() != null) {
            RationRecord record = rationRecordRepository.findByRationCardNo(app.getRationCardNo()).orElse(null);
            if (record != null) {
                rationRecordDTO = mapToRationDTO(record);
            }
        }

        return ApplicationDetailDTO.builder()
                .application(mapToDTO(app))
                .currentRationRecord(rationRecordDTO)
                .build();
    }

    public ApplicationDetailDTO startReview(Long id, UserPrincipal currentUser) {
        verifyNotAuditor(currentUser);

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));

        if ("APPROVED".equalsIgnoreCase(app.getCurrentStatus()) || "REJECTED".equalsIgnoreCase(app.getCurrentStatus()) || "COMPLETED".equalsIgnoreCase(app.getCurrentStatus())) {
            throw new IllegalStateException("Application " + app.getApplicationId() + " has already been finalized and cannot be reviewed.");
        }

        String nowIso = Instant.now().toString();
        app.setCurrentStatus("UNDER_REVIEW");
        app.setProcessingStartedAt(nowIso);
        app.setReviewedByOfficer(currentUser != null ? (currentUser.getEmployeeId() != null ? currentUser.getEmployeeId() : currentUser.getUsername()) : "OFFICER-FOOD");
        Application saved = applicationRepository.save(app);

        // Audit Log
        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(app.getApplicationId())
                .officerId(currentUser != null ? currentUser.getId() : null)
                .action("APPLICATION_REVIEW_STARTED")
                .result("SUCCESS")
                .description("Officer " + (currentUser != null ? currentUser.getUsername() : "system") + " started review for application " + app.getApplicationId())
                .build());

        // Dispatch status callback to GovMesh Core
        foodCallbackService.dispatchStatusCallback(
                app.getApplicationId(),
                app.getCorrelationId(),
                app.getRequestVersion(),
                "PROCESSING",
                app.getAcknowledgementId(),
                app.getReceivedAt(),
                app.getValidatedAt(),
                app.getAcceptedAt(),
                app.getProcessingStartedAt(),
                app.getCompletedAt(),
                app.getCanonicalRequestHash(),
                app.getDocumentHash()
        );

        return getApplicationById(saved.getId());
    }

    @Transactional
    public ApplicationDetailDTO approveApplication(Long id, String comments, UserPrincipal currentUser) {
        verifySeniorOrAdmin(currentUser);

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));

        if ("APPROVED".equalsIgnoreCase(app.getCurrentStatus()) || "COMPLETED".equalsIgnoreCase(app.getCurrentStatus())) {
            throw new IllegalStateException("Application " + app.getApplicationId() + " has already been approved.");
        }
        if ("REJECTED".equalsIgnoreCase(app.getCurrentStatus())) {
            throw new IllegalStateException("Application " + app.getApplicationId() + " was rejected and cannot be approved directly.");
        }

        // Transactional update of RationRecord if ADDRESS_UPDATE
        if (app.getRationCardNo() != null) {
            RationRecord record = rationRecordRepository.findByRationCardNo(app.getRationCardNo())
                    .orElse(null);

            if (record != null && app.getRequestedAddress() != null && !app.getRequestedAddress().isBlank()) {
                record.setHouseAddress(app.getRequestedAddress());
                record.setUpdateStatus("UPDATED");
                rationRecordRepository.save(record);

                auditLogRepository.save(AuditLog.builder()
                        .timestamp(LocalDateTime.now())
                        .applicationId(app.getApplicationId())
                        .officerId(currentUser != null ? currentUser.getId() : null)
                        .action("RATION_RECORD_UPDATED")
                        .result("SUCCESS")
                        .description("Updated house address for Ration Card " + record.getRationCardNo() + " to: " + app.getRequestedAddress())
                        .build());
            }
        }

        String nowIso = Instant.now().toString();
        app.setCurrentStatus("APPROVED");
        app.setCompletedAt(nowIso);
        app.setOfficerComments(comments != null ? comments : "Address update approved after departmental scrutiny.");
        app.setReviewedByOfficer(currentUser != null ? (currentUser.getEmployeeId() != null ? currentUser.getEmployeeId() : currentUser.getUsername()) : "OFFICER-FOOD");
        Application saved = applicationRepository.save(app);

        // Audit Log
        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(app.getApplicationId())
                .officerId(currentUser != null ? currentUser.getId() : null)
                .action("APPLICATION_APPROVED")
                .result("SUCCESS")
                .description("Application " + app.getApplicationId() + " approved by officer " + (currentUser != null ? currentUser.getUsername() : "system"))
                .build());

        // Notification
        if (currentUser != null && currentUser.getId() != null) {
            notificationRepository.save(Notification.builder()
                    .recipientUserId(currentUser.getId())
                    .title("Application Approved")
                    .message("Address update request " + app.getApplicationId() + " has been approved successfully.")
                    .type("REQUEST")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        // Dispatch status callback to GovMesh Core
        foodCallbackService.dispatchStatusCallback(
                app.getApplicationId(),
                app.getCorrelationId(),
                app.getRequestVersion(),
                "COMPLETED",
                app.getAcknowledgementId(),
                app.getReceivedAt(),
                app.getValidatedAt(),
                app.getAcceptedAt(),
                app.getProcessingStartedAt(),
                app.getCompletedAt(),
                app.getCanonicalRequestHash(),
                app.getDocumentHash()
        );

        return getApplicationById(saved.getId());
    }

    @Transactional
    public ApplicationDetailDTO rejectApplication(Long id, String reason, UserPrincipal currentUser) {
        verifySeniorOrAdmin(currentUser);

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Rejection reason is mandatory.");
        }

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));

        if ("APPROVED".equalsIgnoreCase(app.getCurrentStatus()) || "COMPLETED".equalsIgnoreCase(app.getCurrentStatus()) || "REJECTED".equalsIgnoreCase(app.getCurrentStatus())) {
            throw new IllegalStateException("Application " + app.getApplicationId() + " is already in terminal state " + app.getCurrentStatus());
        }

        String nowIso = Instant.now().toString();
        app.setCurrentStatus("REJECTED");
        app.setCompletedAt(nowIso);
        app.setOfficerComments(reason);
        app.setReviewedByOfficer(currentUser != null ? (currentUser.getEmployeeId() != null ? currentUser.getEmployeeId() : currentUser.getUsername()) : "OFFICER-FOOD");
        Application saved = applicationRepository.save(app);

        // Audit Log (Ration Record remains UNCHANGED)
        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(app.getApplicationId())
                .officerId(currentUser != null ? currentUser.getId() : null)
                .action("APPLICATION_REJECTED")
                .result("SUCCESS")
                .description("Application " + app.getApplicationId() + " rejected. Reason: " + reason)
                .build());

        // Notification
        if (currentUser != null && currentUser.getId() != null) {
            notificationRepository.save(Notification.builder()
                    .recipientUserId(currentUser.getId())
                    .title("Application Rejected")
                    .message("Address update request " + app.getApplicationId() + " rejected. Reason: " + reason)
                    .type("ALERT")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        // Dispatch status callback to GovMesh Core
        foodCallbackService.dispatchStatusCallback(
                app.getApplicationId(),
                app.getCorrelationId(),
                app.getRequestVersion(),
                "REJECTED",
                app.getAcknowledgementId(),
                app.getReceivedAt(),
                app.getValidatedAt(),
                app.getAcceptedAt(),
                app.getProcessingStartedAt(),
                app.getCompletedAt(),
                app.getCanonicalRequestHash(),
                app.getDocumentHash()
        );

        return getApplicationById(saved.getId());
    }

    public ApplicationDetailDTO requestInformation(Long id, String comments, UserPrincipal currentUser) {
        verifyNotAuditor(currentUser);

        if (comments == null || comments.isBlank()) {
            throw new IllegalArgumentException("Information request details cannot be empty.");
        }

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));

        if ("APPROVED".equalsIgnoreCase(app.getCurrentStatus()) || "COMPLETED".equalsIgnoreCase(app.getCurrentStatus()) || "REJECTED".equalsIgnoreCase(app.getCurrentStatus())) {
            throw new IllegalStateException("Cannot request information for completed/rejected application " + app.getApplicationId());
        }

        app.setCurrentStatus("INFORMATION_REQUIRED");
        app.setOfficerComments(comments);
        app.setReviewedByOfficer(currentUser != null ? (currentUser.getEmployeeId() != null ? currentUser.getEmployeeId() : currentUser.getUsername()) : "OFFICER-FOOD");
        Application saved = applicationRepository.save(app);

        // Audit Log
        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(app.getApplicationId())
                .officerId(currentUser != null ? currentUser.getId() : null)
                .action("INFORMATION_REQUESTED")
                .result("SUCCESS")
                .description("Requested additional information for " + app.getApplicationId() + ": " + comments)
                .build());

        // Notification
        if (currentUser != null && currentUser.getId() != null) {
            notificationRepository.save(Notification.builder()
                    .recipientUserId(currentUser.getId())
                    .title("Information Requested")
                    .message("Additional details requested for application " + app.getApplicationId())
                    .type("REQUEST")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        // Dispatch status callback to GovMesh Core
        foodCallbackService.dispatchStatusCallback(
                app.getApplicationId(),
                app.getCorrelationId(),
                app.getRequestVersion(),
                "ACTION_REQUIRED",
                app.getAcknowledgementId(),
                app.getReceivedAt(),
                app.getValidatedAt(),
                app.getAcceptedAt(),
                app.getProcessingStartedAt(),
                app.getCompletedAt(),
                app.getCanonicalRequestHash(),
                app.getDocumentHash()
        );

        return getApplicationById(saved.getId());
    }

    @Transactional
    public SoapResultDTO processSoapAddressUpdate(UpdateRationAddressCommand command) {
        String corrId = command.getCorrelationId() != null ? command.getCorrelationId() : "N/A";

        // Audit Log: SOAP Request Received
        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(command.getApplicationId())
                .officerId(null)
                .action("SOAP_REQUEST_RECEIVED")
                .result("SUCCESS")
                .description("UpdateRationAddress SOAP request received for App: " + command.getApplicationId() + " (CorrelationId: " + corrId + ")")
                .build());

        // Revenue Verification Check
        if (command.getRevenueVerified() == null || !command.getRevenueVerified()) {
            auditLogRepository.save(AuditLog.builder()
                    .timestamp(LocalDateTime.now())
                    .applicationId(command.getApplicationId())
                    .officerId(null)
                    .action("SOAP_PROCESSING_FAILED")
                    .result("FAILED")
                    .description("SOAP update rejected: RevenueVerified must be true. (CorrelationId: " + corrId + ")")
                    .build());
            throw new SoapServiceException("VALIDATION_FAILED", "Revenue verification flag is required and must be true.");
        }

        // Application Existence Check (Auto-create for dynamic GovMesh interoperability ingress)
        Application app = applicationRepository.findByApplicationId(command.getApplicationId()).orElse(null);
        if (app == null) {
            String rationNo = command.getRationCardNo() != null ? command.getRationCardNo() : "MH12-2026-" + command.getApplicationId().replace("GM-2026-", "");
            app = Application.builder()
                    .applicationId(command.getApplicationId())
                    .correlationId(corrId)
                    .requestVersion(1)
                    .citizenReference("CIT-" + command.getApplicationId())
                    .rationCardNo(rationNo)
                    .applicationType("ADDRESS_UPDATE")
                    .currentStatus("PENDING")
                    .sourceDepartment("REVENUE")
                    .requestedAddress(command.getAddress())
                    .consentId(command.getConsentId())
                    .acknowledgementId("ACK-FOOD-" + command.getApplicationId())
                    .sentAt(Instant.now().toString())
                    .receivedAt(Instant.now().toString())
                    .validatedAt(Instant.now().toString())
                    .acceptedAt(Instant.now().toString())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            app = applicationRepository.save(app);
        }

        // Master Ration Record Lookup (Auto-create if not existing)
        RationRecord record = rationRecordRepository.findByRationCardNo(app.getRationCardNo()).orElse(null);
        if (record == null) {
            record = RationRecord.builder()
                    .rationCardNo(app.getRationCardNo())
                    .holderName(command.getCitizenName() != null ? command.getCitizenName() : "Citizen " + app.getApplicationId())
                    .houseAddress("Flat 101, Old Government Quarters, Revenue Colony, Pune")
                    .talukaCode(command.getTalukaCode() != null ? command.getTalukaCode() : "TAL-PUN-04")
                    .districtCode(command.getDistrictCode() != null ? command.getDistrictCode() : "DIST-PUN")
                    .verificationFlag(true)
                    .updateStatus("PENDING")
                    .createdAt(LocalDateTime.now())
                    .build();
            record = rationRecordRepository.save(record);
        }

        // Address Field Check
        if (command.getAddress() == null || command.getAddress().isBlank()) {
            throw new SoapServiceException("VALIDATION_FAILED", "Address element is required and cannot be blank.");
        }

        // Perform Transactional Update of Master Ration Record
        record.setHouseAddress(command.getAddress());
        record.setUpdateStatus("UPDATED");
        rationRecordRepository.save(record);

        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(app.getApplicationId())
                .officerId(null)
                .action("RATION_RECORD_UPDATED")
                .result("SUCCESS")
                .description("Updated house address for Ration Card " + record.getRationCardNo() + " to: " + command.getAddress() + " via SOAP Web Service")
                .build());

        // Perform Update of Application Record
        String nowIso = Instant.now().toString();
        app.setCurrentStatus("APPROVED");
        app.setCompletedAt(nowIso);
        app.setRequestedAddress(command.getAddress());
        app.setOfficerComments("Approved via GovMesh SOAP Interoperability Interface (Consent: " + command.getConsentId() + ", Correlation: " + corrId + ")");
        app.setReviewedByOfficer("SOAP_INTEROP_GATEWAY");
        applicationRepository.save(app);

        // Audit Log: SOAP Processing Success
        auditLogRepository.save(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .applicationId(app.getApplicationId())
                .officerId(null)
                .action("SOAP_PROCESSING_SUCCESS")
                .result("SUCCESS")
                .description("Address update request " + app.getApplicationId() + " processed successfully via SOAP (CorrelationId: " + corrId + ")")
                .build());

        return SoapResultDTO.builder()
                .applicationId(app.getApplicationId())
                .status("SUCCESS")
                .message("Ration address update processed successfully")
                .correlationId(corrId)
                .build();
    }

    public List<AuditLog> getApplicationHistory(Long id) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));

        return auditLogRepository.filterAuditLogs(app.getApplicationId(), null, null);
    }

    private void verifyNotAuditor(UserPrincipal currentUser) {
        if (currentUser != null && "AUDITOR".equalsIgnoreCase(currentUser.getRole())) {
            throw new UnauthorizedException("Auditor role has read-only privileges and cannot perform application action operations.");
        }
    }

    private void verifySeniorOrAdmin(UserPrincipal currentUser) {
        if (currentUser != null && !"SENIOR_OFFICER".equalsIgnoreCase(currentUser.getRole()) && !"DEPARTMENT_ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new UnauthorizedException("Only Senior Officers or Department Admins have authorization to approve or reject applications.");
        }
    }

    private ApplicationDTO mapToDTO(Application app) {
        return ApplicationDTO.builder()
                .id(app.getId())
                .applicationId(app.getApplicationId())
                .correlationId(app.getCorrelationId())
                .requestVersion(app.getRequestVersion())
                .citizenReference(app.getCitizenReference())
                .rationCardNo(app.getRationCardNo())
                .applicationType(app.getApplicationType())
                .currentStatus(app.getCurrentStatus())
                .sourceDepartment(app.getSourceDepartment())
                .requestedAddress(app.getRequestedAddress())
                .officerComments(app.getOfficerComments())
                .reviewedByOfficer(app.getReviewedByOfficer())
                .canonicalRequestHash(app.getCanonicalRequestHash())
                .documentHash(app.getDocumentHash())
                .hashStatus(app.getHashStatus())
                .documentId(app.getDocumentId())
                .documentName(app.getDocumentName())
                .documentType(app.getDocumentType())
                .documentSize(app.getDocumentSize())
                .consentId(app.getConsentId())
                .acknowledgementId(app.getAcknowledgementId())
                .sentAt(app.getSentAt())
                .receivedAt(app.getReceivedAt())
                .validatedAt(app.getValidatedAt())
                .acceptedAt(app.getAcceptedAt())
                .processingStartedAt(app.getProcessingStartedAt())
                .completedAt(app.getCompletedAt())
                .rawSourceJson(app.getRawSourceJson())
                .createdAt(app.getCreatedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }

    private RationRecordDTO mapToRationDTO(RationRecord record) {
        return RationRecordDTO.builder()
                .id(record.getId())
                .rationCardNo(record.getRationCardNo())
                .holderName(record.getHolderName())
                .houseAddress(record.getHouseAddress())
                .talukaCode(record.getTalukaCode())
                .districtCode(record.getDistrictCode())
                .verificationFlag(record.getVerificationFlag())
                .updateStatus(record.getUpdateStatus())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}
