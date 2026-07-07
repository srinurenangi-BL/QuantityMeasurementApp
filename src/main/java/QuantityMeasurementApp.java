public class QuantityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("Input: 1.0 inch and 1.0 inch");
        System.out.println("Output: Equal (" + areInchesEqual(1.0, 1.0) + ")");

        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + areFeetEqual(1.0, 1.0) + ")");
    }

    public static boolean areFeetEqual(double firstValue, double secondValue) {
        Feet firstFoot = new Feet(firstValue);
        Feet secondFoot = new Feet(secondValue);
        return firstFoot.equals(secondFoot);
    }

    public static boolean areInchesEqual(double firstValue, double secondValue) {
        Inches firstInch = new Inches(firstValue);
        Inches secondInch = new Inches(secondValue);
        return firstInch.equals(secondInch);
    }

    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }
}
