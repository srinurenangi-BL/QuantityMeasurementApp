public class QuantityMeasurementApp {
    public static void main(String[] args) {
        QuantityLength feetMeasurement = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inchesMeasurement = new QuantityLength(12.0, LengthUnit.INCHES);
        System.out.println("Input: Quantity(1.0, \"feet\") and Quantity(12.0, \"inches\")");
        System.out.println("Output: Equal (" + feetMeasurement.equals(inchesMeasurement) + ")");
        System.out.println("Input: convert(1.0, FEET, INCHES)");
        System.out.println("Output: " + convert(1.0, LengthUnit.FEET, LengthUnit.INCHES));
    }

    public enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.393701 / 12.0);

        private final double conversionFactorToFeet;

        LengthUnit(double conversionFactorToFeet) {
            this.conversionFactorToFeet = conversionFactorToFeet;
        }

        public double getConversionFactorToFeet() {
            return conversionFactorToFeet;
        }
    }

    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        validateValue(value);
        validateUnit(sourceUnit, "sourceUnit");
        validateUnit(targetUnit, "targetUnit");

        if (sourceUnit == targetUnit) {
            return value;
        }

        double valueInFeet = value * sourceUnit.getConversionFactorToFeet();
        return valueInFeet / targetUnit.getConversionFactorToFeet();
    }

    public static QuantityLength add(QuantityLength first, QuantityLength second, LengthUnit targetUnit) {
        validateQuantity(first, "first");
        validateQuantity(second, "second");
        validateUnit(targetUnit, "targetUnit");

        double totalInFeet = first.toFeet() + second.toFeet();
        double convertedValue = convert(totalInFeet, LengthUnit.FEET, targetUnit);
        return new QuantityLength(convertedValue, targetUnit);
    }

    public static QuantityLength add(double firstValue, LengthUnit firstUnit, double secondValue, LengthUnit secondUnit, LengthUnit targetUnit) {
        return add(new QuantityLength(firstValue, firstUnit), new QuantityLength(secondValue, secondUnit), targetUnit);
    }

    public static class QuantityLength {
        final double value;
        final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        public double convertTo(LengthUnit targetUnit) {
            return convert(this.value, this.unit, targetUnit);
        }

        public double toFeet() {
            return convert(this.value, this.unit, LengthUnit.FEET);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            QuantityLength other = (QuantityLength) obj;
            double thisInFeet = this.value * this.unit.getConversionFactorToFeet();
            double otherInFeet = other.value * other.unit.getConversionFactorToFeet();
            return Double.compare(thisInFeet, otherInFeet) == 0;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    private static void validateValue(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
    }

    private static void validateUnit(LengthUnit unit, String parameterName) {
        if (unit == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null");
        }
    }

    private static void validateQuantity(QuantityLength quantity, String parameterName) {
        if (quantity == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null");
        }
        validateValue(quantity.value);
        validateUnit(quantity.unit, parameterName + ".unit");
    }
}
