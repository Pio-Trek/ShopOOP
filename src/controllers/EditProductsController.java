package controllers;

import data.ShopContract.ProductsEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import models.Clothing;
import models.Footwear;
import models.Staff;
import service.ControllerService;
import service.DbManager;
import service.StageService;
import validation.CompareObjects;
import validation.ProductValidation;

import java.io.IOException;
import java.sql.*;

public class EditProductsController {

    @FXML
    private RadioButton rb_clothing;
    @FXML
    private RadioButton rb_footwear;
    @FXML
    private Label lblHeader;
    @FXML
    private Label lblStatus;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnClear;
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtStockLevel;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtProductId;
    @FXML
    private Label lblSizes;
    @FXML
    private Label lblType;
    @FXML
    private Label lblId;
    @FXML
    private TextField txtMeasurement;
    @FXML
    private TextField txtSize;

    private Staff staff;
    private StageService stage = new StageService();
    private Connection conn;
    private Statement stmt;

    private Clothing clothing;
    private Footwear footwear;

    /**
     * Initialize the class when add new Product.
     * Change JavaFX elements to disabled unit user will chose what type of Product to add.
     * Disable all elements uses to edit Product.
     *
     * @param staff Staff object.
     */
    public void initialize(Staff staff) {
        this.staff = staff;
        lblHeader.setText("Add New Product");

        txtProductId.setVisible(false);
        lblId.setVisible(false);
        setElementsDisabled(true);
    }

    /**
     * Initialize the class when edit existing Clothing product.
     * Disable add elements uses to add new Product.
     *
     * @param staff    Staff object.
     * @param clothing Clothing object.
     */
    public void initialize(Staff staff, Clothing clothing) {
        this.staff = staff;
        this.clothing = clothing;
        lblHeader.setText("Edit Clothing");
        selectClothing();
        setRadioButtonsInvisible();

        displayProduct(clothing);
    }

    /**
     * Initialize the class when edit existing Footwear product.
     * Disable add elements uses to add new Product.
     *
     * @param staff    Staff object.
     * @param footwear Footwear object.
     */
    public void initialize(Staff staff, Footwear footwear) {
        this.staff = staff;
        this.footwear = footwear;
        lblHeader.setText("Edit Footwear");
        selectFootwear();
        setRadioButtonsInvisible();

        displayProduct(footwear);
    }

    /**
     * Perform an action when Clothing Radio Button is selected.
     */
    @FXML
    private void selectClothing() {
        setElementsDisabled(false);
        txtSize.clear();
        txtSize.setVisible(false);
        lblSizes.setText("Measurement:");
    }


    /**
     * Perform an action when Footwear Radio Button is selected.
     */
    @FXML
    private void selectFootwear() {
        setElementsDisabled(false);
        txtMeasurement.clear();
        txtMeasurement.setVisible(false);
        lblSizes.setText("Size:");
    }

    /**
     * Set visibility or disable/enable status for JavaFX elements.
     * Used to prevent user to choose Clothing or Footwear.
     */
    private void setElementsDisabled(boolean value) {
        txtProductName.setDisable(value);
        txtPrice.setDisable(value);
        txtStockLevel.setDisable(value);
        btnSubmit.setDisable(value);
        btnClear.setDisable(value);
        txtMeasurement.setVisible(!value);
        txtSize.setVisible(!value);
    }

    /**
     * Set Radio Buttons to invisible when editing existing Product.
     */
    private void setRadioButtonsInvisible() {
        rb_clothing.setVisible(false);
        rb_footwear.setVisible(false);
        lblType.setVisible(false);
    }

    /**
     * Display the Clothing object in JavaFX elements for editing.
     *
     * @param clothing Clothing object.
     */
    private void displayProduct(Clothing clothing) {
        txtProductId.setText(String.valueOf(clothing.getProductId()));
        txtProductName.setText(clothing.getProductName());
        txtPrice.setText(String.valueOf(clothing.getPrice()));
        txtStockLevel.setText(String.valueOf(clothing.getStockLevel()));
        txtMeasurement.setText(clothing.getMeasurement());
    }

    /**
     * Display the Footwear object in JavaFX elements for editing.
     *
     * @param footwear Footwear object.
     */
    private void displayProduct(Footwear footwear) {
        txtProductId.setText(String.valueOf(footwear.getProductId()));
        txtProductName.setText(footwear.getProductName());
        txtPrice.setText(String.valueOf(footwear.getPrice()));
        txtStockLevel.setText(String.valueOf(footwear.getStockLevel()));
        txtSize.setText(String.valueOf(footwear.getSize()));
    }


    /**
     * Action for Submit button for Add and Edit mode.
     */
    @FXML
    private void saveProduct() throws SQLException {

        // Validate user input
        ProductValidation result = ProductValidation.validResult(txtProductName.getText(), txtPrice.getText(), txtStockLevel.getText(), txtMeasurement.getText(), txtSize.getText());

        // Checks if Clothing or Footwear object is not null then will update existing Product
        // otherwise will save a new Product into database.
        if (result.isValid()) {
            if (clothing != null || footwear != null) {
                updateEditedProduct();
            } else {
                saveNewProduct();
            }
        } else {
            lblStatus.setText(result.getErrorMessage());
        }
    }

    /**
     * Get user input and save new Product into database.
     * Indicates when {@see btnSubmit} is press.
     */
    private void saveNewProduct() {
        if (productNotInDb(txtProductName.getText().trim())) {
            insertNewProduct();
        } else {
            lblStatus.setText("Error: Product name already exist");
        }
    }


    /**
     * Checks if Product already exist in database.
     *
     * @param productName to be check in database.
     * @return true Product not exist / false exist.
     */
    private boolean productNotInDb(String productName) {
        String sql = "SELECT " + ProductsEntry.COLUMN_PRODUCT_NAME + " FROM "
                + ProductsEntry.TABLE_NAME + " WHERE "
                + ProductsEntry.COLUMN_PRODUCT_NAME + " = '" + productName + "'";

        try (Connection conn = DbManager.Connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return !rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    private void insertNewProduct() {
        String productName = txtProductName.getText().trim();
        double price = Double.parseDouble(txtPrice.getText().trim());
        int stockLevel = Integer.parseInt(txtStockLevel.getText().trim());
        String measurement = txtMeasurement.getText().trim();

        String sql = "INSERT INTO " + ProductsEntry.TABLE_NAME + "("
                + ProductsEntry.COLUMN_PRODUCT_NAME + ", "
                + ProductsEntry.COLUMN_PRICE + ", "
                + ProductsEntry.COLUMN_STOCK_LEVEL + ", "
                + ProductsEntry.COLUMN_MEASUREMENT + ", "
                + ProductsEntry.COLUMN_SIZE + ") VALUES(?,?,?,?,?)";

        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productName);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stockLevel);
            pstmt.setString(4, measurement);

            if (txtSize.getText().isEmpty()) {
                pstmt.setString(5, null);
            } else {
                pstmt.setInt(5, Integer.parseInt(txtSize.getText().trim()));
            }

            pstmt.executeUpdate();

            lblStatus.setText("Product added successful");
            clear();
        } catch (SQLException e) {
            lblStatus.setText(e.getMessage());
        }
    }


    /**
     * Indicates when {@see btnSubmit} is press.
     * Get user input, compare it with passed {@see clothing} object and if is differentthen update existing Product
     * in database.
     */
    private void updateEditedProduct() throws SQLException {

        int productId = Integer.parseInt(txtProductId.getText());
        String productName = txtProductName.getText().trim();
        double price = Double.parseDouble(txtPrice.getText().trim());
        int stockLevel = Integer.parseInt(txtStockLevel.getText().trim());

        if (clothing != null) {
            String measurement = txtMeasurement.getText().trim();
            Clothing currentClothing = new Clothing(productId, productName, price, stockLevel, measurement);

            // Compare Clothing objects
            if (CompareObjects.compare(currentClothing, clothing)) {
                lblStatus.setText("No changes has been made");
            } else {
                updateDb();
            }
        } else if (footwear != null) {
            int size = Integer.parseInt(txtSize.getText().trim());
            Footwear currentFootwear = new Footwear(productId, productName, price, stockLevel, size);

            // Compare Footwear objects.
            if (CompareObjects.compare(currentFootwear, footwear)) {
                lblStatus.setText("No changes has been made");
            } else {
                updateDb();
            }
        }

    }

    /**
     * Update data of a Product specified by the ID.
     */
    private void updateDb() {
        String sql = "UPDATE " + ProductsEntry.TABLE_NAME + " SET "
                + ProductsEntry.COLUMN_PRODUCT_NAME + " = ?, "
                + ProductsEntry.COLUMN_PRICE + " = ?, "
                + ProductsEntry.COLUMN_STOCK_LEVEL + " = ?, "
                + ProductsEntry.COLUMN_MEASUREMENT + " = ?, "
                + ProductsEntry.COLUMN_SIZE + " = ?"
                + " WHERE " + ProductsEntry.COLUMN_PRODUCT_ID + " = ?";


        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the corresponding param
            pstmt.setString(1, txtProductName.getText().trim());
            pstmt.setDouble(2, Double.parseDouble(txtPrice.getText().trim()));
            pstmt.setInt(3, Integer.parseInt(txtStockLevel.getText().trim()));
            pstmt.setString(4, txtMeasurement.getText().trim());

            if (txtSize.getText().isEmpty()) {
                pstmt.setString(5, null);
            } else {
                pstmt.setInt(5, Integer.parseInt(txtSize.getText().trim()));
            }

            pstmt.setInt(6, Integer.parseInt(txtProductId.getText()));

            // Update
            pstmt.executeUpdate();

            lblStatus.setText("Product updated successful");
        } catch (SQLException e) {
            lblStatus.setText(e.getMessage());
        }
    }

    /**
     * Clear all input elements.
     * Indicates when {@see btnClear} is pressed.
     */
    public void clear() {
        txtProductName.clear();
        txtPrice.clear();
        txtStockLevel.clear();
        txtMeasurement.clear();
        txtSize.clear();
    }

    /**
     * Back button action.
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, staff, ControllerService.MODIFY_PRODUCTS);
    }
}