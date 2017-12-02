package controllers;

import data.ShopContract.CustomerEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Customer;
import service.ControllerService;
import service.DbManager;
import service.StageService;
import validation.RegisterValidation;

import java.io.IOException;
import java.sql.*;

public class RegistrationController {

    @FXML
    private Label lblLoginStatus;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtAddressLine1;
    @FXML
    private TextField txtAddressLine2;
    @FXML
    private TextField txtTown;
    @FXML
    private TextField txtPostcode;

    private StageService stage = new StageService();

    /**
     * Register button action.
     * Get user registration data and check if user exists in database
     * if not exist then add a new row with record to a database.
     */
    @FXML
    private void register() {
        RegisterValidation result = RegisterValidation.validResult(txtUsername.getText(), txtPassword.getText(), txtFirstName.getText(), txtLastName.getText(), txtAddressLine1.getText(), txtAddressLine2.getText(), txtTown.getText(), txtPostcode.getText());

        if (result.isValid()) {
            if (userNotInDb(txtUsername.getText().trim())) {
                String username = txtUsername.getText().trim();
                String password = txtPassword.getText().trim();
                String firstName = txtFirstName.getText().trim();
                String lastName = txtLastName.getText().trim();
                String addressLine1 = txtAddressLine1.getText().trim();
                String addressLine2 = txtAddressLine2.getText().trim();
                String town = txtTown.getText().trim();
                String postcode = txtPostcode.getText().trim();

                Customer customer = new Customer(username, password, firstName, lastName, addressLine1, addressLine2, town, postcode);

                insertNewUser(customer);
            } else {
                lblLoginStatus.setText("Error: Username already exist");
            }

        } else {
            lblLoginStatus.setText(result.getErrorMessage());
        }

    }

    /**
     * Checks if user already exist in database.
     *
     * @param username to be check in database.
     * @return true username not exist / false exist.
     */
    private boolean userNotInDb(String username) {
        String sql = "SELECT " + CustomerEntry.COLUMN_USERNAME + " FROM "
                + CustomerEntry.TABLE_NAME + " WHERE "
                + CustomerEntry.COLUMN_USERNAME + " = '" + username + "'";

        try (Connection conn = DbManager.Connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return !rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void insertNewUser(Customer customer) {
        String sql = "INSERT INTO " + CustomerEntry.TABLE_NAME + "("
                + CustomerEntry.COLUMN_USERNAME + ", "
                + CustomerEntry.COLUMN_PASSWORD + ", "
                + CustomerEntry.COLUMN_FIRST_NAME + ", "
                + CustomerEntry.COLUMN_LAST_NAME + ", "
                + CustomerEntry.COLUMN_ADDRESS_LINE_1 + ", "
                + CustomerEntry.COLUMN_ADDRESS_LINE_2 + ", "
                + CustomerEntry.COLUMN_TOWN + ", "
                + CustomerEntry.COLUMN_POSTCODE + ") VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getUsername());
            pstmt.setString(2, customer.getPassword());
            pstmt.setString(3, customer.getFirstName());
            pstmt.setString(4, customer.getLastName());
            pstmt.setString(5, customer.getAddressLine1());
            pstmt.setString(6, customer.getAddressLine2());
            pstmt.setString(7, customer.getTown());
            pstmt.setString(8, customer.getPostcode());
            pstmt.executeUpdate();

            lblLoginStatus.setText("Registration Successful");
            clear();
        } catch (SQLException e) {
            lblLoginStatus.setText(e.getMessage());
        }
    }

    /**
     * Clear all input elements.
     * Indicates when Clear button is pressed.
     */
    @FXML
    private void clear() {
        txtUsername.clear();
        txtPassword.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtAddressLine1.clear();
        txtAddressLine2.clear();
        txtTown.clear();
        txtPostcode.clear();

    }

    /**
     * Back button action.
     * Opens a new stage {@link CustomerLoginController}
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, ControllerService.CUSTOMER_LOGIN);
    }
}