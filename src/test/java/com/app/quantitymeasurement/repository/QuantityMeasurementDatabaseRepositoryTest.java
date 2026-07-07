package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QuantityMeasurementDatabaseRepositoryTest {
    private QuantityMeasurementDatabaseRepository repository;

    @Before
    public void setUp() {
        repository = new QuantityMeasurementDatabaseRepository(
                "jdbc:h2:mem:qmtest;DB_CLOSE_DELAY=-1;MODE=LEGACY",
                "sa",
                "",
                1,
                2,
                250
        );
        repository.deleteAllMeasurements();
    }

    @After
    public void tearDown() {
        repository.releaseResources();
    }

    @Test
    public void shouldPersistAndRetrieveMeasurements() {
        repository.save(new QuantityMeasurementEntity(1.0, 12.0, "FEET", "INCHES", "length",
                "compare", "true", true, null));

        assertEquals(1, repository.getTotalCount());
        assertEquals(1, repository.findAll().size());
        assertEquals("length", repository.findAll().get(0).getMeasurementType());
        assertEquals(1, repository.getMeasurementsByOperation("compare").size());
        assertEquals(1, repository.getMeasurementsByType("length").size());
    }

    @Test
    public void shouldDeleteAllMeasurements() {
        repository.save(new QuantityMeasurementEntity(1.0, 1.0, "KILOGRAM", "GRAM", "weight",
                "add", "1001.0", true, null));

        repository.deleteAllMeasurements();

        assertEquals(0, repository.getTotalCount());
    }

    @Test
    public void shouldExposeConnectionPoolStatistics() {
        Map<String, Integer> statistics = repository.getPoolStatistics();

        assertTrue(statistics.get("idle") >= 1);
        assertEquals(0, (int) statistics.get("active"));
        assertEquals(2, (int) statistics.get("max"));
    }

    @Test
    public void shouldReturnReleasedConnectionsToPool() throws Exception {
        ConnectionPool pool = new ConnectionPool(
                "jdbc:h2:mem:qmpool;DB_CLOSE_DELAY=-1;MODE=LEGACY",
                "sa",
                "",
                1,
                1,
                250
        );
        Connection connection = pool.acquireConnection();

        assertEquals(1, (int) pool.getStatistics().get("active"));

        pool.releaseConnection(connection);

        assertEquals(0, (int) pool.getStatistics().get("active"));
        assertEquals(1, (int) pool.getStatistics().get("idle"));
        pool.closeAll();
    }
}
