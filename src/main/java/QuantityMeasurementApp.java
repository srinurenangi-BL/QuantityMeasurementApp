public class QuantityMeasurementApp {
    public static void main(String[] args) {
        QuantityLength feetMeasurement = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inchesMeasurement = new QuantityLength(12.0, LengthUnit.INCHES);
        System.out.println("Input: Quantity(1.0, \"feet\") and Quantity(12.0, \"inches\")");
        System.out.println("Output: Equal (" + feetMeasurement.equals(inchesMeasurement) + ")");
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

    public static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
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
    }
}
