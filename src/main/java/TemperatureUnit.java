public enum TemperatureUnit implements IMeasurable {
    CELSIUS(1.0),
    FAHRENHEIT(1.0);

    private final double conversionFactor;

    TemperatureUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return switch (this) {
            case CELSIUS -> value;
            case FAHRENHEIT -> (value - 32.0) * 5.0 / 9.0;
        };
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return switch (this) {
            case CELSIUS -> baseValue;
            case FAHRENHEIT -> (baseValue * 9.0 / 5.0) + 32.0;
        };
    }

    @Override
    public String getUnitName() {
        return switch (this) {
            case CELSIUS -> "Celsius";
            case FAHRENHEIT -> "Fahrenheit";
        };
    }

    @Override
    public boolean supportsArithmetic() {
        return false;
    }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException("Temperature does not support " + operation + " operations");
    }
}
