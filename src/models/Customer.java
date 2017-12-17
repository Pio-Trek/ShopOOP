package models;


/**
 * Customer data model class
 */
public class Customer extends User {

    // Private attributes
    private String addressLine1;
    private String addressLine2;
    private String town;
    private String postcode;
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
}