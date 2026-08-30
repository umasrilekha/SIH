package com.govmesh.food.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ration_records")
public class RationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ration_card_no", nullable = false, unique = true, length = 50)
    private String rationCardNo;

    @Column(name = "holder_name", nullable = false, length = 150)
    private String holderName;

    @Column(name = "house_address", nullable = false, columnDefinition = "TEXT")
    private String houseAddress;

    @Column(name = "taluka_code", nullable = false, length = 50)
    private String talukaCode;

    @Column(name = "district_code", nullable = false, length = 50)
    private String districtCode;

    @Column(name = "verification_flag", nullable = false)
    private Boolean verificationFlag = true;

    @Column(name = "update_status", nullable = false, length = 50)
    private String updateStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public RationRecord() {}

    public RationRecord(Long id, String rationCardNo, String holderName, String houseAddress, String talukaCode, String districtCode, Boolean verificationFlag, String updateStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.rationCardNo = rationCardNo;
        this.holderName = holderName;
        this.houseAddress = houseAddress;
        this.talukaCode = talukaCode;
        this.districtCode = districtCode;
        this.verificationFlag = verificationFlag != null ? verificationFlag : true;
        this.updateStatus = updateStatus;
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

    public String getRationCardNo() { return rationCardNo; }
    public void setRationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public String getHouseAddress() { return houseAddress; }
    public void setHouseAddress(String houseAddress) { this.houseAddress = houseAddress; }

    public String getTalukaCode() { return talukaCode; }
    public void setTalukaCode(String talukaCode) { this.talukaCode = talukaCode; }

    public String getDistrictCode() { return districtCode; }
    public void setDistrictCode(String districtCode) { this.districtCode = districtCode; }

    public Boolean getVerificationFlag() { return verificationFlag; }
    public void setVerificationFlag(Boolean verificationFlag) { this.verificationFlag = verificationFlag; }

    public String getUpdateStatus() { return updateStatus; }
    public void setUpdateStatus(String updateStatus) { this.updateStatus = updateStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static RationRecordBuilder builder() { return new RationRecordBuilder(); }

    public static class RationRecordBuilder {
        private Long id;
        private String rationCardNo;
        private String holderName;
        private String houseAddress;
        private String talukaCode;
        private String districtCode;
        private Boolean verificationFlag = true;
        private String updateStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RationRecordBuilder id(Long id) { this.id = id; return this; }
        public RationRecordBuilder rationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; return this; }
        public RationRecordBuilder holderName(String holderName) { this.holderName = holderName; return this; }
        public RationRecordBuilder houseAddress(String houseAddress) { this.houseAddress = houseAddress; return this; }
        public RationRecordBuilder talukaCode(String talukaCode) { this.talukaCode = talukaCode; return this; }
        public RationRecordBuilder districtCode(String districtCode) { this.districtCode = districtCode; return this; }
        public RationRecordBuilder verificationFlag(Boolean verificationFlag) { this.verificationFlag = verificationFlag; return this; }
        public RationRecordBuilder updateStatus(String updateStatus) { this.updateStatus = updateStatus; return this; }
        public RationRecordBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public RationRecordBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public RationRecord build() {
            return new RationRecord(id, rationCardNo, holderName, houseAddress, talukaCode, districtCode, verificationFlag, updateStatus, createdAt, updatedAt);
        }
    }
}
