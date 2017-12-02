package validation;


/**
 * Validates user login input helper class
 */
public class LoginValidation {

    private boolean isValid;
    private String errorMessage;

    private LoginValidation(boolean isValid) {
        this.isValid = isValid;
    }

    private LoginValidation(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public static LoginValidation validResult(String userName, String password) {

        if (password.isEmpty() && !userName.isEmpty()) {
            return new LoginValidation(false, "Enter your Password");
        } else if (userName.isEmpty() && !password.isEmpty()) {
            return new LoginValidation(false, "Enter your Username");
        } else if (password.isEmpty()) {    //When Username and Password are empty
            return new LoginValidation(false, "Enter your Username and Password");
        } else {
            return new LoginValidation(true);
        }

    }

    public boolean isValid() {
        return isValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
