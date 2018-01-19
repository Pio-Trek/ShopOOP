package controllers;

import data.ShopContract.ProductsEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;
import service.*;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ViewProductsController {

    private static final String NO_STOCK_BACKGROUND_COLOUR = "derive(red, 98%)";
    private final List<String> categoryList = ProductList.getCategoryList();
    private final List<Integer> quantityNumbers = ProductList.getQuantityNumbers();
    private ObservableList<OrderLine> basket = FXCollections.observableArrayList();

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
    @FXML
    private Label labelHeader;

    private Staff staff;
    private Customer customer;
    private StageService stage = new StageService();

    private DecimalFormat decimal = new DecimalFormat("##.00");


    /**
     * Initialize the class when {@link Staff} want to see, edit or delete {@link Product}s.
     *
     * @param staff Passing a {@link Staff} object.
     */
    public void initialize(Staff staff) {
        this.staff = staff;
        labelHeader.setText("Modify Products");
        buttonsStaffView();
        setListViewProduct();
    }

    /**
     * Initialize the class when {@link Customer} want to see
     * {@link Product}s or add them to a basket.
     *
     * @param customer Passing a {@link Customer} object.
     */
    public void initialize(Customer customer) {
        this.customer = customer;
        labelHeader.setText("Browse Products");
        buttonsCustomerView();
        setListViewProduct();
        buttonViewBasket.setDisable(true);
    }

    /**
     * Initialize the class when {@link Customer} going back to
     * browse product with the already created shopping basket.
     *
     * @param customer Passing a {@link Customer} object.
     * @param basket   Passing created shopping basket.
     */
    public void initialize(Customer customer, ObservableList<OrderLine> basket) {
        this.customer = customer;
        this.basket = basket;
        buttonsCustomerView();
        setListViewProduct();

        if (basket.size() > 0) {
            buttonViewBasket.setDisable(false);
        } else {
            buttonViewBasket.setDisable(true);
        }

    }

    /**
     * Populate {@see listViewCategory} with Category list and set Category and
     * Product ListView to allows for only one item to be selected at a time.
     */
    private void setListViewProduct() {
        listViewCategory.getItems().addAll(categoryList);
        listViewCategory.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listViewProduct.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Populate ComboBox with quantity numbers.
     * Quantity number depend on product stock level.
     */
    private void setComboBoxQuantity() {

        comboBoxQuantity.getSelectionModel().clearSelection();
        comboBoxQuantity.getItems().clear();

        int stockLevel;

        // Set ComboBox number to product stock level
        if (currentProduct().getStockLevel() <= 10) {
            stockLevel = currentProduct().getStockLevel();
            // Set ComboBox number to 10. This is maximum quantity number that Customer can order.
        } else {
            stockLevel = 10;
        }

        comboBoxQuantity.getItems().addAll(quantityNumbers.subList(0, stockLevel));
        comboBoxQuantity.getSelectionModel().selectFirst();
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

                    if (empty) {
                        setText(null);
                        setStyle(null);
                    } else {

                        /*
                            Set the background to red colour to indicate to Staff user that
                            stock level of a product is 0.
                         */
                        if (product.getStockLevel() == 0) {
                            setStyle("-fx-control-inner-background: " + NO_STOCK_BACKGROUND_COLOUR + ";");
                        }
                        setText(product.getProductName());
                    }
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
        String sql;

        // SQL Select Query for Staff - display all products.
        if (staff != null) {
            sql = "SELECT * FROM " + ProductsEntry.TABLE_NAME + " WHERE "
                    + columnName + " IS NOT NULL AND "
                    + columnName + " != '' ORDER BY "
                    + ProductsEntry.COLUMN_PRODUCT_NAME + " ASC";

            // SQL Select Query for Customer - display available products (stock level above 0).
        } else {
            sql = "SELECT * FROM " + ProductsEntry.TABLE_NAME + " WHERE "
                    + ProductsEntry.COLUMN_STOCK_LEVEL + " > 0 AND "
                    + columnName + " IS NOT NULL AND "
                    + columnName + " != '' ORDER BY "
                    + ProductsEntry.COLUMN_PRODUCT_NAME + " ASC";
        }

        return sql;
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

    /**
     * Set disable buttons for Customer view and when no product is selected.
     */
    private void buttonsCustomerView() {
        buttonsStaffViewVisible(false);

        if (!buttonAddToBasket.isDisable() && !comboBoxQuantity.isDisable()) {
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
            setComboBoxQuantity();
        }
    }

    /**
     * Set Enable/Disable for buttons and ComboBox when Product is selected/unselected.
     *
     * @param value TRUE/FALSE for set disable JavaFX elements.
     */
    private void buttonsSetDisable(boolean value) {
        buttonDeleteProduct.setDisable(value);
        buttonEditProduct.setDisable(value);
        comboBoxQuantity.setDisable(value);
        buttonAddToBasket.setDisable(value);
    }

    /**
     * Set visibility for buttons and ComboBox.
     *
     * @param value TRUE/FALSE for visibility JavaFX elements.
     */
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
     * Fetch the Product data from the database and open a
     * new stage {@link EditProductsController}.
     */
    @FXML
    private void editProduct(ActionEvent actionEvent) throws IOException {

        String sql = "SELECT * FROM " + ProductsEntry.TABLE_NAME + " WHERE "
                + ProductsEntry.COLUMN_PRODUCT_ID + " = '" + currentProduct().getProductId() + "' AND "
                + ProductsEntry.COLUMN_PRODUCT_NAME + " = '" + currentProduct().getProductName() + "'";

        try (Connection conn = DbManager.Connect();
             Statement stmt = Objects.requireNonNull(conn).createStatement();
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

        Optional<ButtonType> buttonResult = AlertService.showDialog(Alert.AlertType.CONFIRMATION,
                "Delete Product",
                "Are you sure you want to delete product?",
                "PRODUCT ID: " + currentProduct().getProductId()
                        + "\nPRODUCT NAME: " + currentProduct().getProductName());

        if ((buttonResult.isPresent()) && (buttonResult.get() == ButtonType.OK)) {

            String sql = "DELETE FROM " + ProductsEntry.TABLE_NAME + " WHERE "
                    + ProductsEntry.COLUMN_PRODUCT_ID + " = ?";

            try (Connection conn = DbManager.Connect();
                 PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {

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

    /**
     * Add To Basket button action.
     * Add selected Product of given quantity to a {@see basket} ObservableList
     */
    @FXML
    private void addToBasket() {
        // Checks if user already added a product to the basket
        boolean productAlreadyInBasket = basket.stream().anyMatch(p -> p.getProduct().getProductName().equals(currentProduct().getProductName()));

        if (productAlreadyInBasket) {
            AlertService.showDialog(Alert.AlertType.ERROR,
                    "Product is in the basket",
                    null,
                    "Product already exists in the basket.\n" +
                            "Please, remove it from basket before adding again.");
        } else {

            int quantity = comboBoxQuantity.getValue();
            double total = Double.parseDouble(decimal.format(currentProduct().getPrice() * quantity));

            AlertService.showDialog(Alert.AlertType.INFORMATION,
                    "Product added to basket",
                    null,
                    "I have added: " + currentProduct().getProductName()
                            + "\nPrice: " + currentProduct().getPrice()
                            + "\nQuantity: " + comboBoxQuantity.getValue());

            basket.add(new OrderLine(quantity, total, currentProduct()));
            buttonViewBasket.setDisable(false);

        }
    }

    /**
     * View Basket button action.
     * Opens a new stage {@link CustomerBasketController} with shopping basket list.
     */
    @FXML
    private void viewBasket(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, customer, basket, ControllerService.CUSTOMER_BASKET);
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