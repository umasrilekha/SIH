package com.govmesh.food.dto;

import java.time.LocalDateTime;

public class RationRecordDTOs {

    public static class RationRecordDTO {
        private Long id;
        private String rationCardNo;
        private String holderName;
        private String houseAddress;
        private String talukaCode;
        private String districtCode;
        private Boolean verificationFlag;
        private String updateStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RationRecordDTO() {}
        public RationRecordDTO(Long id, String rationCardNo, String holderName, String houseAddress, String talukaCode, String districtCode, Boolean verificationFlag, String updateStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.rationCardNo = rationCardNo;
            this.holderName = holderName;
            this.houseAddress = houseAddress;
            this.talukaCode = talukaCode;
            this.districtCode = districtCode;
            this.verificationFlag = verificationFlag;
            this.updateStatus = updateStatus;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
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

        public static RationRecordDTOBuilder builder() { return new RationRecordDTOBuilder(); }

        public static class RationRecordDTOBuilder {
            private Long id;
            private String rationCardNo;
            private String holderName;
            private String houseAddress;
            private String talukaCode;
            private String districtCode;
            private Boolean verificationFlag;
            private String updateStatus;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public RationRecordDTOBuilder id(Long id) { this.id = id; return this; }
            public RationRecordDTOBuilder rationCardNo(String rationCardNo) { this.rationCardNo = rationCardNo; return this; }
            public RationRecordDTOBuilder holderName(String holderName) { this.holderName = holderName; return this; }
            public RationRecordDTOBuilder houseAddress(String houseAddress) { this.houseAddress = houseAddress; return this; }
            public RationRecordDTOBuilder talukaCode(String talukaCode) { this.talukaCode = talukaCode; return this; }
            public RationRecordDTOBuilder districtCode(String districtCode) { this.districtCode = districtCode; return this; }
            public RationRecordDTOBuilder verificationFlag(Boolean verificationFlag) { this.verificationFlag = verificationFlag; return this; }
            public RationRecordDTOBuilder updateStatus(String updateStatus) { this.updateStatus = updateStatus; return this; }
            public RationRecordDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public RationRecordDTOBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public RationRecordDTO build() {
                return new RationRecordDTO(id, rationCardNo, holderName, houseAddress, talukaCode, districtCode, verificationFlag, updateStatus, createdAt, updatedAt);
            }
        }
    }
}
