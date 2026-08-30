package com.govmesh.food.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "application_id", length = 50)
    private String applicationId;

    @Column(name = "officer_id")
    private Long officerId;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(nullable = false, length = 50)
    private String result;

    @Column(columnDefinition = "TEXT")
    private String description;

    public AuditLog() {}

    public AuditLog(Long id, LocalDateTime timestamp, String applicationId, Long officerId, String action, String result, String description) {
        this.id = id;
        this.timestamp = timestamp;
        this.applicationId = applicationId;
        this.officerId = officerId;
        this.action = action;
        this.result = result;
        this.description = description;
    }

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public Long getOfficerId() { return officerId; }
    public void setOfficerId(Long officerId) { this.officerId = officerId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static AuditLogBuilder builder() { return new AuditLogBuilder(); }

    public static class AuditLogBuilder {
        private Long id;
        private LocalDateTime timestamp;
        private String applicationId;
        private Long officerId;
        private String action;
        private String result;
        private String description;

        public AuditLogBuilder id(Long id) { this.id = id; return this; }
        public AuditLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AuditLogBuilder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
        public AuditLogBuilder officerId(Long officerId) { this.officerId = officerId; return this; }
        public AuditLogBuilder action(String action) { this.action = action; return this; }
        public AuditLogBuilder result(String result) { this.result = result; return this; }
        public AuditLogBuilder description(String description) { this.description = description; return this; }

        public AuditLog build() {
            return new AuditLog(id, timestamp, applicationId, officerId, action, result, description);
        }
    }
}
