package controllers;

import data.ShopContract.OrdersEntry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import models.Customer;
import models.OrderLine;
import service.ControllerService;
import service.DbManager;
import service.StageService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerBasketController {

    private final String POUND_SYMBOL = "\u00A3 ";

    @FXML
    private TableView<OrderLine> tableViewBasket;
    @FXML
    private TableColumn<OrderLine, Integer> tableColumnId;
    @FXML
    private TableColumn<OrderLine, String> tableColumnName;
    @FXML
    private TableColumn<OrderLine, String> tableColumnPrice;
    @FXML
    private TableColumn<OrderLine, Integer> tableColumnQuantity;
    @FXML
    private TableColumn<OrderLine, String> tableColumnTotal;
    @FXML
    private Button buttonRemoveFromBasket;
    @FXML
    private Label labelTotal;

    private Customer customer;
    private ObservableList<OrderLine> basket = FXCollections.observableArrayList();
    private StageService stage = new StageService();
    private double totalPrice;


    public void initialize(Customer customer, ObservableList<OrderLine> basket) {
        this.customer = customer;
        this.basket = basket;

        buttonRemoveFromBasket.setDisable(true);

        tableColumnId.setCellValueFactory(
                p -> new SimpleObjectProperty<>(p.getValue().getProduct().getProductId()));
        tableColumnName.setCellValueFactory(
                p -> new SimpleObjectProperty<>(p.getValue().getProduct().getProductName()));
        tableColumnPrice.setCellValueFactory(
                p -> new SimpleObjectProperty<>(POUND_SYMBOL + p.getValue().getProduct().getPrice()));
        tableColumnQuantity.setCellValueFactory(
                p -> new SimpleObjectProperty<>(p.getValue().getQuantity()));
        tableColumnTotal.setCellValueFactory(
                p -> new SimpleObjectProperty<>(POUND_SYMBOL + p.getValue().getLineTotal()));
        tableViewBasket.setItems(basket);

        getTotalPrice();
    }

    private void getTotalPrice() {
        totalPrice = basket.stream().mapToDouble(OrderLine::getLineTotal).reduce(0, (x, y) -> x + y);

        labelTotal.setText("Total: " + POUND_SYMBOL + String.format("%.2f", totalPrice));
    }

    @FXML
    private void buttonSetEnable(MouseEvent mouseEvent) {
        if (tableViewBasket.getSelectionModel().getSelectedItem() != null) {
            buttonRemoveFromBasket.setDisable(false);
        } else {
            buttonRemoveFromBasket.setDisable(true);
        }
    }

    @FXML
    private void removeProductFromBasket(ActionEvent actionEvent) throws IOException {
        OrderLine model = tableViewBasket.getSelectionModel().selectedItemProperty().get();
        basket.remove(model);
        getTotalPrice();

        if (basket.size() == 0) {
            back(actionEvent);
        }
    }

    @FXML
    private void buyProducts(ActionEvent actionEvent) throws IOException {
        if (customer.isRegistered()) {

            insertNewOrder();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("You need to be login");
            alert.setHeaderText(null);
            alert.setContentText("You are browsing as a guest.\nPlease sign in or create a new account.");

            alert.showAndWait();

            stage.loadStage(actionEvent, customer, basket, ControllerService.CUSTOMER_LOGIN);
        }
    }

    private void insertNewOrder() {
        String sql = "INSERT INTO " + OrdersEntry.TABLE_NAME + "("
                + OrdersEntry.COLUMN_ORDER_DATE + ", "
                + OrdersEntry.COLUMN_USERNAME + ", "
                + OrdersEntry.COLUMN_ORDER_TOTAL + ", "
                + OrdersEntry.COLUMN_STATUS + ") VALUES(strftime('%s','now'),?,?,?)";

        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getUsername());
            pstmt.setDouble(2, (totalPrice * 100.0) / 100.0);
            pstmt.setString(3, "Received");

            // Update
            int row = pstmt.executeUpdate();

            System.out.println("DONE");

/*            if (row == 1) {
                LabelStatusService.getConfirmation(labelStatus, "Registration Successful");
                if (basket != null) {
                    backToBasket(actionEvent);
                }
            } else {
                LabelStatusService.getConfirmation(labelStatus, "Error: Could not create new user");
            }

            clear();*/
        } catch (SQLException e) {
            System.out.println("ERROR");
            //LabelStatusService.getError(labelStatus, e.getMessage());
        }
    }

    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, customer, basket, ControllerService.VIEW_PRODUCTS);
    }

}
