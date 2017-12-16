package data;

/**
 * API Contract for the Shop app.
 */
public final class ShopContract {

    /**
     * Constructor to prevent from accidentally instantiating the contract class
     */
    private ShopContract() {
        throw new IllegalStateException("Must not instantiate an element of this class");
    }


    /**
     * Inner class that defines constant values for the Staff database table.
     */
    public static final class StaffEntry {

        public final static String TABLE_NAME = "Staff";

        public final static String COLUMN_USERNAME = "Username";
        public final static String COLUMN_PASSWORD = "Password";
        public final static String COLUMN_FIRST_NAME = "FirstName";
        public final static String COLUMN_LAST_NAME = "LastName";
        public final static String COLUMN_SALARY = "Salary";
        public final static String COLUMN_POSITION = "Position";
    }


    /**
     * Inner class that defines constant values for the Customer database table.
     */
    public static final class CustomerEntry {

        public final static String TABLE_NAME = "Customers";

        public final static String COLUMN_USERNAME = "Username";
        public final static String COLUMN_PASSWORD = "Password";
        public final static String COLUMN_FIRST_NAME = "FirstName";
        public final static String COLUMN_LAST_NAME = "LastName";
        public final static String COLUMN_ADDRESS_LINE_1 = "AddressLine1";
        public final static String COLUMN_ADDRESS_LINE_2 = "AddressLine2";
        public final static String COLUMN_TOWN = "Town";
        public final static String COLUMN_POSTCODE = "Postcode";
    }

    /**
     * Inner class that defines constant values for the Products database table.
     */
    public static final class ProductsEntry {

        public final static String TABLE_NAME = "Products";

        public final static String COLUMN_PRODUCT_ID = "ProductId";
        public final static String COLUMN_PRODUCT_NAME = "ProductName";
        public final static String COLUMN_PRICE = "Price";
        public final static String COLUMN_STOCK_LEVEL = "StockLevel";
        public final static String COLUMN_MEASUREMENT = "Measurement";
        public final static String COLUMN_SIZE = "Size";
    }

    /**
     * Inner class that defines constant values for the Orders database table.
     */
    public static final class OrdersEntry {

        public final static String TABLE_NAME = "Orders";

        public final static String COLUMN_ORDER_ID = "OrderId";
        public final static String COLUMN_ORDER_DATE = "OrderDate";
        public final static String COLUMN_USERNAME = "Username";
        public final static String COLUMN_ORDER_TOTAL = "OrderTotal";
        public final static String COLUMN_STATUS = "Status";
    }

    /**
     * Inner class that defines constant values for the OrderLines database table.
     */
    public static final class OrderLinesEntry {

        public final static String TABLE_NAME = "OrderLines";

        public final static String COLUMN_PRODUCT_ID = "ProductId";
        public final static String COLUMN_QUANTITY = "Quantity";
        public final static String COLUMN_LINE_TOTAL = "LineTotal";
        public final static String COLUMN_ORDER_ID = "OrderId";
    }
}
