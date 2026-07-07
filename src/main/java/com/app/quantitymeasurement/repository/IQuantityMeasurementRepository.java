package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface IQuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> findAll();

    default List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        return findAll().stream()
                .filter(entity -> entity.getOperation().equalsIgnoreCase(operation))
                .toList();
    }

    default List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return findAll().stream()
                .filter(entity -> entity.getMeasurementType().equalsIgnoreCase(measurementType))
                .toList();
    }

    default int getTotalCount() {
        return findAll().size();
    }

    default void deleteAllMeasurements() {
        throw new UnsupportedOperationException("Repository does not support deleting all measurements");
    }

    default Map<String, Integer> getPoolStatistics() {
        return Collections.emptyMap();
    }

    default void releaseResources() {
        // default repository implementations do not hold external resources
    }
}
