package models;

import java.util.Date;

/**
 * Order data model class
 */
public class Order {
    private int orderId;
    private Date orderDate;
    private double orderTotal;
    private String status;

    // Default constructor
    public Order() {
        this.orderId = 0;
        this.orderDate = null;
        this.orderTotal = 0;
        this.status = "";
    }

    // Overloaded constructor
    public Order(int orderId, Date orderDate, double orderTotal, String status) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderTotal = orderTotal;
        this.status = status;
    }


    // Getters
    public int getOrderId() {
        return orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public String getStatus() {
        return status;
    }


    // Setters
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}