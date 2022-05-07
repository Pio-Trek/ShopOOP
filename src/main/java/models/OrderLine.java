package models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * OrderLine data model class
 */
public class OrderLine {

    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty lineTotal;
    private Product product;


    // Overloaded constructor
    public OrderLine(Integer quantity, Double lineTotal, Product product) {
        this.quantity = new SimpleIntegerProperty(quantity);
        this.lineTotal = new SimpleDoubleProperty(lineTotal);
        this.product = product;
    }


    // Getters
    public int getQuantity() {
        return quantity.get();
    }

    public double getLineTotal() {
        return lineTotal.get();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
