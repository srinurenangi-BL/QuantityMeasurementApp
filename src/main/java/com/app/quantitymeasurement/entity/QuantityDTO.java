package com.app.quantitymeasurement.entity;

public class QuantityDTO {
    private final double value;
    private final String unit;
    private final String category;
    private final String operation;
    private final String result;
    private final boolean success;
    private final String errorMessage;

    public QuantityDTO(double value, String unit, String category, String operation, String result, boolean success, String errorMessage) {
        this.value = value;
        this.unit = unit;
        this.category = category;
        this.operation = operation;
        this.result = result;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getCategory() {
        return category;
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
}
