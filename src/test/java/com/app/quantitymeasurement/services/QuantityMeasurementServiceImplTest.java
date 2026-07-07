package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QuantityMeasurementServiceImplTest {
    @Test
    public void shouldPersistComparisonThroughInjectedRepository() {
        InMemoryRepository repository = new InMemoryRepository();
        QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(repository);

        QuantityDTO result = service.compare(
                new QuantityDTO(1.0, "FEET", "length", "compare", null, true, null),
                new QuantityDTO(12.0, "INCHES", "length", "compare", null, true, null)
        );

        assertTrue(result.isSuccess());
        assertEquals("true", result.getResult());
        assertEquals(1, repository.findAll().size());
        assertEquals("length", repository.findAll().get(0).getMeasurementType());
    }

    @Test
    public void shouldPersistConversionThroughInjectedRepository() {
        InMemoryRepository repository = new InMemoryRepository();
        QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(repository);

        QuantityDTO result = service.convert(
                new QuantityDTO(1.0, "FEET", "length", "convert", null, true, null),
                "INCHES"
        );

        assertTrue(result.isSuccess());
        assertEquals("12.0", result.getResult());
        assertEquals("convert", repository.findAll().get(0).getOperation());
    }

    private static class InMemoryRepository implements IQuantityMeasurementRepository {
        private final List<QuantityMeasurementEntity> measurements = new ArrayList<>();

        @Override
        public void save(QuantityMeasurementEntity entity) {
            measurements.add(entity);
        }

        @Override
        public List<QuantityMeasurementEntity> findAll() {
            return measurements;
        }

        @Override
        public void deleteAllMeasurements() {
            measurements.clear();
        }
    }
}
