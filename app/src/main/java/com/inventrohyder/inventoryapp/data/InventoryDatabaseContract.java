package com.inventrohyder.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * API contract for the inventory app
 */
class InventoryDatabaseContract {


    public static final String CREATE_TABLE = "CREATE TABLE ";

    // MODIFIERS
    public static final String PRIMARY_KEY = " PRIMARY KEY ";
    public static final String AUTOINCREMENT = " AUTOINCREMENT ";
    public static final String NOT_NULL = " NOT NULL ";
    public static final String DEFAULT = " DEFAULT ";

    public static final String FOREIGN_KEY = "FOREIGN KEY ";
    public static final String REFERENCES = " REFERENCES ";

    // TYPES
    public static final String NUMERIC_TYPE = " NUMERIC ";
    public static final String TEXT_TYPE = " TEXT ";
    public static final String INTEGER_TYPE = " INTEGER ";
    public static final String BLOB_TYPE = " BLOB ";

    // Prevent accidental instantiation of the contract class
    public InventoryDatabaseContract() {
    }


    /**
     * Inner class that defines the constant values for the suppliers database table.
     * Each entry in the table represents a single supplier.
     */
    static final class SupplierEntry implements BaseColumns {

        /**
         * Name of the database table
         */
        static final String TABLE_NAME = "tb_suppliers";


        /**
         * Name of the supplier.
         * <p>
         * Type: TEXT
         * </p>
         */
        static final String COLUMN_NAME = "name";
        /**
         * Phone number of the supplier.
         * <p>
         * Type: TEXT
         * </p>
         */
        static final String COLUMN_TEL_NUMBER = "tel_number";
        /**
         * Email address of the supplier.
         * <p>
         * Type: TEXT
         * </p>
         */
        static final String COLUMN_EMAIL = "email_address";

        /**
         * Table creation string for {@link SupplierEntry}
         * <p>
         * CREATE TABLE tb_suppliers (
         * _id INTEGER PRIMARY KEY AUTOINCREMENT,
         * name TEXT NOT NULL,
         * tel_number TEXT,
         * email_address TEXT
         * )
         * </p>
         */
        static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + "("
                + _ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + ","
                + COLUMN_NAME + TEXT_TYPE + NOT_NULL + ","
                + COLUMN_TEL_NUMBER + TEXT_TYPE + ","
                + COLUMN_EMAIL + TEXT_TYPE
                + ")";

        /**
         * Get the qualified name of the given column
         *
         * @param columnName The column to qualify
         * @return The qualified column name
         */
        static String getQName(String columnName) {
            return TABLE_NAME + "." + columnName;
        }
    }

    /**
     * Inner class that defines the constant values for the products database table.
     * Each entry in the table represents a single product.
     */
    static final class ProductEntry implements BaseColumns {

        /**
         * Name of the database table
         */
        static final String TABLE_NAME = "tb_products";

        /**
         * Name of the product
         * <p>
         * Type: TEXT
         * </p>
         */
        static final String COLUMN_NAME = "name";

        /**
         * Supplier ID foreign key
         * <p>
         * Type: INTEGER
         * </p>
         */
        static final String COLUMN_SUPPLIER_ID = "supplier_id";

        /**
         * The quantity of the product available
         * <p>
         * Type: INTEGER
         * </p>
         */
        static final String COLUMN_QUANTITY = "quantity";


        /**
         * The price of a given product
         * <p>
         * Type: NUMERIC
         * </p>
         */
        static final String COLUMN_PRICE = "price";

        /**
         * The picture of a given product
         * <p>
         * Type: BLOB
         * </p>
         */
        static final String COLUMN_PICTURE = "picture";

        /**
         * Is the product currently available
         * <p>
         * Type: INTEGER
         * </p>
         */
        static final String COLUMN_IS_AVAILABLE = "is_available";

        /**
         * Possible values for availability
         */
        static final int AVAILABLE = 1;
        static final int NOT_AVAILABLE = 0;
        /**
         * Table creation string for {@link ProductEntry}
         * <p>
         * CREATE TABLE tb_products (
         * _id INTEGER PRIMARY KEY AUTOINCREMENT,
         * name TEXT NOT NULL,
         * supplier_id INTEGER NOT NULL,
         * quantity INTEGER NOT NULL DEFAULT 0,
         * price NUMERIC NOT NULL DEFAULT 0,
         * picture BLOB,
         * is_available INTEGER,
         * FOREIGN KEY(supplier_id) REFERENCES tb_suppliers(_id)
         * )
         * </p>
         */
        static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + "("
                + _ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + ","
                + COLUMN_NAME + TEXT_TYPE + NOT_NULL + ","
                + COLUMN_SUPPLIER_ID + INTEGER_TYPE + NOT_NULL + ","
                + COLUMN_QUANTITY + INTEGER_TYPE + NOT_NULL + DEFAULT + "0" + ","
                + COLUMN_PRICE + NUMERIC_TYPE + NOT_NULL + DEFAULT + "0" + ","
                + COLUMN_PICTURE + BLOB_TYPE + ","
                + COLUMN_IS_AVAILABLE + INTEGER_TYPE + ","
                + FOREIGN_KEY + "(" + COLUMN_SUPPLIER_ID + ")"
                + REFERENCES + SupplierEntry.TABLE_NAME + "(" + SupplierEntry._ID + ")"
                + ")";

        /**
         * Returns whether or not the given availability is {@link #AVAILABLE},
         * or {@link #NOT_AVAILABLE}.
         */
        static boolean isValidAvailability(int availability) {
            return availability == AVAILABLE || availability == NOT_AVAILABLE;
        }

        /**
         * Get the qualified name of the given column
         *
         * @param columnName The column to qualify
         * @return The qualified column name
         */
        static String getQName(String columnName) {
            return TABLE_NAME + "." + columnName;
        }
    }

}
