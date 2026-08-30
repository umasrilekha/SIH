package com.govmesh.food.dto;

import java.time.LocalDateTime;

public class AuditLogDTO {
    private Long id;
    private LocalDateTime timestamp;
    private String applicationId;
    private Long officerId;
    private String officerName;
    private String action;
    private String result;
    private String description;

    public AuditLogDTO() {}
    public AuditLogDTO(Long id, LocalDateTime timestamp, String applicationId, Long officerId, String officerName, String action, String result, String description) {
        this.id = id;
        this.timestamp = timestamp;
        this.applicationId = applicationId;
        this.officerId = officerId;
        this.officerName = officerName;
        this.action = action;
        this.result = result;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public Long getOfficerId() { return officerId; }
    public void setOfficerId(Long officerId) { this.officerId = officerId; }

    public String getOfficerName() { return officerName; }
    public void setOfficerName(String officerName) { this.officerName = officerName; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static AuditLogDTOBuilder builder() { return new AuditLogDTOBuilder(); }

    public static class AuditLogDTOBuilder {
        private Long id;
        private LocalDateTime timestamp;
        private String applicationId;
        private Long officerId;
        private String officerName;
        private String action;
        private String result;
        private String description;

        public AuditLogDTOBuilder id(Long id) { this.id = id; return this; }
        public AuditLogDTOBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AuditLogDTOBuilder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
        public AuditLogDTOBuilder officerId(Long officerId) { this.officerId = officerId; return this; }
        public AuditLogDTOBuilder officerName(String officerName) { this.officerName = officerName; return this; }
        public AuditLogDTOBuilder action(String action) { this.action = action; return this; }
        public AuditLogDTOBuilder result(String result) { this.result = result; return this; }
        public AuditLogDTOBuilder description(String description) { this.description = description; return this; }

        public AuditLogDTO build() {
            return new AuditLogDTO(id, timestamp, applicationId, officerId, officerName, action, result, description);
        }
    }
}
