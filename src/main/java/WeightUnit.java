public enum WeightUnit implements IMeasurable {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactorToKilograms;

    WeightUnit(double conversionFactorToKilograms) {
        this.conversionFactorToKilograms = conversionFactorToKilograms;
    }

    public double getConversionFactor() {
        return conversionFactorToKilograms;
    }

    public double convertToBaseUnit(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        return value * conversionFactorToKilograms;
    }

    public double convertFromBaseUnit(double baseValue) {
        if (!Double.isFinite(baseValue)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        return baseValue / conversionFactorToKilograms;
    }

    public String getUnitName() {
        return name();
    }
}
