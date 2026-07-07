package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {
    private static final String DATA_FILE = "quantity-measurements.ser";
    private static QuantityMeasurementCacheRepository instance;
    private final List<QuantityMeasurementEntity> cache = new ArrayList<>();

    private QuantityMeasurementCacheRepository() {
        loadFromDisk();
    }

    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        cache.add(entity);
        saveToDisk();
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        return Collections.unmodifiableList(cache);
    }

    @Override
    public void deleteAllMeasurements() {
        cache.clear();
        File file = new File(DATA_FILE);
        if (file.exists() && !file.delete()) {
            throw new QuantityMeasurementException("Unable to delete measurement history");
        }
    }

    private void saveToDisk() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(new ArrayList<>(cache));
        } catch (IOException e) {
            throw new QuantityMeasurementException("Unable to save measurement history", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromDisk() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object persisted = in.readObject();
            if (persisted instanceof List<?>) {
                for (Object entry : (List<?>) persisted) {
                    if (entry instanceof QuantityMeasurementEntity) {
                        cache.add((QuantityMeasurementEntity) entry);
                    }
                }
                return;
            }
            if (persisted instanceof QuantityMeasurementEntity) {
                cache.add((QuantityMeasurementEntity) persisted);
                while (true) {
                    Object entity = in.readObject();
                    if (entity instanceof QuantityMeasurementEntity) {
                        cache.add((QuantityMeasurementEntity) entity);
                    }
                }
            }
        } catch (EOFException ignored) {
            // expected
        } catch (IOException | ClassNotFoundException e) {
            throw new QuantityMeasurementException("Unable to load measurement history", e);
        }
    }
}
