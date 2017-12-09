package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Customer;
import service.ControllerService;
import service.StageService;

import java.io.IOException;

public class CustomerHomeController {

    @FXML
    private Label labelWelcome;
    private Customer customer;
    private StageService stage = new StageService();

    /**
     * Initialize the class.
     *
     * @param customer Passing a {@link Customer} object.
     */
    public void initialize(Customer customer) {
        this.customer = customer;
        labelWelcome.setText("Welcome " + customer.getFirstName() + " " + customer.getLastName());
    }

    /**
     * Edit Customer button action.
     * Opens a new stage {@link EditCustomerController}
     */
    @FXML
    private void editCustomer(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, customer, ControllerService.EDIT_CUSTOMER);
    }

    /**
     * Log out button action.
     * Opens a new stage {@link CustomerLoginController}
     */
    @FXML
    private void logout(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, ControllerService.CUSTOMER_LOGIN);
    }

}
