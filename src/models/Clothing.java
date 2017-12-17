package models;

/**
 * Clothing data model class
 */
public class Clothing extends Product {

    // Private attributes
    private String measurement;

    // Default constructor
    public Clothing() {
        super();
        this.measurement = "";
    }

    // Overloaded constructor
    public Clothing(int productId, String productName, double price, int stockLevel, String measurement) {
        super(productId, productName, price, stockLevel);
        this.measurement = measurement;
    }

    // Getter
    public String getMeasurement() {
        return measurement;
    }
}