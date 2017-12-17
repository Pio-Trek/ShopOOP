package controllers;

import data.ShopContract.StaffEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import models.Staff;
import service.ControllerService;
import service.LabelStatusService;
import service.LoginService;
import service.StageService;
import validation.LoginValidation;

import java.io.IOException;


public class StaffLoginController {

    @FXML
    private Label labelStatus;
    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;
    private StageService stage = new StageService();

    /**
     * Login button action.
     * Get user login data and check if a user exists in the database.
     */
    @FXML
    private void login(ActionEvent actionEvent) throws IOException {

        // Validate user input
        LoginValidation result = LoginValidation.validResult(inputUsername.getText(), inputPassword.getText());
        if (result.isValid()) {
            // Execute the SQL query and try to find user
            LoginService login = new LoginService();
            boolean isAuthorized = login.execute(StaffEntry.TABLE_NAME,
                    inputUsername.getText(), inputPassword.getText());

            // If user found in database then create a new Customer object
            if (isAuthorized) {
                Staff staff = login.getStaff();
                if (staff != null)
                    stage.loadStage(actionEvent, staff, ControllerService.STAFF_HOME);
            } else {
                LabelStatusService.getError(labelStatus, "Login Failed");
            }

        } else {
            LabelStatusService.getError(labelStatus, result.getErrorMessage());
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
     * Back button action.
     * Opens a new stage {@link MainMenuController}
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, ControllerService.MAIN_MENU);
    }

}
