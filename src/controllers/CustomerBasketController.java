package controllers;

import data.ShopContract.OrderLinesEntry;
import data.ShopContract.OrdersEntry;
import data.ShopContract.ProductsEntry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Customer;
import models.Order;
import models.OrderLine;
import service.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Objects;

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


    /**
     * Initialize the class when {@link Customer} want to see a shopping basket.
     *
     * @param customer Passing a {@link Customer} object.
     * @param basket   Passing an ObservableList object.
     */
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

    /**
     * Displays a total price of all products in the {@see basket}.
     */
    private void getTotalPrice() {
        double calculateTotal = basket.stream()
                .mapToDouble(OrderLine::getLineTotal)
                .reduce(0, (x, y) -> x + y);

        totalPrice = Double.parseDouble(decimal.format(calculateTotal));

        labelTotal.setText("Total: " + POUND_SYMBOL + totalPrice);
    }

    /**
     * MouseEvent enables {@see buttonRemoveFromBasket} when product is selected.
     */
    @FXML
    private void buttonSetEnable() {
        if (tableViewBasket.getSelectionModel().getSelectedItem() != null) {
            buttonRemoveFromBasket.setDisable(false);
        }
    }

    /**
     * Triggered when {@see buttonRemoveFromBasket} is pressed.
     * Gets current {@link OrderLine} product and removes from {@see basket}.
     */
    @FXML
    private void removeProductFromBasket(ActionEvent actionEvent) throws IOException {
        OrderLine model = tableViewBasket.getSelectionModel().selectedItemProperty().get();
        basket.remove(model);
        getTotalPrice();

        if (basket.size() == 0) {
            back(actionEvent);
        }
    }

    /**
     * Triggered when buy button is pressed.
     * Adding a new order and order line to a database when user is already log in
     * or move the user to {@link CustomerLoginController} to sign in or sign up.
     */
    @FXML
    private void buyProducts(ActionEvent actionEvent) throws IOException {
        if (customer.isRegistered()) {

            if (updateStockLevel() && insertNewOrder() && insertNewOrderLine()) {

                AlertService.showDialog(Alert.AlertType.INFORMATION,
                        "Order completed",
                        null,
                        "Thank you. You'r order is now precessing.");


                stage.loadStage(actionEvent, customer, ControllerService.CUSTOMER_HOME);

            } else {
                LabelStatusService.getError(labelStatus,
                        "An error occurred. Pleas try again later.");
            }
        } else {
            AlertService.showDialog(Alert.AlertType.WARNING,
                    "You need to be login",
                    null,
                    "You are browsing as a guest.\nPlease sign in or create a new account.");

            stage.loadStage(actionEvent, customer, basket, ControllerService.CUSTOMER_LOGIN);
        }
    }

    /**
     * Updates the stock level in the database of all products in Customer basket.
     */
    private boolean updateStockLevel() {

        String sql = "UPDATE " + ProductsEntry.TABLE_NAME + " SET "
                + ProductsEntry.COLUMN_STOCK_LEVEL + " = ("
                + ProductsEntry.COLUMN_STOCK_LEVEL + " - ?)"
                + " WHERE " + ProductsEntry.COLUMN_PRODUCT_ID + " = ?";

        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {

            int count = 0;

            for (OrderLine p : basket) {
                pstmt.setInt(1, p.getQuantity());
                pstmt.setInt(2, p.getProduct().getProductId());
                pstmt.addBatch();
            }

            // Checks batch statements to be same as products in basket
            if (count % basket.size() == 0) {
                pstmt.executeBatch();
                return true;
            }

        } catch (SQLException e) {
            LabelStatusService.getError(labelStatus, e.getMessage());
        }

        return false;
    }

    /**
     * Create a new {@link Order} record in database
     *
     * @return TRUE/FALSE depends on successful insert statement
     */
    private boolean insertNewOrder() {
        String sql = "INSERT INTO " + OrdersEntry.TABLE_NAME + "("
                + OrdersEntry.COLUMN_ORDER_DATE + ", "
                + OrdersEntry.COLUMN_USERNAME + ", "
                + OrdersEntry.COLUMN_ORDER_TOTAL + ", "
                + OrdersEntry.COLUMN_STATUS + ") VALUES(strftime('%s','now'),?,?,?)";

        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {

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

    /**
     * Gets {@link Order} ID number for just created order.
     * Used to save in {@link OrderLine} database record.
     *
     * @param pstmt Passing PreparedStatement from inserting a new order.
     * @return TRUE/FALSE depends on successful insert statement
     */
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

    /**
     * Creates a new {@link OrderLine} record in database.
     *
     * @return TRUE/FALSE depends on successful insert statement
     */
    private boolean insertNewOrderLine() {
        String sql = "INSERT INTO " + OrderLinesEntry.TABLE_NAME + "("
                + OrderLinesEntry.COLUMN_PRODUCT_ID + ", "
                + OrderLinesEntry.COLUMN_QUANTITY + ", "
                + OrderLinesEntry.COLUMN_LINE_TOTAL + ", "
                + OrderLinesEntry.COLUMN_ORDER_ID + ") VALUES(?,?,?,?)";

        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {

            int count = 0;

            // Batch insert of all products in basket
            for (OrderLine p : basket) {
                pstmt.setInt(1, p.getProduct().getProductId());
                pstmt.setInt(2, p.getQuantity());
                pstmt.setDouble(3, p.getLineTotal());
                pstmt.setInt(4, orderId);
                pstmt.addBatch();

                ++count;
            }

            // Checks batch statements to be same as products in basket
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

    /**
     * Back button action.
     * Opens a new stage {@link ViewProductsController}
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, customer, basket, ControllerService.VIEW_PRODUCTS);
    }

}