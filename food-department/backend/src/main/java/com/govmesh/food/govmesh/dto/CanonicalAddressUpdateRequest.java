package com.govmesh.food.govmesh.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CanonicalAddressUpdateRequest {
    private String applicationId;
    private String sourceDepartment;
    private String targetDepartment;
    private String correlationId;
    private Integer requestVersion;
    private String serviceCode;
    private String requestType;
    private String purpose;
    private List<String> requestedFields;
    private String canonicalRequestHash;
    private String documentHash;
    private String createdAt;
    private String sentAt;
    private CitizenInfo citizen;
    private VerificationInfo verification;
    private ConsentInfo consent;
    private List<DocumentInfo> documents = new ArrayList<>();

    public CanonicalAddressUpdateRequest() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CanonicalAddressUpdateRequest req = new CanonicalAddressUpdateRequest();

        public Builder applicationId(String applicationId) { req.setApplicationId(applicationId); return this; }
        public Builder sourceDepartment(String sourceDepartment) { req.setSourceDepartment(sourceDepartment); return this; }
        public Builder targetDepartment(String targetDepartment) { req.setTargetDepartment(targetDepartment); return this; }
        public Builder correlationId(String correlationId) { req.setCorrelationId(correlationId); return this; }
        public Builder requestVersion(Integer requestVersion) { req.setRequestVersion(requestVersion); return this; }
        public Builder serviceCode(String serviceCode) { req.setServiceCode(serviceCode); return this; }
        public Builder requestType(String requestType) { req.setRequestType(requestType); return this; }
        public Builder purpose(String purpose) { req.setPurpose(purpose); return this; }
        public Builder requestedFields(List<String> requestedFields) { req.setRequestedFields(requestedFields); return this; }
        public Builder canonicalRequestHash(String canonicalRequestHash) { req.setCanonicalRequestHash(canonicalRequestHash); return this; }
        public Builder documentHash(String documentHash) { req.setDocumentHash(documentHash); return this; }
        public Builder createdAt(String createdAt) { req.setCreatedAt(createdAt); return this; }
        public Builder sentAt(String sentAt) { req.setSentAt(sentAt); return this; }
        public Builder citizen(CitizenInfo citizen) { req.setCitizen(citizen); return this; }
        public Builder verification(VerificationInfo verification) { req.setVerification(verification); return this; }
        public Builder consent(ConsentInfo consent) { req.setConsent(consent); return this; }
        public Builder documents(List<DocumentInfo> documents) { req.setDocuments(documents); return this; }

        public CanonicalAddressUpdateRequest build() { return req; }
    }

    public CanonicalAddressUpdateRequest(String applicationId, String sourceDepartment, String targetDepartment, String correlationId, CitizenInfo citizen, VerificationInfo verification, ConsentInfo consent) {
        this.applicationId = applicationId;
        this.sourceDepartment = sourceDepartment;
        this.targetDepartment = targetDepartment;
        this.correlationId = correlationId;
        this.citizen = citizen;
        this.verification = verification;
        this.consent = consent;
    }

    public CanonicalAddressUpdateRequest(String applicationId, String sourceDepartment, String targetDepartment, String correlationId, String purpose, List<String> requestedFields, CitizenInfo citizen, VerificationInfo verification, ConsentInfo consent) {
        this.applicationId = applicationId;
        this.sourceDepartment = sourceDepartment;
        this.targetDepartment = targetDepartment;
        this.correlationId = correlationId;
        this.purpose = purpose;
        this.requestedFields = requestedFields;
        this.citizen = citizen;
        this.verification = verification;
        this.consent = consent;
    }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getSourceDepartment() { return sourceDepartment; }
    public void setSourceDepartment(String sourceDepartment) { this.sourceDepartment = sourceDepartment; }

    public String getTargetDepartment() { return targetDepartment; }
    public void setTargetDepartment(String targetDepartment) { this.targetDepartment = targetDepartment; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public Integer getRequestVersion() { return requestVersion != null ? requestVersion : 1; }
    public void setRequestVersion(Integer requestVersion) { this.requestVersion = requestVersion; }

    public String getServiceCode() { return serviceCode != null ? serviceCode : requestType; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }

    public String getRequestType() { return requestType != null ? requestType : serviceCode; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public List<String> getRequestedFields() { return requestedFields; }
    public void setRequestedFields(List<String> requestedFields) { this.requestedFields = requestedFields; }

    public String getCanonicalRequestHash() { return canonicalRequestHash; }
    public void setCanonicalRequestHash(String canonicalRequestHash) { this.canonicalRequestHash = canonicalRequestHash; }

    public String getDocumentHash() { return documentHash; }
    public void setDocumentHash(String documentHash) { this.documentHash = documentHash; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getSentAt() { return sentAt; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }

    public CitizenInfo getCitizen() { return citizen; }
    public void setCitizen(CitizenInfo citizen) { this.citizen = citizen; }

    public VerificationInfo getVerification() { return verification; }
    public void setVerification(VerificationInfo verification) { this.verification = verification; }

    public ConsentInfo getConsent() { return consent; }
    public void setConsent(ConsentInfo consent) { this.consent = consent; }

    public List<DocumentInfo> getDocuments() { return documents; }
    public void setDocuments(List<DocumentInfo> documents) { this.documents = documents != null ? documents : new ArrayList<>(); }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CitizenInfo {
        private String reference;
        private String name;
        private AddressInfo address;

        public CitizenInfo() {}
        public CitizenInfo(String reference, String name, AddressInfo address) {
            this.reference = reference;
            this.name = name;
            this.address = address;
        }

        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public AddressInfo getAddress() { return address; }
        public void setAddress(AddressInfo address) { this.address = address; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressInfo {
        private String line;
        private String line1;
        private String district;
        private String taluka;
        private String state;
        private String pincode;

        public AddressInfo() {}
        public AddressInfo(String line, String district, String taluka) {
            this.line = line;
            this.district = district;
            this.taluka = taluka;
        }

        public String getLine() { return line != null ? line : line1; }
        public void setLine(String line) { this.line = line; }

        public String getLine1() { return line1 != null ? line1 : line; }
        public void setLine1(String line1) { this.line1 = line1; }

        public String getDistrict() { return district; }
        public void setDistrict(String district) { this.district = district; }

        public String getTaluka() { return taluka; }
        public void setTaluka(String taluka) { this.taluka = taluka; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getPincode() { return pincode; }
        public void setPincode(String pincode) { this.pincode = pincode; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VerificationInfo {
        private String status;
        private String source;
        private Boolean verified;

        public VerificationInfo() {}
        public VerificationInfo(String status, String source) {
            this.status = status;
            this.source = source;
            this.verified = "VALID".equalsIgnoreCase(status) || "VERIFIED".equalsIgnoreCase(status);
        }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }

        public Boolean getVerified() { return verified; }
        public void setVerified(Boolean verified) { this.verified = verified; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConsentInfo {
        private String id;

        public ConsentInfo() {}
        public ConsentInfo(String id) {
            this.id = id;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DocumentInfo {
        private String id;
        private String name;
        private String type;
        private String size;
        private String contentType;
        private String checksum;
        private String uploadedAt;

        public DocumentInfo() {}

        public DocumentInfo(String id, String name, String type, String size, String checksum) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.size = size;
            this.checksum = checksum;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }

        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }

        public String getChecksum() { return checksum; }
        public void setChecksum(String checksum) { this.checksum = checksum; }

        public String getUploadedAt() { return uploadedAt; }
        public void setUploadedAt(String uploadedAt) { this.uploadedAt = uploadedAt; }
    }
}
