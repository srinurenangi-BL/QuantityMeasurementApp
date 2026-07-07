public class QuantityMeasurementController {
    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
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
}
