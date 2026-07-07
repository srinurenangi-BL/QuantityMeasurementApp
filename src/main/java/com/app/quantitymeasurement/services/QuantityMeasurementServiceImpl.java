package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.QuantityMeasurementApp;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.QuantityModel;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {
    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuantityDTO compare(QuantityDTO first, QuantityDTO second) {
        QuantityModel<IMeasurable> left = toModel(first);
        QuantityModel<IMeasurable> right = toModel(second);
        boolean equal = left.getUnit().getClass() == right.getUnit().getClass()
                && new QuantityMeasurementApp.Quantity<IMeasurable>(left.getValue(), left.getUnit())
                .equals(new QuantityMeasurementApp.Quantity<IMeasurable>(right.getValue(), right.getUnit()));
        repository.save(new QuantityMeasurementEntity(first.getValue(), second.getValue(), first.getUnit(),
                second.getUnit(), first.getCategory(), "compare", String.valueOf(equal), true, null));
        return new QuantityDTO(first.getValue(), first.getUnit(), first.getCategory(), "compare", String.valueOf(equal), true, null);
    }

    @Override
    public QuantityDTO convert(QuantityDTO source, String targetUnit) {
        QuantityModel<IMeasurable> model = toModel(source);
        IMeasurable target = resolveUnit(targetUnit, source.getCategory());
        QuantityMeasurementApp.Quantity<IMeasurable> result = new QuantityMeasurementApp.Quantity<IMeasurable>(model.getValue(), model.getUnit())
                .convertTo(target);
        repository.save(new QuantityMeasurementEntity(source.getValue(), 0.0, source.getUnit(), targetUnit,
                source.getCategory(), "convert", String.valueOf(result.value), true, null));
        return new QuantityDTO(result.value, targetUnit, source.getCategory(), "convert", String.valueOf(result.value), true, null);
    }

    @Override
    public QuantityDTO add(QuantityDTO first, QuantityDTO second, String targetUnit) {
        QuantityModel<IMeasurable> left = toModel(first);
        QuantityModel<IMeasurable> right = toModel(second);
        IMeasurable target = resolveUnit(defaultTargetUnit(targetUnit, first), first.getCategory());
        QuantityMeasurementApp.Quantity<IMeasurable> result = new QuantityMeasurementApp.Quantity<IMeasurable>(left.getValue(), left.getUnit())
                .add(new QuantityMeasurementApp.Quantity<IMeasurable>(right.getValue(), right.getUnit()), target);
        repository.save(new QuantityMeasurementEntity(first.getValue(), second.getValue(), first.getUnit(),
                second.getUnit(), first.getCategory(), "add", String.valueOf(result.value), true, null));
        return new QuantityDTO(result.value, target.getUnitName(), first.getCategory(), "add", String.valueOf(result.value), true, null);
    }

    @Override
    public QuantityDTO subtract(QuantityDTO first, QuantityDTO second, String targetUnit) {
        QuantityModel<IMeasurable> left = toModel(first);
        QuantityModel<IMeasurable> right = toModel(second);
        IMeasurable target = resolveUnit(defaultTargetUnit(targetUnit, first), first.getCategory());
        QuantityMeasurementApp.Quantity<IMeasurable> result = new QuantityMeasurementApp.Quantity<IMeasurable>(left.getValue(), left.getUnit())
                .subtract(new QuantityMeasurementApp.Quantity<IMeasurable>(right.getValue(), right.getUnit()), target);
        repository.save(new QuantityMeasurementEntity(first.getValue(), second.getValue(), first.getUnit(),
                second.getUnit(), first.getCategory(), "subtract", String.valueOf(result.value), true, null));
        return new QuantityDTO(result.value, target.getUnitName(), first.getCategory(), "subtract", String.valueOf(result.value), true, null);
    }

    @Override
    public QuantityDTO divide(QuantityDTO first, QuantityDTO second) {
        QuantityModel<IMeasurable> left = toModel(first);
        QuantityModel<IMeasurable> right = toModel(second);
        double result = new QuantityMeasurementApp.Quantity<IMeasurable>(left.getValue(), left.getUnit())
                .divide(new QuantityMeasurementApp.Quantity<IMeasurable>(right.getValue(), right.getUnit()));
        repository.save(new QuantityMeasurementEntity(first.getValue(), second.getValue(), first.getUnit(),
                second.getUnit(), first.getCategory(), "divide", String.valueOf(result), true, null));
        return new QuantityDTO(result, first.getUnit(), first.getCategory(), "divide", String.valueOf(result), true, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuantityMeasurementEntity> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuantityMeasurementEntity> findByOperation(String operation) {
        return repository.findByOperationIgnoreCase(operation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {
        return repository.findByMeasurementTypeIgnoreCase(measurementType);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countSuccessfulOperations(String operation) {
        return repository.countByOperationIgnoreCaseAndErrorFalse(operation);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    private QuantityModel<IMeasurable> toModel(QuantityDTO dto) {
        return new QuantityModel<>(dto.getValue(), resolveUnit(dto.getUnit(), dto.getCategory()));
    }

    private IMeasurable resolveUnit(String unitName, String category) {
        String normalizedUnit = normalizeUnit(unitName);
        switch (normalizeCategory(category)) {
            case "length":
                return LengthUnit.valueOf(normalizedUnit);
            case "weight":
                return WeightUnit.valueOf(normalizedUnit);
            case "volume":
                return VolumeUnit.valueOf(normalizedUnit);
            case "temperature":
                return TemperatureUnit.valueOf(normalizedUnit);
            default:
                throw new QuantityMeasurementException("Unsupported category: " + category);
        }
    }

    private String defaultTargetUnit(String targetUnit, QuantityDTO first) {
        if (targetUnit == null || targetUnit.isBlank()) {
            return first.getUnit();
        }
        return targetUnit;
    }

    private String normalizeUnit(String value) {
        if (value == null || value.isBlank()) {
            throw new QuantityMeasurementException("Unit is required");
        }
        return value.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeCategory(String value) {
        if (value == null || value.isBlank()) {
            throw new QuantityMeasurementException("Category is required");
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
