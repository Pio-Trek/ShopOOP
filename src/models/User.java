package models;

/**
 * User data model class
 */
public class User {

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    // Default constructor with zero parameters
    public User() {
        this.username = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
    }


    // Overloaded constructor
    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String displayGreeting() {
        return "Welcome\n" + getFirstName() + " " + getLastName();
    }


    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}