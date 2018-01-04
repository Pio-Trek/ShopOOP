package controllers;

import data.ShopContract.CustomerEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Customer;
import models.OrderLine;
import service.*;
import validation.RegisterValidation;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class AddEditCustomerController {

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
    private Customer newCustomer;
    private ObservableList<OrderLine> basket = FXCollections.observableArrayList();


    /**
     * Initialize the class when registering a new {@link Customer}
     */
    public void initialize() {
        labelHeader.setText("Register New Customer");
    }

    /**
     * Initialize the class when registering a new {@link Customer} which
     * already had added products to a basket
     *
     * @param customer Passing a {@link Customer} object.
     * @param basket   unregistered user ObservableList basket.
     */
    public void initialize(Customer customer, ObservableList<OrderLine> basket) {
        labelHeader.setText("Register New Customer");
        this.customer = customer;
        this.basket = basket;
    }

    /**
     * Initialize the class when editing an existing {@link Customer}
     *
     * @param customer Passing a {@link Customer} object.
     */
    public void initialize(Customer customer) {
        this.customer = customer;
        labelHeader.setText("Edit Customer");
        inputUsername.setDisable(true);
        displayCustomer();
    }

    /**
     * Populate existing {@link Customer} data into TextField
     */
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
     * if not exist then add a new row with the record to a database.
     */
    @FXML
    private void submit(ActionEvent actionEvent) throws IOException {
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

            newCustomer = createNewCustomer();

            if (customer == null || !customer.isRegistered()) {
                if (userNotInDb(inputUsername.getText().trim())) {
                    insertNewUser(newCustomer, actionEvent);
                } else {
                    LabelStatusService.getConfirmation(labelStatus, "Error: Username already exist");
                }
            } else {
                updateEditedCustomer(newCustomer);
            }

        } else {
            LabelStatusService.getError(labelStatus, result.getErrorMessage());
        }

    }

    /**
     * Create a new {@link Customer} object from given user input.
     *
     * @return {@link Customer} object.
     */
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
             Statement stmt = Objects.requireNonNull(conn).createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return !rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Create a new {@link Customer} record in database
     *
     * @param customer Passing a {@link Customer} object.
     */
    private void insertNewUser(Customer customer, ActionEvent actionEvent) throws IOException {
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
             PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {

            pstmt.setString(1, customer.getUsername());
            pstmt.setString(2, customer.getPassword());
            pstmt.setString(3, customer.getFirstName());
            pstmt.setString(4, customer.getLastName());
            pstmt.setString(5, customer.getAddressLine1());
            pstmt.setString(6, customer.getAddressLine2());
            pstmt.setString(7, customer.getTown());
            pstmt.setString(8, customer.getPostcode());

            int row = pstmt.executeUpdate();

            if (row == 1) {
                LabelStatusService.getConfirmation(labelStatus, "Registration Successful");
                if (basket != null) {
                    backToBasket(actionEvent);
                }
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
    private void updateEditedCustomer(Customer currentCustomer) {

        // Compare Customer objects.
        if (currentCustomer.equals(customer)) {
            LabelStatusService.getError(labelStatus, "No changes has been made");
        } else {
            updateDb();
        }
    }

    /**
     * Update data of a user specified by the Username column.
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
             PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {

            // Set the corresponding param
            pstmt.setString(1, inputPassword.getText().trim());
            pstmt.setString(2, inputFirstName.getText().trim());
            pstmt.setString(3, inputLastName.getText().trim());
            pstmt.setString(4, inputAddressLine1.getText().trim());
            pstmt.setString(5, inputAddressLine2.getText().trim());
            pstmt.setString(6, inputTown.getText().trim());
            pstmt.setString(7, inputPostcode.getText().trim());
            pstmt.setString(8, inputUsername.getText().trim());

            int row = pstmt.executeUpdate();

            if (row == 1) {
                LabelStatusService.getConfirmation(labelStatus, "User updated successful");
                customer = createNewCustomer();
            } else {
                LabelStatusService.getConfirmation(labelStatus, "Error: Could not update user details");
            }

        } catch (SQLException e) {
            LabelStatusService.getError(labelStatus, e.getMessage());
        }
    }

    private void backToBasket(ActionEvent actionEvent) throws IOException {
        AlertService.showDialog(Alert.AlertType.INFORMATION,
                "You are now sign in",
                null,
                "Thank you for registration.\nYou will be now moved to your basket.");

        stage.loadStage(actionEvent, newCustomer, basket, ControllerService.CUSTOMER_BASKET);
    }

    /**
     * Clear all input elements.
     * Indicates when Clear button is pressed.
     */
    @FXML
    private void clear() {
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
     * Opens a new stage {@link CustomerLoginController} when user is not sign in
     * Opens a new stage {@link CustomerHomeController} when user is sign in
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