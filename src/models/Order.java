package models;

import java.util.Date;
import java.util.HashMap;

/**
 * Order data model class
 */
public class Order {
    private int orderId;
    private Date orderDate;
    private double orderTotal;
    private String status;
    private HashMap<Integer, OrderLine> orderLines;

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

    public void addOrderLine(OrderLine ol, boolean isRegistered) {
        //TODO... ADD ORDER LINE
    }

    public void removeOrderLine(int productId, boolean isRegistered) {
        //TODO... REMOVE ORDER LINE
    }

    public int getQuantityOfProduct(int productId) {
        //TODO... GET QUANTITY OF PRODUCT
        return 0;
    }

    public int generateUniqueOrderLineId() {
        //TODO... Generate Unique Order Line ID
        return 0;
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

    public HashMap<Integer, OrderLine> getOrderLines() {
        return orderLines;
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

    public void setOrderLines(HashMap<Integer, OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}