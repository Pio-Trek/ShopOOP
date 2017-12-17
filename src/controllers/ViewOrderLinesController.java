package controllers;

import data.ShopContract.OrderLinesEntry;
import data.ShopContract.ProductsEntry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.*;
import service.ControllerService;
import service.DbManager;
import service.StageService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class ViewOrderLinesController {

    private final String POUND_SYMBOL = "\u00A3 ";

    @FXML
    private Label labelHeaderOrderNum;
    @FXML
    private TableView<OrderLine> tableViewOrderLines;
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

    private Customer customer;
    private Staff staff;
    private int orderId;
    private ObservableList<OrderLine> orderLine = FXCollections.observableArrayList();
    private StageService stage = new StageService();

    /**
     * Initialize the class when {@link Customer} want to see {@link OrderLine}
     *
     * @param customer Passing a {@link Customer} object.
     * @param orderId  Passing a {@link Order} object ID number.
     */
    public void initialize(Customer customer, int orderId) {
        this.customer = customer;
        this.orderId = orderId;
        setTableViewOrderLines();
    }

    /**
     * Initialize the class when {@link Staff} want to see {@link OrderLine}
     *
     * @param staff   Passing a {@link Staff} object.
     * @param orderId Passing a {@link Order} object ID number.
     */
    public void initialize(Staff staff, int orderId) {
        this.staff = staff;
        this.orderId = orderId;
        setTableViewOrderLines();
    }

    /**
     * Populate TableView with {@link OrderLine} objects.
     */
    private void setTableViewOrderLines() {
        labelHeaderOrderNum.setText("View Order Number: " + orderId);

        getOrderLinesFromDb();

        tableColumnId.setCellValueFactory(
                ol -> new SimpleObjectProperty<>(ol.getValue().getProduct().getProductId()));
        tableColumnName.setCellValueFactory(
                ol -> new SimpleObjectProperty<>(ol.getValue().getProduct().getProductName()));
        tableColumnPrice.setCellValueFactory(
                ol -> new SimpleObjectProperty<>(POUND_SYMBOL + ol.getValue().getProduct().getPrice()));
        tableColumnQuantity.setCellValueFactory(
                ol -> new SimpleObjectProperty<>(ol.getValue().getQuantity()));
        tableColumnTotal.setCellValueFactory(
                ol -> new SimpleObjectProperty<>(POUND_SYMBOL + ol.getValue().getLineTotal()));

        tableViewOrderLines.setSelectionModel(null);

        tableViewOrderLines.setItems(orderLine);

    }

    /**
     * Gets {@link OrderLine} and {@link Product} from database.
     * Create (join) an inner table and create a new OrderLine
     * ObservableList to populate in Table View.
     */
    private void getOrderLinesFromDb() {
        String sql = "SELECT o." + OrderLinesEntry.COLUMN_PRODUCT_ID
                + ", p." + ProductsEntry.COLUMN_PRODUCT_NAME
                + ", p." + ProductsEntry.COLUMN_PRICE
                + ", o." + OrderLinesEntry.COLUMN_QUANTITY
                + ", o." + OrderLinesEntry.COLUMN_LINE_TOTAL
                + " FROM " + OrderLinesEntry.TABLE_NAME + " o "
                + "INNER JOIN " + ProductsEntry.TABLE_NAME + " p "
                + "ON o." + OrderLinesEntry.COLUMN_PRODUCT_ID
                + " = p." + ProductsEntry.COLUMN_PRODUCT_ID
                + " WHERE o." + OrderLinesEntry.COLUMN_ORDER_ID
                + " = " + orderId;


        try (Connection conn = DbManager.Connect();
             Statement stmt = Objects.requireNonNull(conn).createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt(OrderLinesEntry.COLUMN_PRODUCT_ID);
                String name = rs.getString(ProductsEntry.COLUMN_PRODUCT_NAME);
                double price = rs.getDouble(ProductsEntry.COLUMN_PRICE);
                int quantity = rs.getInt(OrderLinesEntry.COLUMN_QUANTITY);
                double total = rs.getDouble(OrderLinesEntry.COLUMN_LINE_TOTAL);

                orderLine.add(new OrderLine(quantity, total, new Product(id, name, price)));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Back button action.
     * Opens a new stage {@link ViewOrdersController}
     * for two objects {@link Customer} and {@link Staff}
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        if (customer != null) {
            stage.loadStage(actionEvent, customer, ControllerService.VIEW_ORDERS);
        } else {
            stage.loadStage(actionEvent, staff, ControllerService.VIEW_ORDERS);
        }
    }


}
