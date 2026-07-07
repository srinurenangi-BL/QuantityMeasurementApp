package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QuantityMeasurementControllerTest {
    @Test
    public void shouldDelegateComparisonToService() {
        QuantityMeasurementController controller = new QuantityMeasurementController(new StubService());

        QuantityDTO result = controller.performComparison(
                new QuantityDTO(1.0, "FEET", "length", "compare", null, true, null),
                new QuantityDTO(12.0, "INCHES", "length", "compare", null, true, null)
        );

        assertTrue(result.isSuccess());
        assertEquals("true", result.getResult());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullService() {
        new QuantityMeasurementController(null);
    }

    private static class StubService implements IQuantityMeasurementService {
        @Override
        public QuantityDTO compare(QuantityDTO first, QuantityDTO second) {
            return new QuantityDTO(first.getValue(), first.getUnit(), first.getCategory(),
                    "compare", "true", true, null);
        }

        @Override
        public QuantityDTO convert(QuantityDTO source, String targetUnit) {
            return source;
        }

        @Override
        public QuantityDTO add(QuantityDTO first, QuantityDTO second, String targetUnit) {
            return first;
        }

        @Override
        public QuantityDTO subtract(QuantityDTO first, QuantityDTO second, String targetUnit) {
            return first;
        }

        @Override
        public QuantityDTO divide(QuantityDTO first, QuantityDTO second) {
            return first;
        }
    }
}
