package models;

import java.util.Objects;

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

    /**
     * Compare Clothing objects.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clothing clothing = (Clothing) o;
        return Objects.equals(getProductName(), clothing.getProductName()) &&
                Objects.equals(getPrice(), clothing.getPrice()) &&
                Objects.equals(measurement, clothing.measurement) &&
                Objects.equals(getStockLevel(), clothing.getStockLevel());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}