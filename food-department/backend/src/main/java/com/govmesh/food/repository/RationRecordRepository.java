package com.govmesh.food.repository;

import com.govmesh.food.entity.RationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RationRecordRepository extends JpaRepository<RationRecord, Long> {

    Optional<RationRecord> findByRationCardNo(String rationCardNo);

    @Query("SELECT r FROM RationRecord r WHERE " +
           "(:query IS NULL OR :query = '' OR LOWER(r.rationCardNo) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.holderName) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:district IS NULL OR :district = '' OR LOWER(r.districtCode) LIKE LOWER(CONCAT('%', :district, '%'))) AND " +
           "(:taluka IS NULL OR :taluka = '' OR LOWER(r.talukaCode) LIKE LOWER(CONCAT('%', :taluka, '%')))")
    List<RationRecord> searchRationRecords(
            @Param("query") String query,
            @Param("district") String district,
            @Param("taluka") String taluka
    );
}
