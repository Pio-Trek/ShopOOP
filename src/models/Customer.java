package models;

import java.util.HashMap;

/**
 * Customer data model class
 */
public class Customer extends User {

    // Private attributes
    private String addressLine1;
    private String addressLine2;
    private String town;
    private String postcode;
    private HashMap<Integer, Order> orders;
    private boolean isRegistered = true;

    // Default constructor
    public Customer() {
        super();
        this.addressLine1 = "";
        this.addressLine2 = "";
        this.town = "";
        this.postcode = "";
        this.isRegistered = false;
    }

    // Overloaded constructor
    public Customer(String username, String password, String firstName, String lastName, String addressLine1, String addressLine2, String town, String postcode) {
        super(username, password, firstName, lastName);
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.town = town;
        this.postcode = postcode;
    }

    public void addOrder(Order o) {
        //TODO... ADD ORDER
    }

    public HashMap<Integer, Order> findAllCompleteOrders() {
        //TODO... FIND ALL COMPLETE ORDERS
        return orders;
    }

    public Order findLastOrder() {
        //TODO... FIND LAST ORDERS
        return null;
    }

    // Getters
    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getTown() {
        return town;
    }

    public String getPostcode() {
        return postcode;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    // Setters
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }
}