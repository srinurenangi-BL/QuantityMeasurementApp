package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {
    List<QuantityMeasurementEntity> findByOperationIgnoreCase(String operation);

    List<QuantityMeasurementEntity> findByMeasurementTypeIgnoreCase(String measurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime createdAt);

    List<QuantityMeasurementEntity> findByErrorTrue();

    long countByOperationIgnoreCaseAndErrorFalse(String operation);

    @Query("""
            SELECT measurement
            FROM QuantityMeasurementEntity measurement
            WHERE LOWER(measurement.operation) = LOWER(:operation)
              AND measurement.error = false
            ORDER BY measurement.createdAt DESC
            """)
    List<QuantityMeasurementEntity> findSuccessfulOperationHistory(@Param("operation") String operation);
}
