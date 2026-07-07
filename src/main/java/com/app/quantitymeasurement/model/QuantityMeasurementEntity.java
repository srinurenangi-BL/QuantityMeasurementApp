package com.app.quantitymeasurement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(
        name = "quantity_measurements",
        indexes = {
                @Index(name = "idx_quantity_measurements_operation", columnList = "operation"),
                @Index(name = "idx_quantity_measurements_type", columnList = "measurement_type"),
                @Index(name = "idx_quantity_measurements_created_at", columnList = "created_at")
        }
)
public class QuantityMeasurementEntity {
    private static final Set<String> LENGTH_UNITS = Set.of("FEET", "INCHES", "YARDS", "CENTIMETERS");
    private static final Set<String> WEIGHT_UNITS = Set.of("KILOGRAM", "GRAM", "POUND");
    private static final Set<String> VOLUME_UNITS = Set.of("LITRE", "MILLILITRE", "GALLON");
    private static final Set<String> TEMPERATURE_UNITS = Set.of("CELSIUS", "FAHRENHEIT");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_value", nullable = false)
    private double firstValue;

    @Column(name = "second_value", nullable = false)
    private double secondValue;

    @Column(name = "first_unit", nullable = false, length = 50)
    private String firstUnit;

    @Column(name = "second_unit", length = 50)
    private String secondUnit;

    @Column(name = "measurement_type", nullable = false, length = 50)
    private String measurementType;

    @Column(nullable = false, length = 50)
    private String operation;

    @Column(length = 100)
    private String result;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "is_error", nullable = false)
    private boolean error;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected QuantityMeasurementEntity() {
        // Required by JPA.
    }

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
        this.firstUnit = normalizeUnitName(firstUnit);
        this.secondUnit = normalizeUnitName(secondUnit);
        this.measurementType = normalizeMeasurementType(measurementType, firstUnit, secondUnit);
        this.operation = normalizeOperation(operation);
        this.result = result;
        this.success = success;
        this.error = !success;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.updatedAt = this.createdAt;
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
        error = !success;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
        error = !success;
    }

    public Long getId() {
        return id;
    }

    public double getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(double firstValue) {
        this.firstValue = firstValue;
    }

    public double getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(double secondValue) {
        this.secondValue = secondValue;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public void setFirstUnit(String firstUnit) {
        this.firstUnit = normalizeUnitName(firstUnit);
        this.measurementType = normalizeMeasurementType(measurementType, this.firstUnit, secondUnit);
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public void setSecondUnit(String secondUnit) {
        this.secondUnit = normalizeUnitName(secondUnit);
        this.measurementType = normalizeMeasurementType(measurementType, firstUnit, this.secondUnit);
    }

    public String getMeasurementType() {
        return normalizeMeasurementType(measurementType, firstUnit, secondUnit);
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = normalizeMeasurementType(measurementType, firstUnit, secondUnit);
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = normalizeOperation(operation);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
        this.error = !success;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
        this.success = !error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private static String normalizeMeasurementType(String measurementType, String firstUnit, String secondUnit) {
        if (measurementType != null && !measurementType.isBlank()) {
            return measurementType.toLowerCase(Locale.ROOT);
        }
        return inferMeasurementType(firstUnit, secondUnit);
    }

    private static String inferMeasurementType(String firstUnit, String secondUnit) {
        String normalizedFirst = normalizeUnitName(firstUnit);
        String normalizedSecond = normalizeUnitName(secondUnit);
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

    private static String normalizeUnitName(String unit) {
        return unit == null ? null : unit.toUpperCase(Locale.ROOT);
    }

    private static String normalizeOperation(String operation) {
        return operation == null ? null : operation.toLowerCase(Locale.ROOT);
    }
}
