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

    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    @Column(name = "request_version")
    private Integer requestVersion = 1;

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

    @Column(name = "canonical_request_hash", length = 128)
    private String canonicalRequestHash;

    @Column(name = "document_hash", length = 128)
    private String documentHash;

    @Column(name = "hash_status", length = 50)
    private String hashStatus = "VERIFIED";

    @Column(name = "document_id", length = 100)
    private String documentId;

    @Column(name = "document_name", length = 255)
    private String documentName;

    @Column(name = "document_type", length = 100)
    private String documentType;

    @Column(name = "document_size", length = 50)
    private String documentSize;

    @Column(name = "consent_id", length = 100)
    private String consentId;

    @Column(name = "acknowledgement_id", length = 100)
    private String acknowledgementId;

    @Column(name = "sent_at", length = 50)
    private String sentAt;

    @Column(name = "received_at", length = 50)
    private String receivedAt;

    @Column(name = "validated_at", length = 50)
    private String validatedAt;

    @Column(name = "accepted_at", length = 50)
    private String acceptedAt;

    @Column(name = "processing_started_at", length = 50)
    private String processingStartedAt;

    @Column(name = "completed_at", length = 50)
    private String completedAt;

    @Column(name = "raw_source_json", columnDefinition = "TEXT")
    private String rawSourceJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Application() {}

    public Application(Long id, String applicationId, String correlationId, Integer requestVersion, String citizenReference, String rationCardNo, String applicationType, String currentStatus, String sourceDepartment, String requestedAddress, String officerComments, String reviewedByOfficer, String canonicalRequestHash, String documentHash, String hashStatus, String documentId, String documentName, String documentType, String documentSize, String consentId, String acknowledgementId, String sentAt, String receivedAt, String validatedAt, String acceptedAt, String processingStartedAt, String completedAt, String rawSourceJson, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.applicationId = applicationId;
        this.correlationId = correlationId;
        this.requestVersion = requestVersion;
        this.citizenReference = citizenReference;
        this.rationCardNo = rationCardNo;
        this.applicationType = applicationType;
        this.currentStatus = currentStatus;
        this.sourceDepartment = sourceDepartment;
        this.requestedAddress = requestedAddress;
        this.officerComments = officerComments;
        this.reviewedByOfficer = reviewedByOfficer;
        this.canonicalRequestHash = canonicalRequestHash;
        this.documentHash = documentHash;
        this.hashStatus = hashStatus;
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentType = documentType;
        this.documentSize = documentSize;
        this.consentId = consentId;
        this.acknowledgementId = acknowledgementId;
        this.sentAt = sentAt;
        this.receivedAt = receivedAt;
        this.validatedAt = validatedAt;
        this.acceptedAt = acceptedAt;
        this.processingStartedAt = processingStartedAt;
        this.completedAt = completedAt;
        this.rawSourceJson = rawSourceJson;
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

    public static ApplicationBuilder builder() { return new ApplicationBuilder(); }

    public static class ApplicationBuilder {
        private Long id;
        private String applicationId;
        private String correlationId;
        private Integer requestVersion = 1;
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
        private String hashStatus = "VERIFIED";
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

        public ApplicationBuilder id(Long id) { this.id = id; return this; }
        public ApplicationBuilder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
        public ApplicationBuilder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public ApplicationBuilder requestVersion(Integer requestVersion) { this.requestVersion = requestVersion; return this; }
        public ApplicationBuilder citizenReference(String citizenReference) { this.citizenReference = citizenReference; return this; }
        public ApplicationBuilder rationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; return this; }
        public ApplicationBuilder applicationType(String applicationType) { this.applicationType = applicationType; return this; }
        public ApplicationBuilder currentStatus(String currentStatus) { this.currentStatus = currentStatus; return this; }
        public ApplicationBuilder sourceDepartment(String sourceDepartment) { this.sourceDepartment = sourceDepartment; return this; }
        public ApplicationBuilder requestedAddress(String requestedAddress) { this.requestedAddress = requestedAddress; return this; }
        public ApplicationBuilder officerComments(String officerComments) { this.officerComments = officerComments; return this; }
        public ApplicationBuilder reviewedByOfficer(String reviewedByOfficer) { this.reviewedByOfficer = reviewedByOfficer; return this; }
        public ApplicationBuilder canonicalRequestHash(String canonicalRequestHash) { this.canonicalRequestHash = canonicalRequestHash; return this; }
        public ApplicationBuilder documentHash(String documentHash) { this.documentHash = documentHash; return this; }
        public ApplicationBuilder hashStatus(String hashStatus) { this.hashStatus = hashStatus; return this; }
        public ApplicationBuilder documentId(String documentId) { this.documentId = documentId; return this; }
        public ApplicationBuilder documentName(String documentName) { this.documentName = documentName; return this; }
        public ApplicationBuilder documentType(String documentType) { this.documentType = documentType; return this; }
        public ApplicationBuilder documentSize(String documentSize) { this.documentSize = documentSize; return this; }
        public ApplicationBuilder consentId(String consentId) { this.consentId = consentId; return this; }
        public ApplicationBuilder acknowledgementId(String acknowledgementId) { this.acknowledgementId = acknowledgementId; return this; }
        public ApplicationBuilder sentAt(String sentAt) { this.sentAt = sentAt; return this; }
        public ApplicationBuilder receivedAt(String receivedAt) { this.receivedAt = receivedAt; return this; }
        public ApplicationBuilder validatedAt(String validatedAt) { this.validatedAt = validatedAt; return this; }
        public ApplicationBuilder acceptedAt(String acceptedAt) { this.acceptedAt = acceptedAt; return this; }
        public ApplicationBuilder processingStartedAt(String processingStartedAt) { this.processingStartedAt = processingStartedAt; return this; }
        public ApplicationBuilder completedAt(String completedAt) { this.completedAt = completedAt; return this; }
        public ApplicationBuilder rawSourceJson(String rawSourceJson) { this.rawSourceJson = rawSourceJson; return this; }
        public ApplicationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ApplicationBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Application build() {
            return new Application(id, applicationId, correlationId, requestVersion, citizenReference, rationCardNo, applicationType, currentStatus, sourceDepartment, requestedAddress, officerComments, reviewedByOfficer, canonicalRequestHash, documentHash, hashStatus, documentId, documentName, documentType, documentSize, consentId, acknowledgementId, sentAt, receivedAt, validatedAt, acceptedAt, processingStartedAt, completedAt, rawSourceJson, createdAt, updatedAt);
        }
    }
}
