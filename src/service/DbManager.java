package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database helper for Shop app. Manages connection with a database.
 */
public class DbManager {

    public static Connection Connect() {

        // Name of the SQL driver
        final String DRIVER = "org.sqlite.JDBC";
        // Name of the database file
        final String DATABASE_NAME = "ShopDB.sqlite";

        String url;

        // Checks what type of Operation System is used and change url to a database file
        String OS = (System.getProperty("os.name")).toUpperCase();

        if (OS.contains("WIN")) {
            url = "jdbc:sqlite:src\\data\\" + DATABASE_NAME;
        } else {
            url = "jdbc:sqlite:src/data/" + DATABASE_NAME;
        }

        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(url);

        } catch (ClassNotFoundException e) {
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}