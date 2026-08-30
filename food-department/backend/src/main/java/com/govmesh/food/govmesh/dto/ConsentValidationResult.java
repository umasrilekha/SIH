package com.govmesh.food.govmesh.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ConsentValidationResult {
    private String status; // ALLOWED / BLOCKED
    private String reason; // CONSENT_VALIDATED, CONSENT_NOT_FOUND, CONSENT_PARTY_MISMATCH, PURPOSE_NOT_ALLOWED, CONSENT_INACTIVE, CONSENT_EXPIRED, FIELD_NOT_PERMITTED
    private String consentId;
    private String purpose;
    private List<String> requestedFields;
    private List<String> allowedFields;
    private LocalDateTime timestamp;

    public ConsentValidationResult() {}

    public ConsentValidationResult(String status, String reason, String consentId, String purpose, List<String> requestedFields, List<String> allowedFields, LocalDateTime timestamp) {
        this.status = status;
        this.reason = reason;
        this.consentId = consentId;
        this.purpose = purpose;
        this.requestedFields = requestedFields;
        this.allowedFields = allowedFields;
        this.timestamp = timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getConsentId() { return consentId; }
    public void setConsentId(String consentId) { this.consentId = consentId; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public List<String> getRequestedFields() { return requestedFields; }
    public void setRequestedFields(List<String> requestedFields) { this.requestedFields = requestedFields; }

    public List<String> getAllowedFields() { return allowedFields; }
    public void setAllowedFields(List<String> allowedFields) { this.allowedFields = allowedFields; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class Builder {
        private String status;
        private String reason;
        private String consentId;
        private String purpose;
        private List<String> requestedFields;
        private List<String> allowedFields;
        private LocalDateTime timestamp;

        public Builder status(String status) { this.status = status; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }
        public Builder consentId(String consentId) { this.consentId = consentId; return this; }
        public Builder purpose(String purpose) { this.purpose = purpose; return this; }
        public Builder requestedFields(List<String> requestedFields) { this.requestedFields = requestedFields; return this; }
        public Builder allowedFields(List<String> allowedFields) { this.allowedFields = allowedFields; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ConsentValidationResult build() {
            return new ConsentValidationResult(status, reason, consentId, purpose, requestedFields, allowedFields, timestamp);
        }
    }
}
