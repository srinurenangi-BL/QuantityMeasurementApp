public class QuantityMeasurementAppTest {
    public static void main(String[] args) {
        testFeetEqualitySameValue();
        testFeetEqualityDifferentValue();
        testFeetEqualityNullComparison();
        testFeetEqualityDifferentType();
        testFeetEqualitySameReference();

        testInchesEqualitySameValue();
        testInchesEqualityDifferentValue();
        testInchesEqualityNullComparison();
        testInchesEqualityDifferentType();
        testInchesEqualitySameReference();
        System.out.println("All tests passed.");
    }

    private static void testFeetEqualitySameValue() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet anotherFoot = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(oneFoot.equals(anotherFoot), "Expected 1.0 ft to be equal to 1.0 ft");
    }

    private static void testFeetEqualityDifferentValue() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet twoFeet = new QuantityMeasurementApp.Feet(2.0);
        assertFalse(oneFoot.equals(twoFeet), "Expected 1.0 ft to be different from 2.0 ft");
    }

    private static void testFeetEqualityNullComparison() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(oneFoot.equals(null), "Expected comparison with null to return false");
    }

    private static void testFeetEqualityDifferentType() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(oneFoot.equals("not-a-feet-value"), "Expected comparison with a different type to return false");
    }

    private static void testFeetEqualitySameReference() {
        QuantityMeasurementApp.Feet oneFoot = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(oneFoot.equals(oneFoot), "Expected an object to be equal to itself");
    }

    private static void testInchesEqualitySameValue() {
        QuantityMeasurementApp.Inches oneInch = new QuantityMeasurementApp.Inches(1.0);
        QuantityMeasurementApp.Inches anotherInch = new QuantityMeasurementApp.Inches(1.0);
        assertTrue(oneInch.equals(anotherInch), "Expected 1.0 inch to be equal to 1.0 inch");
    }

    private static void testInchesEqualityDifferentValue() {
        QuantityMeasurementApp.Inches oneInch = new QuantityMeasurementApp.Inches(1.0);
        QuantityMeasurementApp.Inches twoInches = new QuantityMeasurementApp.Inches(2.0);
        assertFalse(oneInch.equals(twoInches), "Expected 1.0 inch to be different from 2.0 inches");
    }

    private static void testInchesEqualityNullComparison() {
        QuantityMeasurementApp.Inches oneInch = new QuantityMeasurementApp.Inches(1.0);
        assertFalse(oneInch.equals(null), "Expected comparison with null to return false");
    }

    private static void testInchesEqualityDifferentType() {
        QuantityMeasurementApp.Inches oneInch = new QuantityMeasurementApp.Inches(1.0);
        assertFalse(oneInch.equals("not-an-inch-value"), "Expected comparison with a different type to return false");
    }

    private static void testInchesEqualitySameReference() {
        QuantityMeasurementApp.Inches oneInch = new QuantityMeasurementApp.Inches(1.0);
        assertTrue(oneInch.equals(oneInch), "Expected an object to be equal to itself");
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
