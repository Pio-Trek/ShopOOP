package models;


import java.util.Objects;

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

    /**
     * Compare Customer objects.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getPassword(), customer.getPassword()) &&
                Objects.equals(getFirstName(), customer.getFirstName()) &&
                Objects.equals(getLastName(), customer.getLastName()) &&
                Objects.equals(addressLine1, customer.addressLine1) &&
                Objects.equals(addressLine2, customer.addressLine2) &&
                Objects.equals(town, customer.town) &&
                Objects.equals(postcode, customer.postcode);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}