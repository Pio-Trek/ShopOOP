package validation;

import java.util.regex.Pattern;

/**
 * Validates registration of a new user helper class
 */
public class RegisterValidation {

    private boolean isValid;
    private String errorMessage;

    private RegisterValidation(boolean isValid) {
        this.isValid = isValid;
    }

    private RegisterValidation(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public static RegisterValidation validResult(String username, String password, String firstName, String lastName, String addressLine1, String addressLine2, String town, String postcode) {
        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || addressLine1.isEmpty() || addressLine2.isEmpty() || town.isEmpty() || postcode.isEmpty()) {
            return new RegisterValidation(false, "All fields are mandatory");

            // Validate UK postcode
        } else if (!Pattern.matches("([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z]))))\\s?[0-9][A-Za-z]{2})", postcode)) {
            return new RegisterValidation(false, "Incorrectly postcode");

        } else {
            return new RegisterValidation(true);
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
