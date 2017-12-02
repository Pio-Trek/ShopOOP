package controllers;

import data.ShopContract.CustomerEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import models.Customer;
import service.ControllerService;
import service.LoginService;
import service.StageService;
import validation.LoginValidation;

import java.io.IOException;

public class CustomerLoginController {

    @FXML
    private Label lblLoginStatus;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    private StageService stage = new StageService();

    /**
     * Empty constructor just to add SQL exception.
     */
    public CustomerLoginController() {
    }

    /**
     * Login button action.
     * Get user login data and check if user exists in database.
     */
    @FXML
    private void login(ActionEvent actionEvent) throws IOException {

        // Validate user input
        LoginValidation result = LoginValidation.validResult(txtUsername.getText(), txtPassword.getText());
        if (result.isValid()) {
            // Execute the SQL query and try to find user
            LoginService login = new LoginService();
            boolean isAuthorized = login.execute(CustomerEntry.TABLE_NAME, txtUsername.getText(), txtPassword.getText());

            // If user found in database then create a new Customer object
            if (isAuthorized) {
                Customer customer = login.customer();
                if (customer != null)
                    stage.loadStage(actionEvent, customer, ControllerService.CUSTOMER_HOME);
            } else {
                lblLoginStatus.setText("Login Failed");
            }

        } else {
            lblLoginStatus.setText(result.getErrorMessage());
        }

    }

    /**
     * Perform an action when user press the Enter key.
     *
     * @param keyEvent Indicates user keyboard input.
     */
    @FXML
    private void loginKey(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            ActionEvent actionEvent = new ActionEvent(keyEvent.getSource(), keyEvent.getTarget());
            login(actionEvent);
        }
    }

    /**
     * Register button action.
     * Opens a new stage {@link RegistrationController}
     */
    @FXML
    private void register(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, ControllerService.REGISTRATION);
    }

    /**
     * Back button action.
     * Opens a new stage {@link MainMenuController}
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, ControllerService.MAIN_MENU);
    }

}

