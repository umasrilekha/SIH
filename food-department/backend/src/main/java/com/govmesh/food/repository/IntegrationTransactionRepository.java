package com.govmesh.food.repository;

import com.govmesh.food.entity.IntegrationTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IntegrationTransactionRepository extends JpaRepository<IntegrationTransaction, Long> {

    Optional<IntegrationTransaction> findByCorrelationId(String correlationId);

    List<IntegrationTransaction> findByApplicationIdOrderByStartedAtDesc(String applicationId);

    @Query("SELECT it FROM IntegrationTransaction it ORDER BY it.startedAt DESC")
    List<IntegrationTransaction> findAllOrderedByStartedAtDesc();
}
