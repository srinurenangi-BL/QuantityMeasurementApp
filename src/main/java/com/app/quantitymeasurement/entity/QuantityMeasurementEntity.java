package com.app.quantitymeasurement.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

public class QuantityMeasurementEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Set<String> LENGTH_UNITS = Set.of("FEET", "INCHES", "YARDS", "CENTIMETERS");
    private static final Set<String> WEIGHT_UNITS = Set.of("KILOGRAM", "GRAM", "POUND");
    private static final Set<String> VOLUME_UNITS = Set.of("LITRE", "MILLILITRE", "GALLON");
    private static final Set<String> TEMPERATURE_UNITS = Set.of("CELSIUS", "FAHRENHEIT");

    private final double firstValue;
    private final double secondValue;
    private final String firstUnit;
    private final String secondUnit;
    private final String measurementType;
    private final String operation;
    private final String result;
    private final boolean success;
    private final String errorMessage;
    private final LocalDateTime createdAt;

    public QuantityMeasurementEntity(
            double firstValue,
            double secondValue,
            String firstUnit,
            String secondUnit,
            String operation,
            String result,
            boolean success,
            String errorMessage) {
        this(firstValue, secondValue, firstUnit, secondUnit, inferMeasurementType(firstUnit, secondUnit),
                operation, result, success, errorMessage, LocalDateTime.now());
    }

    public QuantityMeasurementEntity(
            double firstValue,
            double secondValue,
            String firstUnit,
            String secondUnit,
            String measurementType,
            String operation,
            String result,
            boolean success,
            String errorMessage) {
        this(firstValue, secondValue, firstUnit, secondUnit, measurementType, operation, result, success,
                errorMessage, LocalDateTime.now());
    }

    public QuantityMeasurementEntity(
            double firstValue,
            double secondValue,
            String firstUnit,
            String secondUnit,
            String measurementType,
            String operation,
            String result,
            boolean success,
            String errorMessage,
            LocalDateTime createdAt) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.firstUnit = firstUnit;
        this.secondUnit = secondUnit;
        this.measurementType = normalizeMeasurementType(measurementType, firstUnit, secondUnit);
        this.operation = operation;
        this.result = result;
        this.success = success;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public double getFirstValue() {
        return firstValue;
    }

    public double getSecondValue() {
        return secondValue;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public String getMeasurementType() {
        return normalizeMeasurementType(measurementType, firstUnit, secondUnit);
    }

    public String getOperation() {
        return operation;
    }

    public String getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    private static String normalizeMeasurementType(String measurementType, String firstUnit, String secondUnit) {
        if (measurementType != null && !measurementType.isBlank()) {
            return measurementType.toLowerCase(Locale.ROOT);
        }
        return inferMeasurementType(firstUnit, secondUnit);
    }

    private static String inferMeasurementType(String firstUnit, String secondUnit) {
        String normalizedFirst = normalizeUnit(firstUnit);
        String normalizedSecond = normalizeUnit(secondUnit);
        if (LENGTH_UNITS.contains(normalizedFirst) || LENGTH_UNITS.contains(normalizedSecond)) {
            return "length";
        }
        if (WEIGHT_UNITS.contains(normalizedFirst) || WEIGHT_UNITS.contains(normalizedSecond)) {
            return "weight";
        }
        if (VOLUME_UNITS.contains(normalizedFirst) || VOLUME_UNITS.contains(normalizedSecond)) {
            return "volume";
        }
        if (TEMPERATURE_UNITS.contains(normalizedFirst) || TEMPERATURE_UNITS.contains(normalizedSecond)) {
            return "temperature";
        }
        return "unknown";
    }

    private static String normalizeUnit(String unit) {
        return unit == null ? "" : unit.toUpperCase(Locale.ROOT);
    }
}
