package com.govmesh.food.service;

import com.govmesh.food.dto.ApplicationDTOs.ApplicationDTO;
import com.govmesh.food.dto.DashboardDTOs.*;
import com.govmesh.food.entity.Application;
import com.govmesh.food.entity.AuditLog;
import com.govmesh.food.repository.ApplicationRepository;
import com.govmesh.food.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ApplicationRepository applicationRepository;
    private final AuditLogRepository auditLogRepository;

    public DashboardService(ApplicationRepository applicationRepository, AuditLogRepository auditLogRepository) {
        this.applicationRepository = applicationRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public SummaryDTO getDashboardSummary() {
        long total = applicationRepository.count();
        long pending = applicationRepository.countByCurrentStatus("PENDING");
        long processing = applicationRepository.countByCurrentStatus("PROCESSING");
        long completed = applicationRepository.countByCurrentStatus("COMPLETED");
        long rejected = applicationRepository.countByCurrentStatus("REJECTED");
        long failed = applicationRepository.countByCurrentStatus("FAILED");

        List<ServiceStatusItem> serviceStatuses = Arrays.asList(
                ServiceStatusItem.builder()
                        .name("Department Database")
                        .status("OPERATIONAL")
                        .isConnected(true)
                        .note("PostgreSQL Connection Active")
                        .build(),
                ServiceStatusItem.builder()
                        .name("Officer Authentication")
                        .status("OPERATIONAL")
                        .isConnected(true)
                        .note("JWT & RBAC Security Layer Active")
                        .build(),
                ServiceStatusItem.builder()
                        .name("Application Processing")
                        .status("OPERATIONAL")
                        .isConnected(true)
                        .note("Internal Request Queue Active")
                        .build(),
                ServiceStatusItem.builder()
                        .name("SOAP Service")
                        .status("NOT_CONFIGURED")
                        .isConnected(false)
                        .note("SOAP/XML Endpoint Not Configured in Phase 1")
                        .build(),
                ServiceStatusItem.builder()
                        .name("GovMesh Interoperability")
                        .status("NOT_CONFIGURED")
                        .isConnected(false)
                        .note("Orchestrator Gateway Not Connected in Phase 1")
                        .build()
        );

        return SummaryDTO.builder()
                .totalIncomingRequests(total)
                .pendingCount(pending)
                .processingCount(processing)
                .completedCount(completed)
                .rejectedCount(rejected)
                .failedCount(failed)
                .serviceStatus(serviceStatuses)
                .build();
    }

    public List<ApplicationDTO> getRecentApplications() {
        return applicationRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(this::mapToApplicationDTO)
                .collect(Collectors.toList());
    }

    public List<RecentActivityDTO> getRecentActivities() {
        return auditLogRepository.findTop10ByOrderByTimestampDesc().stream()
                .map(this::mapToActivityDTO)
                .collect(Collectors.toList());
    }

    private ApplicationDTO mapToApplicationDTO(Application app) {
        return ApplicationDTO.builder()
                .id(app.getId())
                .applicationId(app.getApplicationId())
                .citizenReference(app.getCitizenReference())
                .rationCardNo(app.getRationCardNo())
                .applicationType(app.getApplicationType())
                .currentStatus(app.getCurrentStatus())
                .sourceDepartment(app.getSourceDepartment())
                .createdAt(app.getCreatedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }

    private RecentActivityDTO mapToActivityDTO(AuditLog log) {
        return RecentActivityDTO.builder()
                .id(log.getId())
                .timeAgo(formatTimeAgo(log.getTimestamp()))
                .timestamp(log.getTimestamp().toString())
                .description(log.getDescription())
                .action(log.getAction())
                .result(log.getResult())
                .officerName("Officer ID: " + (log.getOfficerId() != null ? log.getOfficerId() : "System"))
                .build();
    }

    private String formatTimeAgo(LocalDateTime timestamp) {
        if (timestamp == null) return "Just now";
        Duration duration = Duration.between(timestamp, LocalDateTime.now());
        long minutes = Math.abs(duration.toMinutes());
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " min ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + " hrs ago";
        long days = hours / 24;
        return days + " days ago";
    }
}
