package com.govmesh.food.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id", nullable = false, unique = true, length = 50)
    private String applicationId;

    @Column(name = "citizen_reference", nullable = false, length = 50)
    private String citizenReference;

    @Column(name = "ration_card_no", nullable = false, length = 50)
    private String rationCardNo;

    @Column(name = "application_type", nullable = false, length = 50)
    private String applicationType;

    @Column(name = "current_status", nullable = false, length = 50)
    private String currentStatus;

    @Column(name = "source_department", nullable = false, length = 50)
    private String sourceDepartment;

    @Column(name = "requested_address", columnDefinition = "TEXT")
    private String requestedAddress;

    @Column(name = "officer_comments", columnDefinition = "TEXT")
    private String officerComments;

    @Column(name = "reviewed_by_officer", length = 100)
    private String reviewedByOfficer;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Application() {}

    public Application(Long id, String applicationId, String citizenReference, String rationCardNo, String applicationType, String currentStatus, String sourceDepartment, String requestedAddress, String officerComments, String reviewedByOfficer, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public static ApplicationBuilder builder() { return new ApplicationBuilder(); }

    public static class ApplicationBuilder {
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

        public ApplicationBuilder id(Long id) { this.id = id; return this; }
        public ApplicationBuilder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
        public ApplicationBuilder citizenReference(String citizenReference) { this.citizenReference = citizenReference; return this; }
        public ApplicationBuilder rationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; return this; }
        public ApplicationBuilder applicationType(String applicationType) { this.applicationType = applicationType; return this; }
        public ApplicationBuilder currentStatus(String currentStatus) { this.currentStatus = currentStatus; return this; }
        public ApplicationBuilder sourceDepartment(String sourceDepartment) { this.sourceDepartment = sourceDepartment; return this; }
        public ApplicationBuilder requestedAddress(String requestedAddress) { this.requestedAddress = requestedAddress; return this; }
        public ApplicationBuilder officerComments(String officerComments) { this.officerComments = officerComments; return this; }
        public ApplicationBuilder reviewedByOfficer(String reviewedByOfficer) { this.reviewedByOfficer = reviewedByOfficer; return this; }
        public ApplicationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ApplicationBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Application build() {
            return new Application(id, applicationId, citizenReference, rationCardNo, applicationType, currentStatus, sourceDepartment, requestedAddress, officerComments, reviewedByOfficer, createdAt, updatedAt);
        }
    }
}
