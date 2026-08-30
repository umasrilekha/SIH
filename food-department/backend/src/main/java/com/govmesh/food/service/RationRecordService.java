package com.govmesh.food.service;

import com.govmesh.food.dto.ApplicationDTOs.ApplicationDTO;
import com.govmesh.food.dto.RationRecordDTOs.RationRecordDTO;
import com.govmesh.food.entity.Application;
import com.govmesh.food.entity.RationRecord;
import com.govmesh.food.exception.ResourceNotFoundException;
import com.govmesh.food.repository.ApplicationRepository;
import com.govmesh.food.repository.RationRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RationRecordService {

    private final RationRecordRepository rationRecordRepository;
    private final ApplicationRepository applicationRepository;

    public RationRecordService(RationRecordRepository rationRecordRepository, ApplicationRepository applicationRepository) {
        this.rationRecordRepository = rationRecordRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<RationRecordDTO> getRationRecords(String query, String district, String taluka) {
        List<RationRecord> records = rationRecordRepository.searchRationRecords(query, district, taluka);
        return records.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public RationRecordDTO getRationRecordById(Long id) {
        RationRecord record = rationRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ration record not found with ID: " + id));
        return mapToDTO(record);
    }

    public RationRecordDTO getRationRecordByCardNo(String rationCardNo) {
        RationRecord record = rationRecordRepository.findByRationCardNo(rationCardNo)
                .orElseThrow(() -> new ResourceNotFoundException("Ration record not found for Ration Card No: " + rationCardNo));
        return mapToDTO(record);
    }

    public List<ApplicationDTO> getRationRecordApplications(Long id) {
        RationRecord record = rationRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ration record not found with ID: " + id));

        List<Application> apps = applicationRepository.filterApplications(record.getRationCardNo(), null, null);
        return apps.stream().map(this::mapAppToDTO).collect(Collectors.toList());
    }

    private RationRecordDTO mapToDTO(RationRecord record) {
        return RationRecordDTO.builder()
                .id(record.getId())
                .rationCardNo(record.getRationCardNo())
                .holderName(record.getHolderName())
                .houseAddress(record.getHouseAddress())
                .talukaCode(record.getTalukaCode())
                .districtCode(record.getDistrictCode())
                .verificationFlag(record.getVerificationFlag())
                .updateStatus(record.getUpdateStatus())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }

    private ApplicationDTO mapAppToDTO(Application app) {
        return ApplicationDTO.builder()
                .id(app.getId())
                .applicationId(app.getApplicationId())
                .citizenReference(app.getCitizenReference())
                .rationCardNo(app.getRationCardNo())
                .applicationType(app.getApplicationType())
                .currentStatus(app.getCurrentStatus())
                .sourceDepartment(app.getSourceDepartment())
                .requestedAddress(app.getRequestedAddress())
                .officerComments(app.getOfficerComments())
                .reviewedByOfficer(app.getReviewedByOfficer())
                .createdAt(app.getCreatedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }
}
