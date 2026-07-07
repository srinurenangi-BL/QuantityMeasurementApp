package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.entity.QuantityDTO;

public interface IQuantityMeasurementService {
    QuantityDTO compare(QuantityDTO first, QuantityDTO second);

    QuantityDTO convert(QuantityDTO source, String targetUnit);

    QuantityDTO add(QuantityDTO first, QuantityDTO second, String targetUnit);

    QuantityDTO subtract(QuantityDTO first, QuantityDTO second, String targetUnit);

    QuantityDTO divide(QuantityDTO first, QuantityDTO second);
}
