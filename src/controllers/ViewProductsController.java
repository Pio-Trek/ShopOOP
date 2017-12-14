package controllers;

import data.ShopContract.ProductsEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;
import service.ControllerService;
import service.DbManager;
import service.ProductList;
import service.StageService;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ViewProductsController {

    private final List<String> categoryList = ProductList.getCategoryList();
    private final List<Integer> quantityNumbers = ProductList.getQuantityNumbers();

    private ObservableList<OrderLine> userBasket = FXCollections.observableArrayList();
    //private Map<Product, Integer> userBasket = new HashMap<>();

    @FXML
    private Button buttonEditProduct;
    @FXML
    private Button buttonAddProduct;
    @FXML
    private Button buttonDeleteProduct;
    @FXML
    private Button buttonAddToBasket;
    @FXML
    private Button buttonViewBasket;
    @FXML
    private ComboBox<Integer> comboBoxQuantity;
    @FXML
    private ListView<String> listViewCategory;
    @FXML
    private ListView<Product> listViewProduct = new ListView<>();

    private Staff staff;
    private Customer customer;
    private StageService stage = new StageService();

    /**
     * Initialize the class.
     *
     * @param staff Passing a {@link Staff} object.
     */
    public void initialize(Staff staff) {
        this.staff = staff;
        buttonsStaffView();
        setListViewProduct();
    }

    public void initialize(Customer customer) {
        this.customer = customer;
        setComboBoxQuantity();
        buttonsCustomerView();
        setListViewProduct();
    }

    private void setListViewProduct() {
        listViewCategory.getItems().addAll(categoryList);
        listViewCategory.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listViewProduct.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void setComboBoxQuantity() {
        comboBoxQuantity.getItems().addAll(quantityNumbers);
        comboBoxQuantity.getSelectionModel().selectFirst();
        comboBoxQuantity.setVisibleRowCount(5);
    }

    /**
     * Mouse event handler for the Category column.
     * Populate products list in Product column as a List of Strings.
     */
    @FXML
    private void getProductList() {
        if (staff != null) {
            buttonsStaffView();
        } else {
            buttonsCustomerView();
        }

        // Clear Product column
        listViewProduct.getItems().clear();

        String sql;
        // Check if selected Category row in ListView is Clothing or Footwear
        if (Objects.equals(listViewCategory.getSelectionModel().getSelectedItem(), categoryList.get(0))) {
            sql = sqlQueryProducts(ProductsEntry.COLUMN_MEASUREMENT);
        } else {
            sql = sqlQueryProducts(ProductsEntry.COLUMN_SIZE);
        }

        List<Product> productList = ProductList.getFromDb(sql);

        if (productList != null) {
            // Hold the list of Product in a ListView
            listViewProduct.getItems().addAll(productList);

            // Populate Product using a ListView setCellFactory() method

            listViewProduct.setCellFactory(lv -> new ListCell<>() {
                @Override
                public void updateItem(Product product, boolean empty) {
                    super.updateItem(product, empty);
                    setText(empty ? null : product.getProductName());
                }
            });
        }
    }

    /**
     * Create the SQL SELECT Query to fetch the Product data from database.
     *
     * @param columnName of Products. For Clothing: Measurement. For Footwear: Size.
     * @return String with SQL statement to select all Products sorted ascending by "ProductName".
     */
    private String sqlQueryProducts(String columnName) {
        return "SELECT * FROM " + ProductsEntry.TABLE_NAME + " WHERE "
                + columnName + " IS NOT NULL AND "
                + columnName + " != '' ORDER BY "
                + ProductsEntry.COLUMN_PRODUCT_NAME + " ASC";
    }

    /**
     * Set disable buttons for Staff view and when no product is selected.
     */
    private void buttonsStaffView() {
        buttonsStaffViewVisible(true);

        if (!buttonDeleteProduct.isDisable() && !buttonEditProduct.isDisable()) {
            buttonsSetDisable(true);
        } else if (listViewProduct.getSelectionModel().getSelectedItem() != null) {
            buttonsSetDisable(false);
        }
    }

    private void buttonsCustomerView() {
        buttonsStaffViewVisible(false);

        if (!buttonAddToBasket.isDisable()
                && !buttonViewBasket.isDisable()
                && !comboBoxQuantity.isDisable()) {
            buttonsSetDisable(true);
        } else if (listViewProduct.getSelectionModel().getSelectedItem() != null) {
            buttonsSetDisable(false);
        }
    }

    /**
     * Mouse event handler for the Product column.
     * Set enable Add and Edit button, when product is selected.
     */
    @FXML
    private void buttonsSetEnable() {
        if (listViewProduct.getSelectionModel().getSelectedItem() != null) {
            buttonsSetDisable(false);
        }
    }

    private void buttonsSetDisable(boolean value) {
        buttonDeleteProduct.setDisable(value);
        buttonEditProduct.setDisable(value);
        comboBoxQuantity.setDisable(value);
        buttonAddToBasket.setDisable(value);
        buttonViewBasket.setDisable(value);
    }

    private void buttonsStaffViewVisible(boolean value) {
        comboBoxQuantity.setVisible(!value);
        buttonAddToBasket.setVisible(!value);
        buttonViewBasket.setVisible(!value);
        buttonAddProduct.setVisible(value);
        buttonEditProduct.setVisible(value);
        buttonDeleteProduct.setVisible(value);
    }

    /**
     * Action for Edit button.
     * Fetch the Product data from the database and open a new stage {@link EditProductsController}.
     */
    @FXML
    private void editProduct(ActionEvent actionEvent) throws IOException {

        String sql = "SELECT * FROM " + ProductsEntry.TABLE_NAME + " WHERE "
                + ProductsEntry.COLUMN_PRODUCT_ID + " = '" + currentProduct().getProductId() + "' AND "
                + ProductsEntry.COLUMN_PRODUCT_NAME + " = '" + currentProduct().getProductName() + "'";

        try (Connection conn = DbManager.Connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int productId = rs.getInt(ProductsEntry.COLUMN_PRODUCT_ID);
            String productName = rs.getString(ProductsEntry.COLUMN_PRODUCT_NAME);
            double price = rs.getDouble(ProductsEntry.COLUMN_PRICE);
            int stockLevel = rs.getInt(ProductsEntry.COLUMN_STOCK_LEVEL);

            // If user selected Clothing product
            if (Objects.equals(listViewCategory.getSelectionModel().getSelectedItem(), categoryList.get(0))) {
                String measurement = rs.getString(ProductsEntry.COLUMN_MEASUREMENT);
                Clothing clothing = new Clothing(productId, productName, price, stockLevel, measurement);
                stage.loadStage(actionEvent, staff, clothing, null);

                // If user selected Footwear product
            } else {
                int size = rs.getInt(ProductsEntry.COLUMN_SIZE);
                Footwear footwear = new Footwear(productId, productName, price, stockLevel, size);
                stage.loadStage(actionEvent, staff, null, footwear);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Action for Delete button.
     * Displays an alert box before deleting a product from database specified by the ID.
     */
    @FXML
    private void deleteProduct() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Are you sure you want to delete product?");
        alert.setContentText("PRODUCT ID: " + currentProduct().getProductId()
                + "\nPRODUCT NAME: " + currentProduct().getProductName());

        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {

            String sql = "DELETE FROM " + ProductsEntry.TABLE_NAME + " WHERE "
                    + ProductsEntry.COLUMN_PRODUCT_ID + " = ?";

            try (Connection conn = DbManager.Connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Set the product ID
                pstmt.setInt(1, currentProduct().getProductId());
                // execute the delete statement
                pstmt.executeUpdate();

                // Refresh Product column
                listViewProduct.getSelectionModel().clearSelection();
                getProductList();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Get the current, selected Product ID and name.
     *
     * @return Product object.
     */
    private Product currentProduct() {
        Product selected = listViewProduct.getSelectionModel().getSelectedItem();
        int selectedId = selected.getProductId();
        String selectedName = selected.getProductName();
        double selectedPrice = selected.getPrice();
        int selectedStockLevel = selected.getStockLevel();

        return new Product(selectedId, selectedName, selectedPrice, selectedStockLevel);
    }


    /**
     * Add Product button action.
     * Opens a new stage {@link EditProductsController}
     */
    @FXML
    private void addNewProduct(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, staff, ControllerService.EDIT_PRODUCTS);
    }

    @FXML
    private void addToBasket() {
        int quantity = comboBoxQuantity.getValue();
        double total = currentProduct().getPrice() * quantity;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product added to basket");
        alert.setHeaderText(null);
        alert.setContentText("I have added: " + currentProduct().getProductName()
                + "\nPrice: " + currentProduct().getPrice()
                + "\nQuantity: " + comboBoxQuantity.getValue());

        alert.showAndWait();

        UUID uniqueID = UUID.randomUUID();

        userBasket.add(new OrderLine(uniqueID.toString(), quantity, total, currentProduct()));

    }

    @FXML
    private void viewBasket(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, userBasket, ControllerService.CUSTOMER_BASKET);
    }

    /**
     * Back button action.
     * Opens a new stage {@link StaffHomeController}
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        if (staff != null) {
            stage.loadStage(actionEvent, staff, ControllerService.STAFF_HOME);
        } else {
            if (customer.isRegistered()) {
                stage.loadStage(actionEvent, customer, ControllerService.CUSTOMER_HOME);
            } else {
                stage.loadStage(actionEvent, ControllerService.MAIN_MENU);
            }
        }
    }

}