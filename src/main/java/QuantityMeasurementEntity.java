import java.io.Serializable;

public class QuantityMeasurementEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final double firstValue;
    private final double secondValue;
    private final String firstUnit;
    private final String secondUnit;
    private final String operation;
    private final String result;
    private final boolean success;
    private final String errorMessage;

    public QuantityMeasurementEntity(double firstValue, double secondValue, String firstUnit, String secondUnit, String operation, String result, boolean success, String errorMessage) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.firstUnit = firstUnit;
        this.secondUnit = secondUnit;
        this.operation = operation;
        this.result = result;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public double getFirstValue() {
        return firstValue;
    }

    public double getSecondValue() {
        return secondValue;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public String getOperation() {
        return operation;
    }

    public String getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
