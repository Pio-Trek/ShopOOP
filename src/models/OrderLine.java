package models;

/**
 * OrderLine data model class
 */
public class OrderLine {
    private int orderLieId;
    private int quantity;
    private double lineTotal;


    // Default constructor
    public OrderLine() {
        this.orderLieId = 0;
        this.quantity = 0;
        this.lineTotal = 0;
    }


    // Overloaded constructor
    public OrderLine(int orderLieId, int quantity, double lineTotal) {
        this.orderLieId = orderLieId;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }


    // Getters
    public int getOrderLieId() {
        return orderLieId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getLineTotal() {
        return lineTotal;
    }


    // Setters
    public void setOrderLieId(int orderLieId) {
        this.orderLieId = orderLieId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
}
