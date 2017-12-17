package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import service.ControllerService;
import service.StageService;

import java.io.IOException;

public class MainMenuController {

    private StageService stage = new StageService();

    /**
     * Customer Login button action.
     * Opens a new stage {@link CustomerLoginController}
     */
    @FXML
    private void customerLogin(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, ControllerService.CUSTOMER_LOGIN);
    }

    /**
     * Staff Login button action.
     * Opens a new stage {@link StaffLoginController}
     */
    @FXML
    private void staffLogin(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, ControllerService.STAFF_LOGIN);
    }

    /**
     * View Products button action.
     * Opens a new stage {@link ViewProductsController}
     */
    @FXML
    private void viewProducts(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, ControllerService.VIEW_PRODUCTS);
    }
}
