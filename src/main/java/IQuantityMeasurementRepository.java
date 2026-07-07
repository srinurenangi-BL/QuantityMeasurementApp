public interface IQuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);

    java.util.List<QuantityMeasurementEntity> findAll();
}
