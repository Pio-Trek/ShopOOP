package service;

import data.ShopContract.CustomerEntry;
import data.ShopContract.StaffEntry;
import models.Customer;
import models.Staff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Login helper. Manages if user exist in a database.
 */
public class LoginService {

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    public boolean execute(String tableName, String username, String password) {

        String sql = "SELECT * FROM " + tableName + " WHERE "
                + StaffEntry.COLUMN_USERNAME + " = '" + username + "' AND "
                + StaffEntry.COLUMN_PASSWORD + " = '" + password + "'";

        try {
            conn = DbManager.Connect();
            if (conn != null) {
                stmt = conn.createStatement();
            }

            rs = stmt.executeQuery(sql);
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    /**
     * Gets user data from a database and assign to a variable.
     *
     * @return New {@link Customer} object.
     */
    public Customer getCustomer() {

        try {
            String username = rs.getString(CustomerEntry.COLUMN_USERNAME);
            String password = rs.getString(CustomerEntry.COLUMN_PASSWORD);
            String firstName = rs.getString(CustomerEntry.COLUMN_FIRST_NAME);
            String lastName = rs.getString(CustomerEntry.COLUMN_LAST_NAME);
            String addressLine1 = rs.getString(CustomerEntry.COLUMN_ADDRESS_LINE_1);
            String addressLine2 = rs.getString(CustomerEntry.COLUMN_ADDRESS_LINE_2);
            String town = rs.getString(CustomerEntry.COLUMN_TOWN);
            String postcode = rs.getString(CustomerEntry.COLUMN_POSTCODE);

            conn.close();

            return new Customer(username, password, firstName, lastName, addressLine1, addressLine2, town, postcode);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets user data from a database and assign to a variable.
     *
     * @return New {@link Staff} object.
     */
    public Staff getStaff() {

        try {
            String username = rs.getString(StaffEntry.COLUMN_USERNAME);
            String password = rs.getString(StaffEntry.COLUMN_PASSWORD);
            String firstName = rs.getString(StaffEntry.COLUMN_FIRST_NAME);
            String lastName = rs.getString(StaffEntry.COLUMN_LAST_NAME);
            double salary = rs.getDouble(StaffEntry.COLUMN_SALARY);
            String position = rs.getString(StaffEntry.COLUMN_POSITION);

            conn.close();

            return new Staff(username, password, firstName, lastName, salary, position);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}