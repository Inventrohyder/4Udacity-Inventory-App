package com.inventrohyder.inventory_app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.inventrohyder.inventory_app.data.InventoryDatabaseContract.ProductEntry;
import com.inventrohyder.inventory_app.data.InventoryDatabaseContract.SupplierEntry;

class InventoryDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the Database file
     */
    static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version
     * <p>
     * Increment database version when the schema changes
     * </p>
     */
    static final int DATABASE_VERSION = 1;

    /**
     * Construct a database open helper with the default database name and version
     *
     * @param context The application context
     */
    public InventoryDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create the database tables
        sqLiteDatabase.execSQL(SupplierEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(ProductEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
