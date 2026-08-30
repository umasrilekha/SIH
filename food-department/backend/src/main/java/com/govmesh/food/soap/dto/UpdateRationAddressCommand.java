package com.govmesh.food.soap.dto;

public class UpdateRationAddressCommand {
    private String applicationId;
    private String citizenName;
    private String rationCardNo;
    private String address;
    private String districtCode;
    private String talukaCode;
    private Boolean revenueVerified;
    private String consentId;
    private String correlationId;

    public UpdateRationAddressCommand() {}

    public UpdateRationAddressCommand(String applicationId, String citizenName, String rationCardNo, String address, String districtCode, String talukaCode, Boolean revenueVerified, String consentId, String correlationId) {
        this.applicationId = applicationId;
        this.citizenName = citizenName;
        this.rationCardNo = rationCardNo;
        this.address = address;
        this.districtCode = districtCode;
        this.talukaCode = talukaCode;
        this.revenueVerified = revenueVerified;
        this.consentId = consentId;
        this.correlationId = correlationId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public String getRationCardNo() { return rationCardNo; }
    public void setRationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDistrictCode() { return districtCode; }
    public void setDistrictCode(String districtCode) { this.districtCode = districtCode; }

    public String getTalukaCode() { return talukaCode; }
    public void setTalukaCode(String talukaCode) { this.talukaCode = talukaCode; }

    public Boolean getRevenueVerified() { return revenueVerified; }
    public void setRevenueVerified(Boolean revenueVerified) { this.revenueVerified = revenueVerified; }

    public String getConsentId() { return consentId; }
    public void setConsentId(String consentId) { this.consentId = consentId; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public static class Builder {
        private String applicationId;
        private String citizenName;
        private String rationCardNo;
        private String address;
        private String districtCode;
        private String talukaCode;
        private Boolean revenueVerified;
        private String consentId;
        private String correlationId;

        public Builder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
        public Builder citizenName(String citizenName) { this.citizenName = citizenName; return this; }
        public Builder rationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder districtCode(String districtCode) { this.districtCode = districtCode; return this; }
        public Builder talukaCode(String talukaCode) { this.talukaCode = talukaCode; return this; }
        public Builder revenueVerified(Boolean revenueVerified) { this.revenueVerified = revenueVerified; return this; }
        public Builder consentId(String consentId) { this.consentId = consentId; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }

        public UpdateRationAddressCommand build() {
            return new UpdateRationAddressCommand(applicationId, citizenName, rationCardNo, address, districtCode, talukaCode, revenueVerified, consentId, correlationId);
        }
    }
}
