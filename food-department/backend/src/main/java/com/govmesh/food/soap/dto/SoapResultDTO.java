package com.govmesh.food.soap.dto;

public class SoapResultDTO {
    private String applicationId;
    private String status;
    private String message;
    private String correlationId;

    public SoapResultDTO() {}

    public SoapResultDTO(String applicationId, String status, String message, String correlationId) {
        this.applicationId = applicationId;
        this.status = status;
        this.message = message;
        this.correlationId = correlationId;
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

    public static class Builder {
        private String applicationId;
        private String status;
        private String message;
        private String correlationId;

        public Builder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }

        public SoapResultDTO build() {
            return new SoapResultDTO(applicationId, status, message, correlationId);
        }
    }
}
