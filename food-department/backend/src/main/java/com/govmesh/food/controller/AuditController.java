package com.govmesh.food.controller;

import com.govmesh.food.dto.AuditLogDTO;
import com.govmesh.food.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditController {

    private final AuditLogService auditLogService;

    public AuditController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogDTO>> getAuditLogs(
            @RequestParam(required = false) String appId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String result) {
        return ResponseEntity.ok(auditLogService.getAuditLogs(appId, action, result));
    }
}
