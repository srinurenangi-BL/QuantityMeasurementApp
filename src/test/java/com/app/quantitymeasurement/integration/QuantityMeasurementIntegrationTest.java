package com.app.quantitymeasurement.integration;

import com.app.quantitymeasurement.QuantityMeasurementApp;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QuantityMeasurementIntegrationTest {
    private QuantityMeasurementDatabaseRepository repository;

    @Before
    public void setUp() {
        repository = new QuantityMeasurementDatabaseRepository(
                "jdbc:h2:mem:qmintegration;DB_CLOSE_DELAY=-1;MODE=LEGACY",
                "sa",
                "",
                1,
                4,
                500
        );
        repository.deleteAllMeasurements();
        QuantityMeasurementApp.initialize(repository);
    }

    @After
    public void tearDown() {
        QuantityMeasurementApp.closeResources();
    }

    @Test
    public void shouldPersistControllerServiceOperationToDatabase() {
        QuantityDTO result = QuantityMeasurementApp.getController().performAddition(
                new QuantityDTO(1.0, "FEET", "length", "add", null, true, null),
                new QuantityDTO(12.0, "INCHES", "length", "add", null, true, null),
                "FEET"
        );

        assertTrue(result.isSuccess());
        assertEquals("2.0", result.getResult());
        assertEquals(1, repository.getTotalCount());
        assertEquals(1, repository.getMeasurementsByType("length").size());
        assertEquals(1, repository.getMeasurementsByOperation("add").size());
    }
}
