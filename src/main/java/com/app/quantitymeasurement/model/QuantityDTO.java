package com.app.quantitymeasurement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Single quantity request or operation response")
public class QuantityDTO {
    @NotNull(message = "Value is required")
    @Schema(example = "1.0")
    private Double value;

    @NotBlank(message = "Unit is required")
    @Schema(example = "FEET")
    private String unit;

    @NotBlank(message = "Category is required")
    @Schema(example = "length", allowableValues = {"length", "weight", "volume", "temperature"})
    private String category;

    @Schema(example = "convert")
    private String operation;

    @Schema(example = "12.0")
    private String result;

    @Schema(example = "true")
    private boolean success;

    @Schema(example = "Unsupported category: time")
    private String errorMessage;

    public QuantityDTO() {
        this.success = true;
    }

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
        return value == null ? 0.0 : value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
