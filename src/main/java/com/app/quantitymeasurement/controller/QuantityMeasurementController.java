package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.QuantityMeasurementRequest;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST endpoints for quantity comparison, conversion, arithmetic, and history")
public class QuantityMeasurementController {
    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
    }

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public ResponseEntity<QuantityDTO> compare(@Valid @RequestBody QuantityMeasurementRequest request) {
        return ResponseEntity.ok(performComparison(request.getFirst(), request.getSecond()));
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert one quantity to another unit")
    public ResponseEntity<QuantityDTO> convert(@Valid @RequestBody QuantityDTO source,
                                               @RequestParam String targetUnit) {
        return ResponseEntity.ok(performConversion(source, targetUnit));
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityDTO> add(@Valid @RequestBody QuantityMeasurementRequest request,
                                           @RequestParam(required = false) String targetUnit) {
        return ResponseEntity.ok(performAddition(request.getFirst(), request.getSecond(),
                resolveTargetUnit(targetUnit, request.getTargetUnit())));
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract the second quantity from the first")
    public ResponseEntity<QuantityDTO> subtract(@Valid @RequestBody QuantityMeasurementRequest request,
                                                @RequestParam(required = false) String targetUnit) {
        return ResponseEntity.ok(performSubtraction(request.getFirst(), request.getSecond(),
                resolveTargetUnit(targetUnit, request.getTargetUnit())));
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide one quantity by another")
    public ResponseEntity<QuantityDTO> divide(@Valid @RequestBody QuantityMeasurementRequest request) {
        return ResponseEntity.ok(performDivision(request.getFirst(), request.getSecond()));
    }

    @GetMapping
    @Operation(summary = "List stored measurement operations")
    public ResponseEntity<List<QuantityMeasurementEntity>> getAllMeasurements() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/operation/{operation}")
    @Operation(summary = "List measurement operations by operation type")
    public ResponseEntity<List<QuantityMeasurementEntity>> getMeasurementsByOperation(@PathVariable String operation) {
        return ResponseEntity.ok(service.findByOperation(operation));
    }

    @GetMapping("/type/{measurementType}")
    @Operation(summary = "List measurement operations by measurement type")
    public ResponseEntity<List<QuantityMeasurementEntity>> getMeasurementsByType(@PathVariable String measurementType) {
        return ResponseEntity.ok(service.findByMeasurementType(measurementType));
    }

    @GetMapping("/count")
    @Operation(summary = "Count all stored measurement operations")
    public ResponseEntity<Map<String, Long>> countMeasurements() {
        return ResponseEntity.ok(Map.of("count", service.count()));
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Count successful operations by type")
    public ResponseEntity<Map<String, Long>> countSuccessfulOperations(@PathVariable String operation) {
        return ResponseEntity.ok(Map.of("count", service.countSuccessfulOperations(operation)));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all stored measurement operations")
    public void deleteAllMeasurements() {
        service.deleteAll();
    }

    public QuantityDTO performComparison(QuantityDTO first, QuantityDTO second) {
        return service.compare(first, second);
    }

    public QuantityDTO performConversion(QuantityDTO source, String targetUnit) {
        return service.convert(source, targetUnit);
    }

    public QuantityDTO performAddition(QuantityDTO first, QuantityDTO second, String targetUnit) {
        return service.add(first, second, targetUnit);
    }

    public QuantityDTO performSubtraction(QuantityDTO first, QuantityDTO second, String targetUnit) {
        return service.subtract(first, second, targetUnit);
    }

    public QuantityDTO performDivision(QuantityDTO first, QuantityDTO second) {
        return service.divide(first, second);
    }

    private String resolveTargetUnit(String requestParamTargetUnit, String bodyTargetUnit) {
        if (requestParamTargetUnit != null && !requestParamTargetUnit.isBlank()) {
            return requestParamTargetUnit;
        }
        return bodyTargetUnit;
    }
}
