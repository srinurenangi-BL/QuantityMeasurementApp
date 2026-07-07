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

        testConversionFeetToInches();
        testConversionInchesToFeet();
        testConversionYardsToInches();
        testConversionInchesToYards();
        testConversionCentimetersToInches();
        testConversionFeetToYard();
        testConversionRoundTripPreservesValue();
        testConversionZeroValue();
        testConversionNegativeValue();
        testConversionInvalidUnitThrows();
        testConversionNaNOrInfiniteThrows();
        testConversionSameUnitReturnsOriginalValue();

        testAdditionSameUnitFeet();
        testAdditionSameUnitInches();
        testAdditionCrossUnitFeetAndInches();
        testAdditionCrossUnitInchesAndFeet();
        testAdditionCrossUnitYardAndFeet();
        testAdditionCrossUnitCentimeterAndInch();
        testAdditionCommutativity();
        testAdditionWithZero();
        testAdditionNegativeValues();
        testAdditionNullSecondOperand();
        testAdditionLargeValues();
        testAdditionSmallValues();

        testAdditionExplicitTargetUnitFeet();
        testAdditionExplicitTargetUnitInches();
        testAdditionExplicitTargetUnitYards();
        testAdditionExplicitTargetUnitCentimeters();
        testAdditionExplicitTargetUnitSameAsFirstOperand();
        testAdditionExplicitTargetUnitSameAsSecondOperand();
        testAdditionExplicitTargetUnitCommutativity();
        testAdditionExplicitTargetUnitWithZero();
        testAdditionExplicitTargetUnitNegativeValues();
        testAdditionExplicitTargetUnitNullTargetUnit();
        testAdditionExplicitTargetUnitLargeToSmallScale();
        testAdditionExplicitTargetUnitSmallToLargeScale();
        System.out.println("All tests passed.");
    }

    private static void testEqualityFeetToFeetSameValue() {
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET);
        QuantityMeasurementApp.QuantityLength anotherFoot = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(oneFoot.equals(anotherFoot), "Expected 1.0 feet to be equal to 1.0 feet");
    }

    private static void testEqualityInchToInchSameValue() {
        QuantityMeasurementApp.QuantityLength oneInch = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.INCHES);
        QuantityMeasurementApp.QuantityLength anotherInch = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.INCHES);
        assertTrue(oneInch.equals(anotherInch), "Expected 1.0 inch to be equal to 1.0 inch");
    }

    private static void testEqualityEquivalentValuesDifferentUnits() {
        QuantityMeasurementApp.QuantityLength twelveInches = new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES);
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(twelveInches.equals(oneFoot), "Expected 12 inches to equal 1 foot");
    }

    private static void testEqualityFeetToFeetDifferentValue() {
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET);
        QuantityMeasurementApp.QuantityLength twoFeet = new QuantityMeasurementApp.QuantityLength(2.0, LengthUnit.FEET);
        assertFalse(oneFoot.equals(twoFeet), "Expected 1.0 feet to be different from 2.0 feet");
    }

    private static void testEqualityInchToInchDifferentValue() {
        QuantityMeasurementApp.QuantityLength oneInch = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.INCHES);
        QuantityMeasurementApp.QuantityLength twoInches = new QuantityMeasurementApp.QuantityLength(2.0, LengthUnit.INCHES);
        assertFalse(oneInch.equals(twoInches), "Expected 1.0 inch to be different from 2.0 inches");
    }

    private static void testEqualityYardToYardSameValue() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength anotherYard = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.YARDS);
        assertTrue(oneYard.equals(anotherYard), "Expected 1.0 yard to be equal to 1.0 yard");
    }

    private static void testEqualityYardToYardDifferentValue() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength twoYards = new QuantityMeasurementApp.QuantityLength(2.0, LengthUnit.YARDS);
        assertFalse(oneYard.equals(twoYards), "Expected 1.0 yard to be different from 2.0 yards");
    }

    private static void testEqualityYardToFeetEquivalentValue() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength threeFeet = new QuantityMeasurementApp.QuantityLength(3.0, LengthUnit.FEET);
        assertTrue(oneYard.equals(threeFeet), "Expected 1 yard to equal 3 feet");
    }

    private static void testEqualityYardToInchesEquivalentValue() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength thirtySixInches = new QuantityMeasurementApp.QuantityLength(36.0, LengthUnit.INCHES);
        assertTrue(oneYard.equals(thirtySixInches), "Expected 1 yard to equal 36 inches");
    }

    private static void testEqualityCentimetersToInchesEquivalentValue() {
        QuantityMeasurementApp.QuantityLength oneCentimeter = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityMeasurementApp.QuantityLength expectedInches = new QuantityMeasurementApp.QuantityLength(0.393701, LengthUnit.INCHES);
        assertTrue(oneCentimeter.equals(expectedInches), "Expected 1 centimeter to equal 0.393701 inches");
    }

    private static void testEqualityCentimetersToFeetNonEquivalentValue() {
        QuantityMeasurementApp.QuantityLength oneCentimeter = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(oneCentimeter.equals(oneFoot), "Expected 1 centimeter to be different from 1 foot");
    }

    private static void testEqualityMultiUnitTransitiveProperty() {
        QuantityMeasurementApp.QuantityLength oneYard = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength threeFeet = new QuantityMeasurementApp.QuantityLength(3.0, LengthUnit.FEET);
        QuantityMeasurementApp.QuantityLength thirtySixInches = new QuantityMeasurementApp.QuantityLength(36.0, LengthUnit.INCHES);
        assertTrue(oneYard.equals(threeFeet), "Expected 1 yard to equal 3 feet");
        assertTrue(threeFeet.equals(thirtySixInches), "Expected 3 feet to equal 36 inches");
        assertTrue(oneYard.equals(thirtySixInches), "Expected 1 yard to equal 36 inches transitively");
    }

    private static void testEqualityNullComparison() {
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(oneFoot.equals(null), "Expected comparison with null to return false");
    }

    private static void testEqualitySameReference() {
        QuantityMeasurementApp.QuantityLength oneFoot = new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET);
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

    private static void testConversionFeetToInches() {
        double result = QuantityMeasurementApp.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(12.0, result, 1e-6, "Expected 1 foot to convert to 12 inches");
    }

    private static void testConversionInchesToFeet() {
        double result = QuantityMeasurementApp.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET);
        assertEquals(2.0, result, 1e-6, "Expected 24 inches to convert to 2 feet");
    }

    private static void testConversionYardsToInches() {
        double result = QuantityMeasurementApp.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES);
        assertEquals(36.0, result, 1e-6, "Expected 1 yard to convert to 36 inches");
    }

    private static void testConversionInchesToYards() {
        double result = QuantityMeasurementApp.convert(72.0, LengthUnit.INCHES, LengthUnit.YARDS);
        assertEquals(2.0, result, 1e-6, "Expected 72 inches to convert to 2 yards");
    }

    private static void testConversionCentimetersToInches() {
        double result = QuantityMeasurementApp.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        assertEquals(1.0, result, 1e-6, "Expected 2.54 centimeters to convert to 1 inch");
    }

    private static void testConversionFeetToYard() {
        double result = QuantityMeasurementApp.convert(6.0, LengthUnit.FEET, LengthUnit.YARDS);
        assertEquals(2.0, result, 1e-6, "Expected 6 feet to convert to 2 yards");
    }

    private static void testConversionRoundTripPreservesValue() {
        double result = QuantityMeasurementApp.convert(QuantityMeasurementApp.convert(5.0, LengthUnit.FEET, LengthUnit.INCHES), LengthUnit.INCHES, LengthUnit.FEET);
        assertEquals(5.0, result, 1e-6, "Round-trip conversion should preserve the original value");
    }

    private static void testConversionZeroValue() {
        double result = QuantityMeasurementApp.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(0.0, result, 1e-6, "Zero should convert to zero");
    }

    private static void testConversionNegativeValue() {
        double result = QuantityMeasurementApp.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(-12.0, result, 1e-6, "Negative values should preserve sign");
    }

    private static void testConversionInvalidUnitThrows() {
        try {
            QuantityMeasurementApp.convert(1.0, null, LengthUnit.FEET);
            throw new AssertionError("Expected null source unit to be rejected");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    private static void testConversionNaNOrInfiniteThrows() {
        try {
            QuantityMeasurementApp.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
            throw new AssertionError("Expected NaN to be rejected");
        } catch (IllegalArgumentException expected) {
            // expected
        }

        try {
            QuantityMeasurementApp.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES);
            throw new AssertionError("Expected infinite value to be rejected");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    private static void testConversionSameUnitReturnsOriginalValue() {
        double result = QuantityMeasurementApp.convert(5.0, LengthUnit.FEET, LengthUnit.FEET);
        assertEquals(5.0, result, 1e-6, "Converting to the same unit should return the original value");
    }

    private static void testAdditionSameUnitFeet() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(2.0, LengthUnit.FEET), LengthUnit.FEET);
        assertQuantity(result, 3.0, LengthUnit.FEET, "Expected 1 foot + 2 feet = 3 feet");
    }

    private static void testAdditionSameUnitInches() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(6.0, LengthUnit.INCHES), new QuantityMeasurementApp.QuantityLength(6.0, LengthUnit.INCHES), LengthUnit.INCHES);
        assertQuantity(result, 12.0, LengthUnit.INCHES, "Expected 6 inches + 6 inches = 12 inches");
    }

    private static void testAdditionCrossUnitFeetAndInches() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        assertQuantity(result, 2.0, LengthUnit.FEET, "Expected 1 foot + 12 inches = 2 feet");
    }

    private static void testAdditionCrossUnitInchesAndFeet() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), LengthUnit.INCHES);
        assertQuantity(result, 24.0, LengthUnit.INCHES, "Expected 12 inches + 1 foot = 24 inches");
    }

    private static void testAdditionCrossUnitYardAndFeet() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.YARDS), new QuantityMeasurementApp.QuantityLength(3.0, LengthUnit.FEET), LengthUnit.YARDS);
        assertQuantity(result, 2.0, LengthUnit.YARDS, "Expected 1 yard + 3 feet = 2 yards");
    }

    private static void testAdditionCrossUnitCentimeterAndInch() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(2.54, LengthUnit.CENTIMETERS), new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.INCHES), LengthUnit.CENTIMETERS);
        assertEquals(5.08, result.value, 1e-3, "Expected 2.54 cm + 1 inch = 5.08 cm");
        if (result.unit != LengthUnit.CENTIMETERS) {
            throw new AssertionError("Expected result unit to be CENTIMETERS");
        }
    }

    private static void testAdditionCommutativity() {
        QuantityMeasurementApp.QuantityLength left = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        QuantityMeasurementApp.QuantityLength right = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), LengthUnit.FEET);
        assertQuantity(left, right.value, right.unit, "Addition should be commutative");
    }

    private static void testAdditionWithZero() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(5.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(0.0, LengthUnit.INCHES), LengthUnit.FEET);
        assertQuantity(result, 5.0, LengthUnit.FEET, "Adding zero should preserve the value");
    }

    private static void testAdditionNegativeValues() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(5.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.FEET);
        assertQuantity(result, 3.0, LengthUnit.FEET, "Expected 5 feet + (-2 feet) = 3 feet");
    }

    private static void testAdditionNullSecondOperand() {
        try {
            QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), null, LengthUnit.FEET);
            throw new AssertionError("Expected null operand to be rejected");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    private static void testAdditionLargeValues() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1e6, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(1e6, LengthUnit.FEET), LengthUnit.FEET);
        assertQuantity(result, 2e6, LengthUnit.FEET, "Expected large values to add correctly");
    }

    private static void testAdditionSmallValues() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(0.001, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(0.002, LengthUnit.FEET), LengthUnit.FEET);
        assertQuantity(result, 0.003, LengthUnit.FEET, "Expected small values to add correctly");
    }

    private static void testAdditionExplicitTargetUnitFeet() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        assertQuantity(result, 2.0, LengthUnit.FEET, "Expected explicit target unit FEET to produce 2 feet");
    }

    private static void testAdditionExplicitTargetUnitInches() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.INCHES);
        assertQuantity(result, 24.0, LengthUnit.INCHES, "Expected explicit target unit INCHES to produce 24 inches");
    }

    private static void testAdditionExplicitTargetUnitYards() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.YARDS);
        assertEquals(0.6666667, result.value, 1e-4, "Expected explicit target unit YARDS to produce about 0.667 yards");
        if (result.unit != LengthUnit.YARDS) {
            throw new AssertionError("Expected result unit to be YARDS");
        }
    }

    private static void testAdditionExplicitTargetUnitCentimeters() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.INCHES), new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.INCHES), LengthUnit.CENTIMETERS);
        assertEquals(5.08, result.value, 1e-3, "Expected explicit target unit CENTIMETERS to produce about 5.08 cm");
        if (result.unit != LengthUnit.CENTIMETERS) {
            throw new AssertionError("Expected result unit to be CENTIMETERS");
        }
    }

    private static void testAdditionExplicitTargetUnitSameAsFirstOperand() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(2.0, LengthUnit.YARDS), new QuantityMeasurementApp.QuantityLength(3.0, LengthUnit.FEET), LengthUnit.YARDS);
        assertQuantity(result, 3.0, LengthUnit.YARDS, "Expected target unit matching first operand to yield 3 yards");
    }

    private static void testAdditionExplicitTargetUnitSameAsSecondOperand() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(2.0, LengthUnit.YARDS), new QuantityMeasurementApp.QuantityLength(3.0, LengthUnit.FEET), LengthUnit.FEET);
        assertQuantity(result, 9.0, LengthUnit.FEET, "Expected target unit matching second operand to yield 9 feet");
    }

    private static void testAdditionExplicitTargetUnitCommutativity() {
        QuantityMeasurementApp.QuantityLength left = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.YARDS);
        QuantityMeasurementApp.QuantityLength right = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), LengthUnit.YARDS);
        assertEquals(left.value, right.value, 1e-6, "Addition should be commutative for explicit target units");
    }

    private static void testAdditionExplicitTargetUnitWithZero() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(5.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(0.0, LengthUnit.INCHES), LengthUnit.YARDS);
        assertEquals(1.6666667, result.value, 1e-4, "Expected 5 feet + 0 inches to equal about 1.667 yards");
        if (result.unit != LengthUnit.YARDS) {
            throw new AssertionError("Expected result unit to be YARDS");
        }
    }

    private static void testAdditionExplicitTargetUnitNegativeValues() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(5.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.INCHES);
        assertQuantity(result, 36.0, LengthUnit.INCHES, "Expected 5 feet + (-2 feet) to produce 36 inches");
    }

    private static void testAdditionExplicitTargetUnitNullTargetUnit() {
        try {
            QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), null);
            throw new AssertionError("Expected null target unit to be rejected");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    private static void testAdditionExplicitTargetUnitLargeToSmallScale() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(1000.0, LengthUnit.FEET), new QuantityMeasurementApp.QuantityLength(500.0, LengthUnit.FEET), LengthUnit.INCHES);
        assertQuantity(result, 18000.0, LengthUnit.INCHES, "Expected large-to-small conversion to preserve the sum");
    }

    private static void testAdditionExplicitTargetUnitSmallToLargeScale() {
        QuantityMeasurementApp.QuantityLength result = QuantityMeasurementApp.add(new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), new QuantityMeasurementApp.QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.YARDS);
        assertEquals(0.6666667, result.value, 1e-4, "Expected small-to-large conversion to preserve the sum");
        if (result.unit != LengthUnit.YARDS) {
            throw new AssertionError("Expected result unit to be YARDS");
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

    private static void assertEquals(double expected, double actual, double epsilon, String message) {
        if (Math.abs(expected - actual) > epsilon) {
            throw new AssertionError(message + " Expected=" + expected + " Actual=" + actual);
        }
    }

    private static void assertQuantity(QuantityMeasurementApp.QuantityLength actual, double expectedValue, LengthUnit expectedUnit, String message) {
        assertEquals(expectedValue, actual.value, 1e-6, message);
        if (actual.unit != expectedUnit) {
            throw new AssertionError(message + " Expected unit=" + expectedUnit + " Actual unit=" + actual.unit);
        }
    }
}
