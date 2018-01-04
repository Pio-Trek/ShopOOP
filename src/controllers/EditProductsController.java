package controllers;

import data.ShopContract.ProductsEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Clothing;
import models.Footwear;
import models.Staff;
import service.ControllerService;
import service.DbManager;
import service.LabelStatusService;
import service.StageService;
import validation.ProductValidation;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class EditProductsController {

    public ToggleGroup productType;
    @FXML
    private RadioButton radioButtonClothing;
    @FXML
    private RadioButton radioButtonFootwear;
    @FXML
    private Label labelHeader;
    @FXML
    private Label labelStatus;
    @FXML
    private Button buttonSubmit;
    @FXML
    private Button buttonClear;
    @FXML
    private TextField inputProductName;
    @FXML
    private TextField inputStockLevel;
    @FXML
    private TextField inputPrice;
    @FXML
    private TextField inputProductId;
    @FXML
    private Label labelSizes;
    @FXML
    private Label labelType;
    @FXML
    private Label labelId;
    @FXML
    private TextField inputMeasurement;
    @FXML
    private TextField labelSize;

    private Staff staff;
    private StageService stage = new StageService();

    private Clothing clothing;
    private Footwear footwear;

    /**
     * Initialize the class when adding new Product.
     * Change JavaFX elements to disabled until a user will choose what type of Product to add.
     * Disable all elements uses to edit Product.
     *
     * @param staff Staff object.
     */
    public void initialize(Staff staff) {
        this.staff = staff;
        labelHeader.setText("Add New Product");

        inputProductId.setVisible(false);
        labelId.setVisible(false);
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
        labelHeader.setText("Edit Clothing");
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
        labelHeader.setText("Edit Footwear");
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
        labelSize.clear();
        labelSize.setVisible(false);
        labelSizes.setText("Measurement:");
    }


    /**
     * Perform an action when Footwear Radio Button is selected.
     */
    @FXML
    private void selectFootwear() {
        setElementsDisabled(false);
        inputMeasurement.clear();
        inputMeasurement.setVisible(false);
        labelSizes.setText("Size:");
    }

    /**
     * Set visibility or disable/enable status for JavaFX elements.
     * Used to prevent user to choose Clothing when Footwear is
     * selected and vice versa.
     */
    private void setElementsDisabled(boolean value) {
        inputProductName.setDisable(value);
        inputPrice.setDisable(value);
        inputStockLevel.setDisable(value);
        buttonSubmit.setDisable(value);
        buttonClear.setDisable(value);
        inputMeasurement.setVisible(!value);
        labelSize.setVisible(!value);
    }

    /**
     * Set Radio Buttons to invisible when editing existing Product.
     */
    private void setRadioButtonsInvisible() {
        radioButtonClothing.setVisible(false);
        radioButtonFootwear.setVisible(false);
        labelType.setVisible(false);
    }

    /**
     * Display the Clothing object in JavaFX elements for editing.
     *
     * @param clothing Clothing object.
     */
    private void displayProduct(Clothing clothing) {
        inputProductId.setText(String.valueOf(clothing.getProductId()));
        inputProductName.setText(clothing.getProductName());
        inputPrice.setText(String.valueOf(clothing.getPrice()));
        inputStockLevel.setText(String.valueOf(clothing.getStockLevel()));
        inputMeasurement.setText(clothing.getMeasurement());
    }

    /**
     * Display the Footwear object in JavaFX elements for editing.
     *
     * @param footwear Footwear object.
     */
    private void displayProduct(Footwear footwear) {
        inputProductId.setText(String.valueOf(footwear.getProductId()));
        inputProductName.setText(footwear.getProductName());
        inputPrice.setText(String.valueOf(footwear.getPrice()));
        inputStockLevel.setText(String.valueOf(footwear.getStockLevel()));
        labelSize.setText(String.valueOf(footwear.getSize()));
    }


    /**
     * Action for Submit button for Add and Edit mode.
     */
    @FXML
    private void saveProduct() {

        // Validate user input
        ProductValidation result = ProductValidation.validResult(inputProductName.getText(), inputPrice.getText(), inputStockLevel.getText(), inputMeasurement.getText(), labelSize.getText());

        // Checks if Clothing or Footwear object is not null then will update existing Product
        // otherwise will save a new Product into database.
        if (result.isValid()) {
            if (clothing != null || footwear != null) {
                updateEditedProduct();
            } else {
                saveNewProduct();
            }
        } else {
            LabelStatusService.getError(labelStatus, result.getErrorMessage());
        }
    }

    /**
     * Get user input and save new Product into database.
     * Indicates when {@see btnSubmit} is press.
     */
    private void saveNewProduct() {
        if (productNotInDb(inputProductName.getText().trim())) {
            insertNewProduct();
        } else {
            LabelStatusService.getError(labelStatus, "Error: Product name already exist");
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
             Statement stmt = Objects.requireNonNull(conn).createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return !rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Creates a new Product record in database.
     */
    private void insertNewProduct() {
        String productName = inputProductName.getText().trim();
        double price = Double.parseDouble(inputPrice.getText().trim());
        int stockLevel = Integer.parseInt(inputStockLevel.getText().trim());
        String measurement = inputMeasurement.getText().trim();

        String sql = "INSERT INTO " + ProductsEntry.TABLE_NAME + "("
                + ProductsEntry.COLUMN_PRODUCT_NAME + ", "
                + ProductsEntry.COLUMN_PRICE + ", "
                + ProductsEntry.COLUMN_STOCK_LEVEL + ", "
                + ProductsEntry.COLUMN_MEASUREMENT + ", "
                + ProductsEntry.COLUMN_SIZE + ") VALUES(?,?,?,?,?)";

        try (Connection conn = DbManager.Connect();
             PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {

            pstmt.setString(1, productName);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stockLevel);
            pstmt.setString(4, measurement);

            if (labelSize.getText().isEmpty()) {
                pstmt.setString(5, null);
            } else {
                pstmt.setInt(5, Integer.parseInt(labelSize.getText().trim()));
            }

            int row = pstmt.executeUpdate();

            if (row == 1) {
                LabelStatusService.getConfirmation(labelStatus, "Product added successful");
                clear();
            } else {
                LabelStatusService.getConfirmation(labelStatus, "Error: Could not add new product");
            }

        } catch (SQLException e) {
            LabelStatusService.getError(labelStatus, e.getMessage());
        }
    }


    /**
     * Indicates when {@see buttonSubmit} is press.
     * Get user input, compare it with passed {@see clothing} object
     * and if is different then update existing Product in database.
     */
    private void updateEditedProduct() {

        int productId = Integer.parseInt(inputProductId.getText());
        String productName = inputProductName.getText().trim();
        double price = Double.parseDouble(inputPrice.getText().trim());
        int stockLevel = Integer.parseInt(inputStockLevel.getText().trim());

        if (clothing != null) {
            String measurement = inputMeasurement.getText().trim();
            Clothing currentClothing = new Clothing(productId, productName, price, stockLevel, measurement);

            // Compare Clothing objects
            if (currentClothing.equals(clothing)) {
                LabelStatusService.getError(labelStatus, "No changes has been made");
            } else {
                updateDb();
            }
        } else if (footwear != null) {
            int size = Integer.parseInt(labelSize.getText().trim());
            Footwear currentFootwear = new Footwear(productId, productName, price, stockLevel, size);

            // Compare Footwear objects.
            if (currentFootwear.equals(footwear)) {
                LabelStatusService.getError(labelStatus, "No changes has been made");
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
             PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {

            // Set the corresponding param
            pstmt.setString(1, inputProductName.getText().trim());
            pstmt.setDouble(2, Double.parseDouble(inputPrice.getText().trim()));
            pstmt.setInt(3, Integer.parseInt(inputStockLevel.getText().trim()));
            pstmt.setString(4, inputMeasurement.getText().trim());

            if (labelSize.getText().isEmpty()) {
                pstmt.setString(5, null);
            } else {
                pstmt.setInt(5, Integer.parseInt(labelSize.getText().trim()));
            }

            pstmt.setInt(6, Integer.parseInt(inputProductId.getText()));

            int row = pstmt.executeUpdate();

            if (row == 1) {
                LabelStatusService.getConfirmation(labelStatus, "Product updated successful");
            } else {
                LabelStatusService.getConfirmation(labelStatus, "Error: Could not update product");
            }

        } catch (SQLException e) {
            LabelStatusService.getConfirmation(labelStatus, e.getMessage());
        }
    }

    /**
     * Clear all input elements.
     * Indicates when {@see btnClear} is pressed.
     */
    public void clear() {
        inputProductName.clear();
        inputPrice.clear();
        inputStockLevel.clear();
        inputMeasurement.clear();
        labelSize.clear();
    }

    /**
     * Back button action.
     * Opens a new stage {@link ViewProductsController}
     */
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        stage.loadStage(actionEvent, staff, ControllerService.VIEW_PRODUCTS);
    }
}