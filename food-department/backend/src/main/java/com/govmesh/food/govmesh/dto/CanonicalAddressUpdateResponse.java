package com.govmesh.food.govmesh.dto;

public class CanonicalAddressUpdateResponse {
    private String applicationId;
    private String status;
    private String message;
    private String correlationId;
    private String targetDepartment;
    private String errorCode;

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

    public String getTargetDepartment() { return targetDepartment; }
    public void setTargetDepartment(String targetDepartment) { this.targetDepartment = targetDepartment; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public static class Builder {
        private String applicationId;
        private String status;
        private String message;
        private String correlationId;
        private String targetDepartment;
        private String errorCode;

        public Builder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public Builder targetDepartment(String targetDepartment) { this.targetDepartment = targetDepartment; return this; }
        public Builder errorCode(String errorCode) { this.errorCode = errorCode; return this; }

        public CanonicalAddressUpdateResponse build() {
            return new CanonicalAddressUpdateResponse(applicationId, status, message, correlationId, targetDepartment, errorCode);
        }
    }
}
