package models;

import java.util.Objects;

/**
 * Footwear data model class
 */
public class Footwear extends Product {

    // Private attributes
    private int size;

    // Default constructor
    public Footwear() {
        super();
        this.size = 0;
    }

    // Overloaded constructor
    public Footwear(int productId, String productName, double price, int stockLevel, int size) {
        super(productId, productName, price, stockLevel);
        this.size = size;
    }

    // Getter
    public int getSize() {
        return size;
    }

    // Setter
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Compare Footwear objects.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Footwear footwear = (Footwear) o;
        return Objects.equals(getProductName(), footwear.getProductName()) &&
                Objects.equals(getPrice(), footwear.getPrice()) &&
                Objects.equals(size, footwear.size) &&
                Objects.equals(getStockLevel(), footwear.getStockLevel());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}