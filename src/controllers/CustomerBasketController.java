package controllers;

import data.ShopContract.OrderLinesEntry;
import data.ShopContract.OrdersEntry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Customer;
import models.OrderLine;
import service.ControllerService;
import service.DbManager;
import service.LabelStatusService;
import service.StageService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

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
    @FXML
    private Label labelStatus;

    private Customer customer;
    private ObservableList<OrderLine> basket = FXCollections.observableArrayList();
    private StageService stage = new StageService();
    private double totalPrice;
    private int orderId;

    private DecimalFormat decimal = new DecimalFormat("##.00");


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
        double calculateTotal = basket.stream()
                .mapToDouble(OrderLine::getLineTotal)
                .reduce(0, (x, y) -> x + y);

        totalPrice = Double.parseDouble(decimal.format(calculateTotal));

        labelTotal.setText("Total: " + POUND_SYMBOL + totalPrice);
    }

    // Mouse Event !!!
    @FXML
    private void buttonSetEnable() {
        if (tableViewBasket.getSelectionModel().getSelectedItem() != null) {
            buttonRemoveFromBasket.setDisable(false);
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

            if (insertNewOrder() && insertNewOrderLine()) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order completed");
                alert.setHeaderText(null);
                alert.setContentText("Thank you. You'r order is now precessing.");

                alert.showAndWait();

                stage.loadStage(actionEvent, customer, ControllerService.CUSTOMER_HOME);

            } else {
                LabelStatusService.getError(labelStatus,
                        "An error occurred. Pleas try again later.");
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("You need to be login");
            alert.setHeaderText(null);
            alert.setContentText("You are browsing as a guest.\nPlease sign in or create a new account.");

            alert.showAndWait();

            stage.loadStage(actionEvent, customer, basket, ControllerService.CUSTOMER_LOGIN);
        }
    }

    private boolean insertNewOrder() {
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

            return row == 1 && getOrderId(pstmt);

        } catch (SQLException e) {
            LabelStatusService.getError(labelStatus, e.getMessage());
        }
        return false;
    }

    private boolean getOrderId(PreparedStatement pstmt) throws SQLException {
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                orderId = (generatedKeys.getInt(1));
                return true;
            } else {
                throw new SQLException("Error: Order ID not obtained.");
            }
        }
    }

    private boolean insertNewOrderLine() {
        String sql = "INSERT INTO " + OrderLinesEntry.TABLE_NAME + "("
                + OrderLinesEntry.COLUMN_PRODUCT_ID + ", "
                + OrderLinesEntry.COLUMN_QUANTITY + ", "
                + OrderLinesEntry.COLUMN_LINE_TOTAL + ", "
                + OrderLinesEntry.COLUMN_ORDER_ID + ") VALUES(?,?,?,?)";

        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int count = 0;

            for (OrderLine p : basket) {
                pstmt.setInt(1, p.getProduct().getProductId());
                pstmt.setInt(2, p.getQuantity());
                pstmt.setDouble(3, p.getLineTotal());
                pstmt.setInt(4, orderId);
                pstmt.addBatch();

                ++count;
            }

            if (count % basket.size() == 0) {
                pstmt.executeBatch();
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            LabelStatusService.getError(labelStatus, e.getMessage());
        }
        return false;
    }


    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, customer, basket, ControllerService.VIEW_PRODUCTS);
    }

}