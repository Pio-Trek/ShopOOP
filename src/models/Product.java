package models;

/**
 * Product data model class
 */
public class Product {
    private int productId;
    private String productName;
    private double price;
    private int stockLevel;


    // Default constructor with zero parameters
    public Product() {
        this.productId = 0;
        this.productName = "";
        this.price = 0.0;
        this.stockLevel = 0;
    }

    // Overloaded constructor used only to display in a ListView in {@link ViewProductsController}
    public Product(int productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    // Overloaded constructor
    public Product(int productId, String productName, double price, int stockLevel) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stockLevel = stockLevel;
    }

    // Getters
    public int getProductId() {
        return productId;
    }

    // Setters
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }
}