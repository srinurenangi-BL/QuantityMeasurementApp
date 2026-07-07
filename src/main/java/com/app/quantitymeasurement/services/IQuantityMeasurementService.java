package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementService {
    QuantityDTO compare(QuantityDTO first, QuantityDTO second);

    QuantityDTO convert(QuantityDTO source, String targetUnit);

    QuantityDTO add(QuantityDTO first, QuantityDTO second, String targetUnit);

    QuantityDTO subtract(QuantityDTO first, QuantityDTO second, String targetUnit);

    QuantityDTO divide(QuantityDTO first, QuantityDTO second);

    List<QuantityMeasurementEntity> findAll();

    List<QuantityMeasurementEntity> findByOperation(String operation);

    List<QuantityMeasurementEntity> findByMeasurementType(String measurementType);

    long count();

    long countSuccessfulOperations(String operation);

    void deleteAll();
}
