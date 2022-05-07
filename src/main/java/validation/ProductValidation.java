package validation;

import java.util.regex.Pattern;

/**
 * Validates creating of a new product helper class
 */
public class ProductValidation {

    private boolean isValid;
    private String errorMessage;

    private ProductValidation(boolean isValid) {
        this.isValid = isValid;
    }

    private ProductValidation(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public static ProductValidation validResult(String productName, String price, String stockLevel, String measurement, String size) {
        if (productName.isEmpty() || price.isEmpty() || stockLevel.isEmpty() || measurement.isEmpty() && size.isEmpty()) {
            return new ProductValidation(false, "All fields are mandatory");

            // Validate price
        } else if (!Pattern.matches("(?<![\\d.])(\\d{1,5}|\\d{0,5}\\.\\d{1,2})?(?![\\d.])", price)) {
            return new ProductValidation(false, "Incorrectly price value");

        } else {
            return new ProductValidation(true);
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
