package models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * OrderLine data model class
 */
public class OrderLine {
    private final SimpleStringProperty orderLineId;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty lineTotal;
    private Product product;


    // Overloaded constructor
    public OrderLine(String orderLineId, Integer quantity, Double lineTotal, Product product) {
        this.orderLineId = new SimpleStringProperty(orderLineId);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.lineTotal = new SimpleDoubleProperty(lineTotal);
        this.product = product;
    }


    // Getters
    public String getOrderLineId() {
        return orderLineId.get();
    }

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
