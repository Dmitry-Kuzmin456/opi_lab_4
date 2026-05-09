package managerBeans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("validator")
@SessionScoped
public class Validator implements Serializable {

    public static class ValidationResult {
        private final boolean isValid;
        private final String errorMessage;

        public ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    private ValidationResult validateX(double x) {
        if (x >= -5 && x <= 3) {
            return new ValidationResult(true, null);
        }
        return new ValidationResult(false, "X must be between -5 and 3");
    }

    private ValidationResult validateY(double y) {
        if (y >= -5 && y <= 5) {
            return new ValidationResult(true, null);
        }
        return new ValidationResult(false, "Y must be between -5 and 5");
    }

    private ValidationResult validateR(double r) {
        double[] validRs = new double[]{1, 2, 3, 4, 5};
        for (double value : validRs) {
            if (value == r) {
                return new ValidationResult(true, null);
            }
        }
        return new ValidationResult(false, "R must be one of [1, 2, 3, 4, 5]");
    }

    public ValidationResult validateForm(double x, double y, double r) {
        if (x == Double.NaN || y == Double.NaN || r == Double.NaN) {
            return new ValidationResult(false, "All fields (X, Y, R) must be provided");
        }

        ValidationResult xResult = validateX(x);
        if (!xResult.isValid()) {
            return xResult;
        }

        ValidationResult yResult = validateY(y);
        if (!yResult.isValid()) {
            return yResult;
        }

        ValidationResult rResult = validateR(r);
        if (!rResult.isValid()) {
            return rResult;
        }

        return new ValidationResult(true, null);
    }
}