public interface IMeasurable {
    @FunctionalInterface
    interface SupportsArithmetic {
        boolean isSupported();
    }

    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();

    default boolean supportsArithmetic() {
        return true;
    }

    default void validateOperationSupport(String operation) {
        if (!supportsArithmetic()) {
            throw new UnsupportedOperationException("This unit does not support " + operation + " operations");
        }
    }
}
