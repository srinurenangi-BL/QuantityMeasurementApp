public class QuantityMeasurementAppTest {
    public static void main(String[] args) {
        testEqualitySameValue();
        testEqualityDifferentValue();
        testEqualityNullComparison();
        testEqualityDifferentType();
        testEqualitySameReference();
        System.out.println("All tests passed.");
    }

    private static void testEqualitySameValue() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet anotherFoot = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(oneFoot.equals(anotherFoot), "Expected 1.0 ft to be equal to 1.0 ft");
    }

    private static void testEqualityDifferentValue() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet twoFeet = new QuantityMeasurementApp.Feet(2.0);
        assertFalse(oneFoot.equals(twoFeet), "Expected 1.0 ft to be different from 2.0 ft");
    }

    private static void testEqualityNullComparison() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(oneFoot.equals(null), "Expected comparison with null to return false");
    }

    private static void testEqualityDifferentType() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(oneFoot.equals("not-a-feet-value"), "Expected comparison with a different type to return false");
    }

    private static void testEqualitySameReference() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(oneFoot.equals(oneFoot), "Expected an object to be equal to itself");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }
}
