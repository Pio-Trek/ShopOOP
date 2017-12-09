package controllers;

import data.ShopContract.CustomerEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Customer;
import service.ControllerService;
import service.DbManager;
import service.LabelStatusService;
import service.StageService;
import validation.CompareObjects;
import validation.RegisterValidation;

import java.io.IOException;
import java.sql.*;

public class EditCustomerController {

    @FXML
    private Label labelHeader;
    @FXML
    private Label labelStatus;
    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private TextField inputFirstName;
    @FXML
    private TextField inputLastName;
    @FXML
    private TextField inputAddressLine1;
    @FXML
    private TextField inputAddressLine2;
    @FXML
    private TextField inputTown;
    @FXML
    private TextField inputPostcode;

    private StageService stage = new StageService();
    private Customer customer;


    public void initialize() {
        labelHeader.setText("Register New Customer");
    }

    public void initialize(Customer customer) {
        this.customer = customer;
        labelHeader.setText("Edit Customer");
        inputUsername.setDisable(true);
        displayCustomer();
    }

    private void displayCustomer() {
        inputUsername.setText(customer.getUsername());
        inputPassword.setText(customer.getPassword());
        inputFirstName.setText(customer.getFirstName());
        inputLastName.setText(customer.getLastName());
        inputAddressLine1.setText(customer.getAddressLine1());
        inputAddressLine2.setText(customer.getAddressLine2());
        inputTown.setText(customer.getTown());
        inputPostcode.setText(customer.getPostcode());
    }

    /**
     * Register button action.
     * Get user registration data and check if user exists in database
     * if not exist then add a new row with record to a database.
     */
    @FXML
    private void submit() throws SQLException {
        RegisterValidation result = RegisterValidation.validResult(
                inputUsername.getText(),
                inputPassword.getText(),
                inputFirstName.getText(),
                inputLastName.getText(),
                inputAddressLine1.getText(),
                inputAddressLine2.getText(),
                inputTown.getText(),
                inputPostcode.getText());

        if (result.isValid()) {

            Customer newCustomer = createNewCustomer();

            if (customer == null) {
                if (userNotInDb(inputUsername.getText().trim())) {
                    insertNewUser(newCustomer);
                } else {
                    LabelStatusService.getConfirmation(labelStatus, "Error: Username already exist");
                }
            } else {
                updateEditedCustomer(newCustomer);
            }

        } else {
            LabelStatusService.getConfirmation(labelStatus, result.getErrorMessage());
        }

    }

    private Customer createNewCustomer() {

        String username = inputUsername.getText().trim();
        String password = inputPassword.getText().trim();
        String firstName = inputFirstName.getText().trim();
        String lastName = inputLastName.getText().trim();
        String addressLine1 = inputAddressLine1.getText().trim();
        String addressLine2 = inputAddressLine2.getText().trim();
        String town = inputTown.getText().trim();
        String postcode = inputPostcode.getText().trim();

        return new Customer(username, password, firstName, lastName, addressLine1, addressLine2, town, postcode);
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


            // Update
            int row = pstmt.executeUpdate();

            if (row == 1) {
                LabelStatusService.getConfirmation(labelStatus, "Registration Successful");
            } else {
                LabelStatusService.getConfirmation(labelStatus, "Error: Could not create new user");
            }

            clear();
        } catch (SQLException e) {
            LabelStatusService.getError(labelStatus, e.getMessage());
        }
    }

    /**
     * Indicates when {@see buttonSubmit} is press.
     * Get user input, compare it with passed {@see getCustomer} object and if is
     * different then update existing Customer in database.
     */
    private void updateEditedCustomer(Customer currentCustomer) throws SQLException {

        // Compare Customer objects.
        if (CompareObjects.compare(currentCustomer, customer)) {
            LabelStatusService.getConfirmation(labelStatus, "No changes has been made");
        } else {
            updateDb();
        }
    }

    /**
     * Update data of a User specified by the Username column.
     */
    private void updateDb() {
        String sql = "UPDATE " + CustomerEntry.TABLE_NAME + " SET "
                + CustomerEntry.COLUMN_PASSWORD + " = ?, "
                + CustomerEntry.COLUMN_FIRST_NAME + " = ?, "
                + CustomerEntry.COLUMN_LAST_NAME + " = ?, "
                + CustomerEntry.COLUMN_ADDRESS_LINE_1 + " = ?, "
                + CustomerEntry.COLUMN_ADDRESS_LINE_2 + " = ?,"
                + CustomerEntry.COLUMN_TOWN + " = ?,"
                + CustomerEntry.COLUMN_POSTCODE + " = ?"
                + " WHERE " + CustomerEntry.COLUMN_USERNAME + " = ?";


        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the corresponding param
            pstmt.setString(1, inputPassword.getText().trim());
            pstmt.setString(2, inputFirstName.getText().trim());
            pstmt.setString(3, inputLastName.getText().trim());
            pstmt.setString(4, inputAddressLine1.getText().trim());
            pstmt.setString(5, inputAddressLine2.getText().trim());
            pstmt.setString(6, inputTown.getText().trim());
            pstmt.setString(7, inputPostcode.getText().trim());
            pstmt.setString(8, inputUsername.getText().trim());

            // Update
            int row = pstmt.executeUpdate();

            if (row == 1) {
                LabelStatusService.getConfirmation(labelStatus, "User updated successful");
            } else {
                LabelStatusService.getConfirmation(labelStatus, "Error: Could not update user details");
            }

        } catch (SQLException e) {
            LabelStatusService.getError(labelStatus, e.getMessage());
        }
    }

    /**
     * Clear all input elements.
     * Indicates when Clear button is pressed.
     */
    @FXML
    private void clear() {
        inputUsername.clear();
        inputPassword.clear();
        inputFirstName.clear();
        inputLastName.clear();
        inputAddressLine1.clear();
        inputAddressLine2.clear();
        inputTown.clear();
        inputPostcode.clear();

    }

    /**
     * Back button action.
     * Opens a new stage {@link CustomerLoginController}
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        if (customer == null) {
            stage.loadStage(actionEvent, ControllerService.CUSTOMER_LOGIN);
        } else {
            stage.loadStage(actionEvent, customer, ControllerService.CUSTOMER_HOME);
        }
    }
}