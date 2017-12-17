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

    // Overloaded constructor uses for {@link OrderLine}
    public Product(int productId, String productName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
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

    public String getProductName() {
        return productName;
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
}