package com.govmesh.food.service;

import com.govmesh.food.dto.AuditLogDTO;
import com.govmesh.food.entity.AuditLog;
import com.govmesh.food.repository.AuditLogRepository;
import com.govmesh.food.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public List<AuditLogDTO> getAuditLogs(String appId, String action, String result) {
        List<AuditLog> logs = auditLogRepository.filterAuditLogs(appId, action, result);
        return logs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private AuditLogDTO mapToDTO(AuditLog log) {
        String officerName = "System";
        if (log.getOfficerId() != null) {
            officerName = userRepository.findById(log.getOfficerId())
                    .map(u -> u.getFullName() + " (" + u.getRole() + ")")
                    .orElse("Officer ID: " + log.getOfficerId());
        }

        return AuditLogDTO.builder()
                .id(log.getId())
                .timestamp(log.getTimestamp())
                .applicationId(log.getApplicationId())
                .officerId(log.getOfficerId())
                .officerName(officerName)
                .action(log.getAction())
                .result(log.getResult())
                .description(log.getDescription())
                .build();
    }
}
