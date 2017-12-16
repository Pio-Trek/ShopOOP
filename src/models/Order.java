package models;

/**
 * Order data model class
 */
public class Order {
    private int orderId;
    private String orderDate;
    private double orderTotal;
    private String status;
    private String userName;

    // Default constructor
    public Order() {
        this.orderId = 0;
        this.orderDate = null;
        this.orderTotal = 0;
        this.status = "";
        this.userName = "";
    }

    // Overloaded constructor
    public Order(int orderId, String orderDate, double orderTotal, String status, String userName) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderTotal = orderTotal;
        this.status = status;
        this.userName = userName;
    }
    

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }
}