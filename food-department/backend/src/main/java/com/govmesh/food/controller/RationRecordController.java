package com.govmesh.food.controller;

import com.govmesh.food.dto.ApplicationDTOs.ApplicationDTO;
import com.govmesh.food.dto.RationRecordDTOs.RationRecordDTO;
import com.govmesh.food.service.RationRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ration-records")
public class RationRecordController {

    private final RationRecordService rationRecordService;

    public RationRecordController(RationRecordService rationRecordService) {
        this.rationRecordService = rationRecordService;
    }

    @GetMapping
    public ResponseEntity<List<RationRecordDTO>> getRationRecords(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String taluka) {
        return ResponseEntity.ok(rationRecordService.getRationRecords(query, district, taluka));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RationRecordDTO> getRationRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(rationRecordService.getRationRecordById(id));
    }

    @GetMapping("/card/{rationCardNo}")
    public ResponseEntity<RationRecordDTO> getRationRecordByCardNo(@PathVariable String rationCardNo) {
        return ResponseEntity.ok(rationRecordService.getRationRecordByCardNo(rationCardNo));
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationDTO>> getRationRecordApplications(@PathVariable Long id) {
        return ResponseEntity.ok(rationRecordService.getRationRecordApplications(id));
    }
}
