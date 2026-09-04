package com.govmesh.food.govmesh.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CanonicalAddressUpdateResponse {
    private String applicationId;
    private String status;
    private String message;
    private String correlationId;
    private Integer requestVersion;
    private String targetDepartment;
    private String acknowledgementId;
    private String canonicalRequestHash;
    private String documentHash;
    private String hashStatus;
    private String createdAt;
    private String sentAt;
    private String receivedAt;
    private String validatedAt;
    private String acceptedAt;
    private String completedAt;
    private String errorCode;
    private Object data;

    public CanonicalAddressUpdateResponse() {}

    public CanonicalAddressUpdateResponse(String applicationId, String status, String message, String correlationId, String targetDepartment, String errorCode) {
        this.applicationId = applicationId;
        this.status = status;
        this.message = message;
        this.correlationId = correlationId;
        this.targetDepartment = targetDepartment;
        this.errorCode = errorCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public Integer getRequestVersion() { return requestVersion; }
    public void setRequestVersion(Integer requestVersion) { this.requestVersion = requestVersion; }

    public String getTargetDepartment() { return targetDepartment; }
    public void setTargetDepartment(String targetDepartment) { this.targetDepartment = targetDepartment; }

    public String getAcknowledgementId() { return acknowledgementId; }
    public void setAcknowledgementId(String acknowledgementId) { this.acknowledgementId = acknowledgementId; }

    public String getCanonicalRequestHash() { return canonicalRequestHash; }
    public void setCanonicalRequestHash(String canonicalRequestHash) { this.canonicalRequestHash = canonicalRequestHash; }

    public String getDocumentHash() { return documentHash; }
    public void setDocumentHash(String documentHash) { this.documentHash = documentHash; }

    public String getHashStatus() { return hashStatus; }
    public void setHashStatus(String hashStatus) { this.hashStatus = hashStatus; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getSentAt() { return sentAt; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }

    public String getReceivedAt() { return receivedAt; }
    public void setReceivedAt(String receivedAt) { this.receivedAt = receivedAt; }

    public String getValidatedAt() { return validatedAt; }
    public void setValidatedAt(String validatedAt) { this.validatedAt = validatedAt; }

    public String getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(String acceptedAt) { this.acceptedAt = acceptedAt; }

    public String getCompletedAt() { return completedAt; }
    public void setCompletedAt(String completedAt) { this.completedAt = completedAt; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public static class Builder {
        private String applicationId;
        private String status;
        private String message;
        private String correlationId;
        private Integer requestVersion;
        private String targetDepartment;
        private String acknowledgementId;
        private String canonicalRequestHash;
        private String documentHash;
        private String hashStatus;
        private String createdAt;
        private String sentAt;
        private String receivedAt;
        private String validatedAt;
        private String acceptedAt;
        private String completedAt;
        private String errorCode;
        private Object data;

        public Builder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public Builder requestVersion(Integer requestVersion) { this.requestVersion = requestVersion; return this; }
        public Builder targetDepartment(String targetDepartment) { this.targetDepartment = targetDepartment; return this; }
        public Builder acknowledgementId(String acknowledgementId) { this.acknowledgementId = acknowledgementId; return this; }
        public Builder canonicalRequestHash(String canonicalRequestHash) { this.canonicalRequestHash = canonicalRequestHash; return this; }
        public Builder documentHash(String documentHash) { this.documentHash = documentHash; return this; }
        public Builder hashStatus(String hashStatus) { this.hashStatus = hashStatus; return this; }
        public Builder createdAt(String createdAt) { this.createdAt = createdAt; return this; }
        public Builder sentAt(String sentAt) { this.sentAt = sentAt; return this; }
        public Builder receivedAt(String receivedAt) { this.receivedAt = receivedAt; return this; }
        public Builder validatedAt(String validatedAt) { this.validatedAt = validatedAt; return this; }
        public Builder acceptedAt(String acceptedAt) { this.acceptedAt = acceptedAt; return this; }
        public Builder completedAt(String completedAt) { this.completedAt = completedAt; return this; }
        public Builder errorCode(String errorCode) { this.errorCode = errorCode; return this; }
        public Builder data(Object data) { this.data = data; return this; }

        public CanonicalAddressUpdateResponse build() {
            CanonicalAddressUpdateResponse resp = new CanonicalAddressUpdateResponse(applicationId, status, message, correlationId, targetDepartment, errorCode);
            resp.setRequestVersion(requestVersion);
            resp.setAcknowledgementId(acknowledgementId);
            resp.setCanonicalRequestHash(canonicalRequestHash);
            resp.setDocumentHash(documentHash);
            resp.setHashStatus(hashStatus);
            resp.setCreatedAt(createdAt);
            resp.setSentAt(sentAt);
            resp.setReceivedAt(receivedAt);
            resp.setValidatedAt(validatedAt);
            resp.setAcceptedAt(acceptedAt);
            resp.setCompletedAt(completedAt);
            resp.setData(data);
            return resp;
        }
    }
}
