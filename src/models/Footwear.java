package models;

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
}