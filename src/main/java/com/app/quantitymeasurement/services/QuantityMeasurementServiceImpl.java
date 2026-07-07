package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.QuantityMeasurementApp;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.entity.QuantityModel;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.WeightUnit;

import java.util.logging.Level;
import java.util.logging.Logger;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {
    private static final Logger LOGGER = Logger.getLogger(QuantityMeasurementServiceImpl.class.getName());

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuantityDTO compare(QuantityDTO first, QuantityDTO second) {
        try {
            QuantityModel<IMeasurable> left = toModel(first);
            QuantityModel<IMeasurable> right = toModel(second);
            boolean equal = left.getUnit().getClass() == right.getUnit().getClass()
                    && new QuantityMeasurementApp.Quantity<IMeasurable>(left.getValue(), left.getUnit())
                    .equals(new QuantityMeasurementApp.Quantity<IMeasurable>(right.getValue(), right.getUnit()));
            repository.save(new QuantityMeasurementEntity(first.getValue(), second.getValue(), first.getUnit(),
                    second.getUnit(), first.getCategory(), "compare", String.valueOf(equal), true, null));
            return new QuantityDTO(first.getValue(), first.getUnit(), first.getCategory(), "compare", String.valueOf(equal), true, null);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Comparison failed", e);
            return new QuantityDTO(first.getValue(), first.getUnit(), first.getCategory(), "compare", null, false, e.getMessage());
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO source, String targetUnit) {
        try {
            QuantityModel<IMeasurable> model = toModel(source);
            IMeasurable target = resolveUnit(targetUnit, source.getCategory());
            QuantityMeasurementApp.Quantity<IMeasurable> result = new QuantityMeasurementApp.Quantity<IMeasurable>(model.getValue(), model.getUnit())
                    .convertTo(target);
            repository.save(new QuantityMeasurementEntity(source.getValue(), 0.0, source.getUnit(), targetUnit,
                    source.getCategory(), "convert", String.valueOf(result.value), true, null));
            return new QuantityDTO(result.value, targetUnit, source.getCategory(), "convert", String.valueOf(result.value), true, null);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Conversion failed", e);
            return new QuantityDTO(source.getValue(), source.getUnit(), source.getCategory(), "convert", null, false, e.getMessage());
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO first, QuantityDTO second, String targetUnit) {
        try {
            QuantityModel<IMeasurable> left = toModel(first);
            QuantityModel<IMeasurable> right = toModel(second);
            IMeasurable target = resolveUnit(targetUnit, first.getCategory());
            QuantityMeasurementApp.Quantity<IMeasurable> result = new QuantityMeasurementApp.Quantity<IMeasurable>(left.getValue(), left.getUnit())
                    .add(new QuantityMeasurementApp.Quantity<IMeasurable>(right.getValue(), right.getUnit()), target);
            repository.save(new QuantityMeasurementEntity(first.getValue(), second.getValue(), first.getUnit(),
                    second.getUnit(), first.getCategory(), "add", String.valueOf(result.value), true, null));
            return new QuantityDTO(result.value, targetUnit, first.getCategory(), "add", String.valueOf(result.value), true, null);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Addition failed", e);
            return new QuantityDTO(first.getValue(), first.getUnit(), first.getCategory(), "add", null, false, e.getMessage());
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO first, QuantityDTO second, String targetUnit) {
        try {
            QuantityModel<IMeasurable> left = toModel(first);
            QuantityModel<IMeasurable> right = toModel(second);
            IMeasurable target = resolveUnit(targetUnit, first.getCategory());
            QuantityMeasurementApp.Quantity<IMeasurable> result = new QuantityMeasurementApp.Quantity<IMeasurable>(left.getValue(), left.getUnit())
                    .subtract(new QuantityMeasurementApp.Quantity<IMeasurable>(right.getValue(), right.getUnit()), target);
            repository.save(new QuantityMeasurementEntity(first.getValue(), second.getValue(), first.getUnit(),
                    second.getUnit(), first.getCategory(), "subtract", String.valueOf(result.value), true, null));
            return new QuantityDTO(result.value, targetUnit, first.getCategory(), "subtract", String.valueOf(result.value), true, null);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Subtraction failed", e);
            return new QuantityDTO(first.getValue(), first.getUnit(), first.getCategory(), "subtract", null, false, e.getMessage());
        }
    }

    @Override
    public QuantityDTO divide(QuantityDTO first, QuantityDTO second) {
        try {
            QuantityModel<IMeasurable> left = toModel(first);
            QuantityModel<IMeasurable> right = toModel(second);
            double result = new QuantityMeasurementApp.Quantity<IMeasurable>(left.getValue(), left.getUnit())
                    .divide(new QuantityMeasurementApp.Quantity<IMeasurable>(right.getValue(), right.getUnit()));
            repository.save(new QuantityMeasurementEntity(first.getValue(), second.getValue(), first.getUnit(),
                    second.getUnit(), first.getCategory(), "divide", String.valueOf(result), true, null));
            return new QuantityDTO(result, first.getUnit(), first.getCategory(), "divide", String.valueOf(result), true, null);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Division failed", e);
            return new QuantityDTO(first.getValue(), first.getUnit(), first.getCategory(), "divide", null, false, e.getMessage());
        }
    }

    private QuantityModel<IMeasurable> toModel(QuantityDTO dto) {
        String category = dto.getCategory();
        switch (category) {
            case "length":
                return new QuantityModel<>(dto.getValue(), LengthUnit.valueOf(dto.getUnit().toUpperCase()));
            case "weight":
                return new QuantityModel<>(dto.getValue(), WeightUnit.valueOf(dto.getUnit().toUpperCase()));
            case "volume":
                return new QuantityModel<>(dto.getValue(), VolumeUnit.valueOf(dto.getUnit().toUpperCase()));
            case "temperature":
                return new QuantityModel<>(dto.getValue(), TemperatureUnit.valueOf(dto.getUnit().toUpperCase()));
            default:
                throw new QuantityMeasurementException("Unsupported category: " + category);
        }
    }

    private IMeasurable resolveUnit(String unitName, String category) {
        switch (category) {
            case "length":
                return LengthUnit.valueOf(unitName.toUpperCase());
            case "weight":
                return WeightUnit.valueOf(unitName.toUpperCase());
            case "volume":
                return VolumeUnit.valueOf(unitName.toUpperCase());
            case "temperature":
                return TemperatureUnit.valueOf(unitName.toUpperCase());
            default:
                throw new QuantityMeasurementException("Unsupported category: " + category);
        }
    }
}
