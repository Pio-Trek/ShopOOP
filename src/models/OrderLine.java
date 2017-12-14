package models;

/**
 * OrderLine data model class
 */
public class OrderLine {
    private int orderLineId;
    private int quantity;
    private double lineTotal;
    private Product product;


    // Overloaded constructor
    public OrderLine(int orderLineId, int quantity, double lineTotal) {
        this.orderLineId = orderLineId;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }


    // Getters
    public int getOrderLineId() {
        return orderLineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public Product getProduct() {
        return product;
    }


    // Setters
    public void setOrderLineId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
