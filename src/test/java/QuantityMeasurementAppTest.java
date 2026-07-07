public class QuantityMeasurementAppTest {
    public static void main(String[] args) {
        testEqualityFeetToFeetSameValue();
        testEqualityInchToInchSameValue();
        testEqualityEquivalentValuesDifferentUnits();
        testEqualityFeetToFeetDifferentValue();
        testEqualityInchToInchDifferentValue();
        testEqualityYardToYardSameValue();
        testEqualityYardToYardDifferentValue();
        testEqualityYardToFeetEquivalentValue();
        testEqualityYardToInchesEquivalentValue();
        testEqualityCentimetersToInchesEquivalentValue();
        testEqualityCentimetersToFeetNonEquivalentValue();
        testEqualityMultiUnitTransitiveProperty();
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

    private static void testEqualityYardToYardSameValue() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength anotherYard = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        assertTrue(oneYard.equals(anotherYard), "Expected 1.0 yard to be equal to 1.0 yard");
    }

    private static void testEqualityYardToYardDifferentValue() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength twoYards = new QuantityMeasurementApp.QuantityLength(2.0, QuantityMeasurementApp.LengthUnit.YARDS);
        assertFalse(oneYard.equals(twoYards), "Expected 1.0 yard to be different from 2.0 yards");
    }

    private static void testEqualityYardToFeetEquivalentValue() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength threeFeet = new QuantityMeasurementApp.QuantityLength(3.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(oneYard.equals(threeFeet), "Expected 1 yard to equal 3 feet");
    }

    private static void testEqualityYardToInchesEquivalentValue() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength thirtySixInches = new QuantityMeasurementApp.QuantityLength(36.0, QuantityMeasurementApp.LengthUnit.INCHES);
        assertTrue(oneYard.equals(thirtySixInches), "Expected 1 yard to equal 36 inches");
    }

    private static void testEqualityCentimetersToInchesEquivalentValue() {
        QuantityMeasurementApp.QuantityLength oneCentimeter = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.CENTIMETERS);
        QuantityMeasurementApp.QuantityLength expectedInches = new QuantityMeasurementApp.QuantityLength(0.393701, QuantityMeasurementApp.LengthUnit.INCHES);
        assertTrue(oneCentimeter.equals(expectedInches), "Expected 1 centimeter to equal 0.393701 inches");
    }

    private static void testEqualityCentimetersToFeetNonEquivalentValue() {
        QuantityMeasurementApp.QuantityLength oneCentimeter = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.CENTIMETERS);
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertFalse(oneCentimeter.equals(oneFoot), "Expected 1 centimeter to be different from 1 foot");
    }

    private static void testEqualityMultiUnitTransitiveProperty() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength threeFeet = new QuantityMeasurementApp.QuantityLength(3.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.QuantityLength thirtySixInches = new QuantityMeasurementApp.QuantityLength(36.0, QuantityMeasurementApp.LengthUnit.INCHES);
        assertTrue(oneYard.equals(threeFeet), "Expected 1 yard to equal 3 feet");
        assertTrue(threeFeet.equals(thirtySixInches), "Expected 3 feet to equal 36 inches");
        assertTrue(oneYard.equals(thirtySixInches), "Expected 1 yard to equal 36 inches transitively");
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
