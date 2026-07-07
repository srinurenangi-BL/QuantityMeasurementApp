package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:repository-test;DB_CLOSE_DELAY=-1;MODE=LEGACY",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class QuantityMeasurementRepositoryTest {
    @Autowired
    private QuantityMeasurementRepository repository;

    @Test
    void shouldPersistAndQueryMeasurements() {
        repository.save(new QuantityMeasurementEntity(1.0, 12.0, "FEET", "INCHES", "length",
                "compare", "true", true, null));
        repository.save(new QuantityMeasurementEntity(1.0, 1000.0, "KILOGRAM", "GRAM", "weight",
                "add", "2.0", true, null));
        repository.save(new QuantityMeasurementEntity(1.0, 1.0, "LITRE", "UNKNOWN", "volume",
                "convert", null, false, "Unsupported unit"));

        assertEquals(1, repository.findByOperationIgnoreCase("compare").size());
        assertEquals(1, repository.findByMeasurementTypeIgnoreCase("weight").size());
        assertEquals(3, repository.findByCreatedAtAfter(LocalDateTime.now().minusMinutes(1)).size());
        assertEquals(1, repository.findByErrorTrue().size());
        assertEquals(1, repository.countByOperationIgnoreCaseAndErrorFalse("add"));
        assertEquals(1, repository.findSuccessfulOperationHistory("compare").size());
    }
}
