public class QuantityMeasurementAppTest {
    public static void main(String[] args) {
        testEqualityFeetToFeetSameValue();
        testEqualityInchToInchSameValue();
        testEqualityEquivalentValuesDifferentUnits();
        testEqualityFeetToFeetDifferentValue();
        testEqualityInchToInchDifferentValue();
        testEqualityNullComparison();
        testEqualitySameReference();
        testEqualityNullUnit();
        System.out.println("All tests passed.");
    }

    private static void testEqualityFeetToFeetSameValue() {
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.QuantityLength anotherFoot = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(oneFoot.equals(anotherFoot), "Expected 1.0 feet to be equal to 1.0 feet");
    }

    private static void testEqualityInchToInchSameValue() {
        QuantityMeasurementApp.QuantityLength oneInch = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCHES);
        QuantityMeasurementApp.QuantityLength anotherInch = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCHES);
        assertTrue(oneInch.equals(anotherInch), "Expected 1.0 inch to be equal to 1.0 inch");
    }

    private static void testEqualityEquivalentValuesDifferentUnits() {
        QuantityMeasurementApp.QuantityLength twelveInches = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCHES);
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(twelveInches.equals(oneFoot), "Expected 12 inches to equal 1 foot");
    }

    private static void testEqualityFeetToFeetDifferentValue() {
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.QuantityLength twoFeet = new QuantityMeasurementApp.QuantityLength(2.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertFalse(oneFoot.equals(twoFeet), "Expected 1.0 feet to be different from 2.0 feet");
    }

    private static void testEqualityInchToInchDifferentValue() {
        QuantityMeasurementApp.QuantityLength oneInch = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCHES);
        QuantityMeasurementApp.QuantityLength twoInches = new QuantityMeasurementApp.QuantityLength(2.0, QuantityMeasurementApp.LengthUnit.INCHES);
        assertFalse(oneInch.equals(twoInches), "Expected 1.0 inch to be different from 2.0 inches");
    }

    private static void testEqualityNullComparison() {
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertFalse(oneFoot.equals(null), "Expected comparison with null to return false");
    }

    private static void testEqualitySameReference() {
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(oneFoot.equals(oneFoot), "Expected an object to be equal to itself");
    }

    private static void testEqualityNullUnit() {
        try {
            new QuantityMeasurementApp.QuantityLength(1.0, null);
            throw new AssertionError("Expected null unit to be rejected");
        } catch (IllegalArgumentException expected) {
            // expected
        }
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
