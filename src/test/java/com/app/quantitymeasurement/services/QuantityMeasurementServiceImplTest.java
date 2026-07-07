package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:service-test;DB_CLOSE_DELAY=-1;MODE=LEGACY",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class QuantityMeasurementServiceImplTest {
    @Autowired
    private IQuantityMeasurementService service;

    @Autowired
    private QuantityMeasurementRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldPersistComparisonThroughJpaRepository() {
        QuantityDTO result = service.compare(
                new QuantityDTO(1.0, "FEET", "length", "compare", null, true, null),
                new QuantityDTO(12.0, "INCHES", "length", "compare", null, true, null)
        );

        assertTrue(result.isSuccess());
        assertEquals("true", result.getResult());
        assertEquals(1, repository.count());
        assertEquals("length", repository.findAll().get(0).getMeasurementType());
    }

    @Test
    void shouldPersistConversionThroughJpaRepository() {
        QuantityDTO result = service.convert(
                new QuantityDTO(1.0, "FEET", "length", "convert", null, true, null),
                "INCHES"
        );

        assertTrue(result.isSuccess());
        assertEquals("12.0", result.getResult());
        assertEquals("convert", repository.findAll().get(0).getOperation());
    }
}
