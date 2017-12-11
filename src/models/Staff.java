package models;

/**
 * Staff data model class
 */
public class Staff extends User {

    // Private attributes
    private double salary;
    private String position;

    // Default constructor
    public Staff() {
        super();
        this.salary = 0;
        this.position = "";
    }

    public Staff(String username, String password, String firstName, String lastName, double salary, String position) {
        super(username, password, firstName, lastName);
        this.salary = salary;
        this.position = position;
    }

    // Getters
    public double getSalary() {
        return salary;
    }

    // Setters
    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}