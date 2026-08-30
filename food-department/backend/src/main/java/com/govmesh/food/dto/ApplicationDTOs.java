package com.govmesh.food.dto;

import java.time.LocalDateTime;

public class ApplicationDTOs {

    public static class ApplicationDTO {
        private Long id;
        private String applicationId;
        private String citizenReference;
        private String rationCardNo;
        private String applicationType;
        private String currentStatus;
        private String sourceDepartment;
        private String requestedAddress;
        private String officerComments;
        private String reviewedByOfficer;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ApplicationDTO() {}
        public ApplicationDTO(Long id, String applicationId, String citizenReference, String rationCardNo, String applicationType, String currentStatus, String sourceDepartment, String requestedAddress, String officerComments, String reviewedByOfficer, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.applicationId = applicationId;
            this.citizenReference = citizenReference;
            this.rationCardNo = rationCardNo;
            this.applicationType = applicationType;
            this.currentStatus = currentStatus;
            this.sourceDepartment = sourceDepartment;
            this.requestedAddress = requestedAddress;
            this.officerComments = officerComments;
            this.reviewedByOfficer = reviewedByOfficer;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getApplicationId() { return applicationId; }
        public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

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

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

        public static ApplicationDTOBuilder builder() { return new ApplicationDTOBuilder(); }

        public static class ApplicationDTOBuilder {
            private Long id;
            private String applicationId;
            private String citizenReference;
            private String rationCardNo;
            private String applicationType;
            private String currentStatus;
            private String sourceDepartment;
            private String requestedAddress;
            private String officerComments;
            private String reviewedByOfficer;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public ApplicationDTOBuilder id(Long id) { this.id = id; return this; }
            public ApplicationDTOBuilder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
            public ApplicationDTOBuilder citizenReference(String citizenReference) { this.citizenReference = citizenReference; return this; }
            public ApplicationDTOBuilder rationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; return this; }
            public ApplicationDTOBuilder applicationType(String applicationType) { this.applicationType = applicationType; return this; }
            public ApplicationDTOBuilder currentStatus(String currentStatus) { this.currentStatus = currentStatus; return this; }
            public ApplicationDTOBuilder sourceDepartment(String sourceDepartment) { this.sourceDepartment = sourceDepartment; return this; }
            public ApplicationDTOBuilder requestedAddress(String requestedAddress) { this.requestedAddress = requestedAddress; return this; }
            public ApplicationDTOBuilder officerComments(String officerComments) { this.officerComments = officerComments; return this; }
            public ApplicationDTOBuilder reviewedByOfficer(String reviewedByOfficer) { this.reviewedByOfficer = reviewedByOfficer; return this; }
            public ApplicationDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public ApplicationDTOBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public ApplicationDTO build() {
                return new ApplicationDTO(id, applicationId, citizenReference, rationCardNo, applicationType, currentStatus, sourceDepartment, requestedAddress, officerComments, reviewedByOfficer, createdAt, updatedAt);
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
