public class QuantityMeasurementApp {
    public static void main(String[] args) {
        Feet oneFoot = new Feet(1.0);
        Feet anotherFoot = new Feet(1.0);
        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + oneFoot.equals(anotherFoot) + ")");
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
}
