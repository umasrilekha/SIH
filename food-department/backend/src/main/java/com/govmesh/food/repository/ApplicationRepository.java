package com.govmesh.food.repository;

import com.govmesh.food.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByApplicationId(String applicationId);

    long countByCurrentStatus(String currentStatus);

    @Query("SELECT a FROM Application a ORDER BY CASE WHEN a.applicationId = 'GM-2026-000124' THEN 0 ELSE 1 END, a.id DESC")
    List<Application> findTop5RecentApplications();

    @Query("SELECT a FROM Application a WHERE " +
           "(:query IS NULL OR :query = '' OR LOWER(a.applicationId) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(a.citizenReference) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(a.rationCardNo) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:status IS NULL OR :status = '' OR a.currentStatus = :status) AND " +
           "(:type IS NULL OR :type = '' OR a.applicationType = :type) " +
           "ORDER BY CASE WHEN a.applicationId = 'GM-2026-000124' THEN 0 ELSE 1 END, a.id DESC, a.createdAt DESC")
    List<Application> filterApplications(
            @Param("query") String query,
            @Param("status") String status,
            @Param("type") String type
    );
}
