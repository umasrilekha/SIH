package com.govmesh.food.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consents")
public class Consent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consent_id", nullable = false, unique = true)
    private String consentId;

    @Column(name = "citizen_reference", nullable = false)
    private String citizenReference;

    @Column(name = "requesting_department", nullable = false)
    private String requestingDepartment;

    @Column(name = "receiving_department", nullable = false)
    private String receivingDepartment;

    @Column(name = "purpose", nullable = false)
    private String purpose;

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE, EXPIRED, REVOKED

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    public Consent() {}

    public Consent(Long id, String consentId, String citizenReference, String requestingDepartment, String receivingDepartment, String purpose, String status, LocalDateTime issuedAt, LocalDateTime expiresAt, LocalDateTime revokedAt) {
        this.id = id;
        this.consentId = consentId;
        this.citizenReference = citizenReference;
        this.requestingDepartment = requestingDepartment;
        this.receivingDepartment = receivingDepartment;
        this.purpose = purpose;
        this.status = status;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getConsentId() { return consentId; }
    public void setConsentId(String consentId) { this.consentId = consentId; }

    public String getCitizenReference() { return citizenReference; }
    public void setCitizenReference(String citizenReference) { this.citizenReference = citizenReference; }

    public String getRequestingDepartment() { return requestingDepartment; }
    public void setRequestingDepartment(String requestingDepartment) { this.requestingDepartment = requestingDepartment; }

    public String getReceivingDepartment() { return receivingDepartment; }
    public void setReceivingDepartment(String receivingDepartment) { this.receivingDepartment = receivingDepartment; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getRevokedAt() { return revokedAt; }
    public void setRevokedAt(LocalDateTime revokedAt) { this.revokedAt = revokedAt; }

    public static class Builder {
        private Long id;
        private String consentId;
        private String citizenReference;
        private String requestingDepartment;
        private String receivingDepartment;
        private String purpose;
        private String status;
        private LocalDateTime issuedAt;
        private LocalDateTime expiresAt;
        private LocalDateTime revokedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder consentId(String consentId) { this.consentId = consentId; return this; }
        public Builder citizenReference(String citizenReference) { this.citizenReference = citizenReference; return this; }
        public Builder requestingDepartment(String requestingDepartment) { this.requestingDepartment = requestingDepartment; return this; }
        public Builder receivingDepartment(String receivingDepartment) { this.receivingDepartment = receivingDepartment; return this; }
        public Builder purpose(String purpose) { this.purpose = purpose; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder issuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; return this; }
        public Builder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public Builder revokedAt(LocalDateTime revokedAt) { this.revokedAt = revokedAt; return this; }

        public Consent build() {
            return new Consent(id, consentId, citizenReference, requestingDepartment, receivingDepartment, purpose, status, issuedAt, expiresAt, revokedAt);
        }
    }
}
