package com.app.quantitymeasurement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Two-quantity arithmetic or comparison request")
public class QuantityMeasurementRequest {
    @Valid
    @NotNull(message = "First quantity is required")
    private QuantityDTO first;

    @Valid
    @NotNull(message = "Second quantity is required")
    private QuantityDTO second;

    @Schema(example = "INCHES")
    private String targetUnit;

    public QuantityMeasurementRequest() {
    }

    public QuantityMeasurementRequest(QuantityDTO first, QuantityDTO second, String targetUnit) {
        this.first = first;
        this.second = second;
        this.targetUnit = targetUnit;
    }

    public QuantityDTO getFirst() {
        return first;
    }

    public void setFirst(QuantityDTO first) {
        this.first = first;
    }

    public QuantityDTO getSecond() {
        return second;
    }

    public void setSecond(QuantityDTO second) {
        this.second = second;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }
}
