package service;

import data.ShopContract.ProductsEntry;
import models.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Get Product list from database (IDs and Names) and add to a List of Products
 */
public class ProductList {

    // List of categories
    private static final List<String> categoryList = List.of("Clothing", "Footwear");

    // List of products
    private static List<Product> productList = new ArrayList<>();

    // Quantity list
    private static List<Integer> quantityNumbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


    /**
     * Constructor to prevent from accidentally instantiating this class
     */
    private ProductList() {
        throw new IllegalStateException("Must not instantiate an element of this class");
    }

    public static List<Product> getFromDb(String sql) {
        productList.clear();

        try (Connection conn = DbManager.Connect();
             Statement stmt = Objects.requireNonNull(conn).createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Add product ID and Name to List of Products
            while (rs.next()) {
                int id = rs.getInt(ProductsEntry.COLUMN_PRODUCT_ID);
                String name = rs.getString(ProductsEntry.COLUMN_PRODUCT_NAME);
                double price = rs.getDouble(ProductsEntry.COLUMN_PRICE);
                int stockLevel = rs.getInt(ProductsEntry.COLUMN_STOCK_LEVEL);


                productList.add(new Product(id, name, price, stockLevel));
            }

            return productList;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static List<String> getCategoryList() {
        return categoryList;
    }

    public static List<Integer> getQuantityNumbers() {
        return quantityNumbers;
    }
}
