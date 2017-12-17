package controllers;

import data.ShopContract.OrdersEntry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Customer;
import models.Order;
import models.Staff;
import service.ControllerService;
import service.DbManager;
import service.StageService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ViewOrdersController {

    private final String POUND_SYMBOL = "\u00A3 ";

    @FXML
    private TableView<Order> tableViewOrders;
    @FXML
    private TableColumn<Order, Integer> tableColumnId;
    @FXML
    private TableColumn<Order, String> tableColumnUsername;
    @FXML
    private TableColumn<Order, String> tableColumnDate;
    @FXML
    private TableColumn<Order, String> tableColumnTotal;
    @FXML
    private TableColumn<Order, String> tableColumnStatus;
    @FXML
    private Button buttonViewOrder;

    private StageService stage = new StageService();
    private Customer customer;
    private Staff staff;
    private ObservableList<Order> order = FXCollections.observableArrayList();

    /**
     * Initialize the class when {@link Customer} want to see orders.
     *
     * @param customer Passing a {@link Customer} object.
     */
    public void initialize(Customer customer) {
        this.customer = customer;
        setTableViewOrders();
    }

    /**
     * Initialize the class when {@link Staff} want to see all
     * customers orders.
     *
     * @param staff Passing a {@link Staff} object.
     */
    public void initialize(Staff staff) {
        this.staff = staff;
        setTableViewOrders();
    }

    /**
     * Populate TableView with {@link Order} objects.
     */
    private void setTableViewOrders() {
        buttonViewOrder.setDisable(true);

        getOrdersFromDb();

        tableViewOrders.setPlaceholder(new Label("You have no orders"));

        tableColumnId.setCellValueFactory(
                o -> new SimpleObjectProperty<>(o.getValue().getOrderId()));
        tableColumnDate.setCellValueFactory(
                o -> new SimpleObjectProperty<>(o.getValue().getOrderDate()));
        tableColumnTotal.setCellValueFactory(
                o -> new SimpleObjectProperty<>(POUND_SYMBOL + o.getValue().getOrderTotal()));
        tableColumnStatus.setCellValueFactory(
                o -> new SimpleObjectProperty<>(o.getValue().getStatus()));

        // Hide or show {@see tableColumnUsername} depends on when customer or staff is browsing orders.
        if (customer != null) {
            tableColumnUsername.setVisible(false);
        } else {
            tableColumnUsername.setCellValueFactory(o -> new SimpleObjectProperty<>(o.getValue().getUserName()));
        }

        tableViewOrders.setItems(order);
    }

    /**
     * Gets {@link Order} data from database.
     * When a customer is browsing then show only this customer orders.
     * When staff is browsing then show all customers orders.
     */
    private void getOrdersFromDb() {
        String sql;

        // When browsing as customer
        if (customer != null) {
            sql = "SELECT * FROM " + OrdersEntry.TABLE_NAME
                    + " WHERE " + OrdersEntry.COLUMN_USERNAME
                    + " = '" + customer.getUsername() + "' "
                    + "ORDER BY " + OrdersEntry.COLUMN_ORDER_DATE + " DESC";

            // When browsing as staff
        } else {
            sql = "SELECT * FROM " + OrdersEntry.TABLE_NAME
                    + " ORDER BY " + OrdersEntry.COLUMN_USERNAME + " ASC,"
                    + OrdersEntry.COLUMN_ORDER_ID + " DESC";
        }


        try (Connection conn = DbManager.Connect();
             Statement stmt = Objects.requireNonNull(conn).createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt(OrdersEntry.COLUMN_ORDER_ID);
                long date = rs.getLong(OrdersEntry.COLUMN_ORDER_DATE);
                double total = rs.getDouble(OrdersEntry.COLUMN_ORDER_TOTAL);
                String status = rs.getString(OrdersEntry.COLUMN_STATUS);
                String userName = rs.getString(OrdersEntry.COLUMN_USERNAME);

                order.add(new Order(id, formatDateTime(date), total, status, userName));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Get {@link Order} ID number from TableView and pass it to a {@link ViewOrderLinesController} stage.
     */
    @FXML
    private void viewSelectedOrder(ActionEvent actionEvent) throws Exception {
        int orderId = tableViewOrders
                .getSelectionModel()
                .selectedItemProperty()
                .get().getOrderId();

        if (customer != null) {
            stage.loadStage(actionEvent, customer, null, orderId, ControllerService.VIEW_ORDER_LINES);
        } else {
            stage.loadStage(actionEvent, null, staff, orderId, ControllerService.VIEW_ORDER_LINES);
        }
    }

    /**
     * Convert Unix epoch time to to human readable date
     */
    private String formatDateTime(long timeInMilliseconds) {
        String pattern = "dd/MM/yyyy HH:mm:ss";

        SimpleDateFormat formatter = new SimpleDateFormat((pattern), Locale.ENGLISH);
        return formatter.format(new Date(timeInMilliseconds * 1000));
    }

    /**
     * MouseEvent when product is selected then set enable {@see buttonViewOrder}
     */
    @FXML
    private void buttonSetEnable() {
        if (tableViewOrders.getSelectionModel().selectedItemProperty().get() != null) {
            buttonViewOrder.setDisable(false);
        }
    }

    /**
     * Back button action.
     * Opens a new stage {@link CustomerHomeController} when browsing as customer.
     * Opens a new stage {@link StaffHomeController} when browsing as staff.
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        if (customer != null) {
            stage.loadStage(actionEvent, customer, ControllerService.CUSTOMER_HOME);
        } else {
            stage.loadStage(actionEvent, staff, ControllerService.STAFF_HOME);
        }
    }
}
