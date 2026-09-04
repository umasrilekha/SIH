package com.govmesh.food.dto;

import java.time.LocalDateTime;

public class ApplicationDTOs {

    public static class ApplicationDTO {
        private Long id;
        private String applicationId;
        private String correlationId;
        private Integer requestVersion;
        private String citizenReference;
        private String rationCardNo;
        private String applicationType;
        private String currentStatus;
        private String sourceDepartment;
        private String requestedAddress;
        private String officerComments;
        private String reviewedByOfficer;
        private String canonicalRequestHash;
        private String documentHash;
        private String hashStatus;
        private String documentId;
        private String documentName;
        private String documentType;
        private String documentSize;
        private String consentId;
        private String acknowledgementId;
        private String sentAt;
        private String receivedAt;
        private String validatedAt;
        private String acceptedAt;
        private String processingStartedAt;
        private String completedAt;
        private String rawSourceJson;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ApplicationDTO() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getApplicationId() { return applicationId; }
        public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

        public String getCorrelationId() { return correlationId; }
        public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

        public Integer getRequestVersion() { return requestVersion; }
        public void setRequestVersion(Integer requestVersion) { this.requestVersion = requestVersion; }

        public String getCitizenReference() { return citizenReference; }
        public void setCitizenReference(String citizenReference) { this.citizenReference = citizenReference; }

        public String getRationCardNo() { return rationCardNo; }
        public void setRationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; }

        public String getApplicationType() { return applicationType; }
        public void setApplicationType(String applicationType) { this.applicationType = applicationType; }

        public String getCurrentStatus() { return currentStatus; }
        public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

        public String getSourceDepartment() { return sourceDepartment; }
        public void setSourceDepartment(String sourceDepartment) { this.sourceDepartment = sourceDepartment; }

        public String getRequestedAddress() { return requestedAddress; }
        public void setRequestedAddress(String requestedAddress) { this.requestedAddress = requestedAddress; }

        public String getOfficerComments() { return officerComments; }
        public void setOfficerComments(String officerComments) { this.officerComments = officerComments; }

        public String getReviewedByOfficer() { return reviewedByOfficer; }
        public void setReviewedByOfficer(String reviewedByOfficer) { this.reviewedByOfficer = reviewedByOfficer; }

        public String getCanonicalRequestHash() { return canonicalRequestHash; }
        public void setCanonicalRequestHash(String canonicalRequestHash) { this.canonicalRequestHash = canonicalRequestHash; }

        public String getDocumentHash() { return documentHash; }
        public void setDocumentHash(String documentHash) { this.documentHash = documentHash; }

        public String getHashStatus() { return hashStatus; }
        public void setHashStatus(String hashStatus) { this.hashStatus = hashStatus; }

        public String getDocumentId() { return documentId; }
        public void setDocumentId(String documentId) { this.documentId = documentId; }

        public String getDocumentName() { return documentName; }
        public void setDocumentName(String documentName) { this.documentName = documentName; }

        public String getDocumentType() { return documentType; }
        public void setDocumentType(String documentType) { this.documentType = documentType; }

        public String getDocumentSize() { return documentSize; }
        public void setDocumentSize(String documentSize) { this.documentSize = documentSize; }

        public String getConsentId() { return consentId; }
        public void setConsentId(String consentId) { this.consentId = consentId; }

        public String getAcknowledgementId() { return acknowledgementId; }
        public void setAcknowledgementId(String acknowledgementId) { this.acknowledgementId = acknowledgementId; }

        public String getSentAt() { return sentAt; }
        public void setSentAt(String sentAt) { this.sentAt = sentAt; }

        public String getReceivedAt() { return receivedAt; }
        public void setReceivedAt(String receivedAt) { this.receivedAt = receivedAt; }

        public String getValidatedAt() { return validatedAt; }
        public void setValidatedAt(String validatedAt) { this.validatedAt = validatedAt; }

        public String getAcceptedAt() { return acceptedAt; }
        public void setAcceptedAt(String acceptedAt) { this.acceptedAt = acceptedAt; }

        public String getProcessingStartedAt() { return processingStartedAt; }
        public void setProcessingStartedAt(String processingStartedAt) { this.processingStartedAt = processingStartedAt; }

        public String getCompletedAt() { return completedAt; }
        public void setCompletedAt(String completedAt) { this.completedAt = completedAt; }

        public String getRawSourceJson() { return rawSourceJson; }
        public void setRawSourceJson(String rawSourceJson) { this.rawSourceJson = rawSourceJson; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

        public static ApplicationDTOBuilder builder() { return new ApplicationDTOBuilder(); }

        public static class ApplicationDTOBuilder {
            private Long id;
            private String applicationId;
            private String correlationId;
            private Integer requestVersion;
            private String citizenReference;
            private String rationCardNo;
            private String applicationType;
            private String currentStatus;
            private String sourceDepartment;
            private String requestedAddress;
            private String officerComments;
            private String reviewedByOfficer;
            private String canonicalRequestHash;
            private String documentHash;
            private String hashStatus;
            private String documentId;
            private String documentName;
            private String documentType;
            private String documentSize;
            private String consentId;
            private String acknowledgementId;
            private String sentAt;
            private String receivedAt;
            private String validatedAt;
            private String acceptedAt;
            private String processingStartedAt;
            private String completedAt;
            private String rawSourceJson;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public ApplicationDTOBuilder id(Long id) { this.id = id; return this; }
            public ApplicationDTOBuilder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
            public ApplicationDTOBuilder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
            public ApplicationDTOBuilder requestVersion(Integer requestVersion) { this.requestVersion = requestVersion; return this; }
            public ApplicationDTOBuilder citizenReference(String citizenReference) { this.citizenReference = citizenReference; return this; }
            public ApplicationDTOBuilder rationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; return this; }
            public ApplicationDTOBuilder applicationType(String applicationType) { this.applicationType = applicationType; return this; }
            public ApplicationDTOBuilder currentStatus(String currentStatus) { this.currentStatus = currentStatus; return this; }
            public ApplicationDTOBuilder sourceDepartment(String sourceDepartment) { this.sourceDepartment = sourceDepartment; return this; }
            public ApplicationDTOBuilder requestedAddress(String requestedAddress) { this.requestedAddress = requestedAddress; return this; }
            public ApplicationDTOBuilder officerComments(String officerComments) { this.officerComments = officerComments; return this; }
            public ApplicationDTOBuilder reviewedByOfficer(String reviewedByOfficer) { this.reviewedByOfficer = reviewedByOfficer; return this; }
            public ApplicationDTOBuilder canonicalRequestHash(String canonicalRequestHash) { this.canonicalRequestHash = canonicalRequestHash; return this; }
            public ApplicationDTOBuilder documentHash(String documentHash) { this.documentHash = documentHash; return this; }
            public ApplicationDTOBuilder hashStatus(String hashStatus) { this.hashStatus = hashStatus; return this; }
            public ApplicationDTOBuilder documentId(String documentId) { this.documentId = documentId; return this; }
            public ApplicationDTOBuilder documentName(String documentName) { this.documentName = documentName; return this; }
            public ApplicationDTOBuilder documentType(String documentType) { this.documentType = documentType; return this; }
            public ApplicationDTOBuilder documentSize(String documentSize) { this.documentSize = documentSize; return this; }
            public ApplicationDTOBuilder consentId(String consentId) { this.consentId = consentId; return this; }
            public ApplicationDTOBuilder acknowledgementId(String acknowledgementId) { this.acknowledgementId = acknowledgementId; return this; }
            public ApplicationDTOBuilder sentAt(String sentAt) { this.sentAt = sentAt; return this; }
            public ApplicationDTOBuilder receivedAt(String receivedAt) { this.receivedAt = receivedAt; return this; }
            public ApplicationDTOBuilder validatedAt(String validatedAt) { this.validatedAt = validatedAt; return this; }
            public ApplicationDTOBuilder acceptedAt(String acceptedAt) { this.acceptedAt = acceptedAt; return this; }
            public ApplicationDTOBuilder processingStartedAt(String processingStartedAt) { this.processingStartedAt = processingStartedAt; return this; }
            public ApplicationDTOBuilder completedAt(String completedAt) { this.completedAt = completedAt; return this; }
            public ApplicationDTOBuilder rawSourceJson(String rawSourceJson) { this.rawSourceJson = rawSourceJson; return this; }
            public ApplicationDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public ApplicationDTOBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public ApplicationDTO build() {
                ApplicationDTO dto = new ApplicationDTO();
                dto.setId(id);
                dto.setApplicationId(applicationId);
                dto.setCorrelationId(correlationId);
                dto.setRequestVersion(requestVersion);
                dto.setCitizenReference(citizenReference);
                dto.setRationCardNo(rationCardNo);
                dto.setApplicationType(applicationType);
                dto.setCurrentStatus(currentStatus);
                dto.setSourceDepartment(sourceDepartment);
                dto.setRequestedAddress(requestedAddress);
                dto.setOfficerComments(officerComments);
                dto.setReviewedByOfficer(reviewedByOfficer);
                dto.setCanonicalRequestHash(canonicalRequestHash);
                dto.setDocumentHash(documentHash);
                dto.setHashStatus(hashStatus);
                dto.setDocumentId(documentId);
                dto.setDocumentName(documentName);
                dto.setDocumentType(documentType);
                dto.setDocumentSize(documentSize);
                dto.setConsentId(consentId);
                dto.setAcknowledgementId(acknowledgementId);
                dto.setSentAt(sentAt);
                dto.setReceivedAt(receivedAt);
                dto.setValidatedAt(validatedAt);
                dto.setAcceptedAt(acceptedAt);
                dto.setProcessingStartedAt(processingStartedAt);
                dto.setCompletedAt(completedAt);
                dto.setRawSourceJson(rawSourceJson);
                dto.setCreatedAt(createdAt);
                dto.setUpdatedAt(updatedAt);
                return dto;
            }
        }
    }

    public static class ApplicationDetailDTO {
        private ApplicationDTO application;
        private RationRecordDTOs.RationRecordDTO currentRationRecord;

        public ApplicationDetailDTO() {}
        public ApplicationDetailDTO(ApplicationDTO application, RationRecordDTOs.RationRecordDTO currentRationRecord) {
            this.application = application;
            this.currentRationRecord = currentRationRecord;
        }

        public ApplicationDTO getApplication() { return application; }
        public void setApplication(ApplicationDTO application) { this.application = application; }

        public RationRecordDTOs.RationRecordDTO getCurrentRationRecord() { return currentRationRecord; }
        public void setCurrentRationRecord(RationRecordDTOs.RationRecordDTO currentRationRecord) { this.currentRationRecord = currentRationRecord; }

        public static ApplicationDetailDTOBuilder builder() { return new ApplicationDetailDTOBuilder(); }

        public static class ApplicationDetailDTOBuilder {
            private ApplicationDTO application;
            private RationRecordDTOs.RationRecordDTO currentRationRecord;

            public ApplicationDetailDTOBuilder application(ApplicationDTO application) { this.application = application; return this; }
            public ApplicationDetailDTOBuilder currentRationRecord(RationRecordDTOs.RationRecordDTO currentRationRecord) { this.currentRationRecord = currentRationRecord; return this; }

            public ApplicationDetailDTO build() {
                return new ApplicationDetailDTO(application, currentRationRecord);
            }
        }
    }

    public static class ActionRequestDTO {
        private String comments;
        private String reason;

        public ActionRequestDTO() {}
        public ActionRequestDTO(String comments, String reason) {
            this.comments = comments;
            this.reason = reason;
        }

        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}
