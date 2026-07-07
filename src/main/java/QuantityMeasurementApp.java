public class QuantityMeasurementApp {
    private static QuantityMeasurementController controller;

    public static void main(String[] args) {
        initialize();
        QuantityDTO first = new QuantityDTO(1.0, "FEET", "length", "compare", null, true, null);
        QuantityDTO second = new QuantityDTO(12.0, "INCHES", "length", "compare", null, true, null);
        QuantityDTO result = controller.performComparison(first, second);
        System.out.println("Comparison result: " + result.getResult());
    }

    public static void initialize() {
        if (controller == null) {
            controller = new QuantityMeasurementController(new QuantityMeasurementServiceImpl(QuantityMeasurementCacheRepository.getInstance()));
        }
    }

    public static QuantityMeasurementController getController() {
        initialize();
        return controller;
    }

    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        validateValue(value);
        validateUnit(sourceUnit, "sourceUnit");
        validateUnit(targetUnit, "targetUnit");

        if (sourceUnit == targetUnit) {
            return value;
        }

        double valueInFeet = sourceUnit.convertToBaseUnit(value);
        return targetUnit.convertFromBaseUnit(valueInFeet);
    }

    public static <U extends IMeasurable> double convert(double value, U sourceUnit, U targetUnit) {
        validateValue(value);
        validateUnit(sourceUnit, "sourceUnit");
        validateUnit(targetUnit, "targetUnit");

        if (sourceUnit == targetUnit) {
            return value;
        }

        double valueInBaseUnit = sourceUnit.convertToBaseUnit(value);
        return targetUnit.convertFromBaseUnit(valueInBaseUnit);
    }

    public static <U extends IMeasurable> Quantity<U> convert(Quantity<U> quantity, U targetUnit) {
        validateQuantity(quantity, "quantity");
        validateUnit(targetUnit, "targetUnit");
        double convertedValue = convert(quantity.value, quantity.unit, targetUnit);
        return new Quantity<>(convertedValue, targetUnit);
    }

    public static QuantityLength add(QuantityLength first, QuantityLength second, LengthUnit targetUnit) {
        validateQuantity(first, "first");
        validateQuantity(second, "second");
        Quantity<LengthUnit> result = addGenericQuantity(new Quantity<>(first.value, first.unit), new Quantity<>(second.value, second.unit), targetUnit);
        return new QuantityLength(result.value, result.unit);
    }

    public static QuantityLength add(QuantityLength first, QuantityLength second) {
        validateQuantity(first, "first");
        validateQuantity(second, "second");
        Quantity<LengthUnit> result = addGenericQuantity(new Quantity<>(first.value, first.unit), new Quantity<>(second.value, second.unit));
        return new QuantityLength(result.value, result.unit);
    }

    public static QuantityLength add(double firstValue, LengthUnit firstUnit, double secondValue, LengthUnit secondUnit, LengthUnit targetUnit) {
        return add(new QuantityLength(firstValue, firstUnit), new QuantityLength(secondValue, secondUnit), targetUnit);
    }

    public static QuantityWeight convert(QuantityWeight quantity, WeightUnit targetUnit) {
        validateQuantity(quantity, "quantity");
        Quantity<WeightUnit> result = convert(new Quantity<>(quantity.value, quantity.unit), targetUnit);
        return new QuantityWeight(result.value, result.unit);
    }

    public static QuantityWeight add(QuantityWeight first, QuantityWeight second) {
        validateQuantity(first, "first");
        validateQuantity(second, "second");
        Quantity<WeightUnit> result = addGenericQuantity(new Quantity<>(first.value, first.unit), new Quantity<>(second.value, second.unit));
        return new QuantityWeight(result.value, result.unit);
    }

    public static QuantityWeight add(QuantityWeight first, QuantityWeight second, WeightUnit targetUnit) {
        validateQuantity(first, "first");
        validateQuantity(second, "second");
        Quantity<WeightUnit> result = addGenericQuantity(new Quantity<>(first.value, first.unit), new Quantity<>(second.value, second.unit), targetUnit);
        return new QuantityWeight(result.value, result.unit);
    }

    public static Quantity<WeightUnit> add(double firstValue, WeightUnit firstUnit, double secondValue, WeightUnit secondUnit) {
        return addGenericQuantity(new Quantity<>(firstValue, firstUnit), new Quantity<>(secondValue, secondUnit));
    }

    public static Quantity<WeightUnit> add(double firstValue, WeightUnit firstUnit, double secondValue, WeightUnit secondUnit, WeightUnit targetUnit) {
        return addGenericQuantity(new Quantity<>(firstValue, firstUnit), new Quantity<>(secondValue, secondUnit), targetUnit);
    }

    private static <U extends IMeasurable> Quantity<U> addGenericQuantity(Quantity<U> first, Quantity<U> second, U targetUnit) {
        validateQuantity(first, "first");
        validateQuantity(second, "second");
        validateUnit(targetUnit, "targetUnit");
        double totalInBaseUnit = first.toBaseUnitValue() + second.toBaseUnitValue();
        double convertedValue = targetUnit.convertFromBaseUnit(totalInBaseUnit);
        return new Quantity<>(convertedValue, targetUnit);
    }

    public static <U extends IMeasurable> Quantity<U> addGenericQuantity(Quantity<U> first, Quantity<U> second) {
        return addGenericQuantity(first, second, first.unit);
    }

    private enum ArithmeticOperation {
        ADD((left, right) -> left + right),
        SUBTRACT((left, right) -> left - right),
        DIVIDE((left, right) -> {
            if (right == 0.0) {
                throw new ArithmeticException("Cannot divide by zero");
            }
            return left / right;
        });

        private final java.util.function.DoubleBinaryOperator operation;

        ArithmeticOperation(java.util.function.DoubleBinaryOperator operation) {
            this.operation = operation;
        }

        double compute(double left, double right) {
            return operation.applyAsDouble(left, right);
        }
    }

    public static class Quantity<U extends IMeasurable> {
        final double value;
        final U unit;

        public Quantity(double value, U unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        public Quantity<U> convertTo(U targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }
            double convertedValue = convert(this.value, this.unit, targetUnit);
            return new Quantity<>(convertedValue, targetUnit);
        }

        public double toBaseUnitValue() {
            return this.unit.convertToBaseUnit(this.value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Quantity<?> other = (Quantity<?>) obj;
            if (this.unit.getClass() != other.unit.getClass()) {
                return false;
            }
            double thisInBaseUnit = this.unit.convertToBaseUnit(this.value);
            double otherInBaseUnit = other.unit.convertToBaseUnit(other.value);
            return Double.compare(thisInBaseUnit, otherInBaseUnit) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(this.unit.convertToBaseUnit(this.value));
        }

        @Override
        public String toString() {
            return value + " " + unit.getUnitName();
        }

        public Quantity<U> add(Quantity<U> other) {
            return add(other, this.unit);
        }

        public Quantity<U> add(Quantity<U> other, U targetUnit) {
            return new Quantity<>(roundToTwoDecimals(performArithmetic(other, targetUnit, ArithmeticOperation.ADD)), targetUnit);
        }

        public Quantity<U> subtract(Quantity<U> other) {
            return subtract(other, this.unit);
        }

        public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
            return new Quantity<>(roundToTwoDecimals(performArithmetic(other, targetUnit, ArithmeticOperation.SUBTRACT)), targetUnit);
        }

        public double divide(Quantity<U> other) {
            validateArithmeticOperands(other, null, false, ArithmeticOperation.DIVIDE);
            double baseResult = performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
            return baseResult;
        }

        private double performArithmetic(Quantity<U> other, U targetUnit, ArithmeticOperation operation) {
            validateArithmeticOperands(other, targetUnit, true, operation);
            double baseResult = performBaseArithmetic(other, operation);
            return targetUnit.convertFromBaseUnit(baseResult);
        }

        private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {
            double thisInBaseUnit = this.toBaseUnitValue();
            double otherInBaseUnit = other.toBaseUnitValue();
            return operation.compute(thisInBaseUnit, otherInBaseUnit);
        }

        private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired, ArithmeticOperation operation) {
            validateQuantity(other, "other");
            if (this.unit.getClass() != other.unit.getClass()) {
                throw new IllegalArgumentException("Cannot perform arithmetic on quantities from different measurement categories");
            }
            if (targetUnitRequired && targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }
            if (targetUnitRequired && this.unit.getClass() != targetUnit.getClass()) {
                throw new IllegalArgumentException("Target unit must be from the same measurement category");
            }
            this.unit.validateOperationSupport(operation.name());
        }

        private double roundToTwoDecimals(double value) {
            return Math.round(value * 100.0) / 100.0;
        }
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
            double thisInFeet = this.unit.convertToBaseUnit(this.value);
            double otherInFeet = other.unit.convertToBaseUnit(other.value);
            return Double.compare(thisInFeet, otherInFeet) == 0;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static class QuantityWeight {
        final double value;
        final WeightUnit unit;

        public QuantityWeight(double value, WeightUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        public QuantityWeight convertTo(WeightUnit targetUnit) {
            return convert(this, targetUnit);
        }

        public double toKilograms() {
            return convert(this.value, this.unit, WeightUnit.KILOGRAM);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            QuantityWeight other = (QuantityWeight) obj;
            double thisInKilograms = this.unit.convertToBaseUnit(this.value);
            double otherInKilograms = other.unit.convertToBaseUnit(other.value);
            return Double.compare(thisInKilograms, otherInKilograms) == 0;
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

    private static void validateUnit(WeightUnit unit, String parameterName) {
        if (unit == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null");
        }
    }

    private static <U extends IMeasurable> void validateUnit(U unit, String parameterName) {
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

    private static void validateQuantity(QuantityWeight quantity, String parameterName) {
        if (quantity == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null");
        }
        validateValue(quantity.value);
        validateUnit(quantity.unit, parameterName + ".unit");
    }

    private static <U extends IMeasurable> void validateQuantity(Quantity<U> quantity, String parameterName) {
        if (quantity == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null");
        }
        validateValue(quantity.value);
        validateUnit(quantity.unit, parameterName + ".unit");
    }
}
