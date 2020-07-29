package com.inventrohyder.inventory_app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.inventrohyder.inventory_app.data.InventoryDatabaseContract.ProductEntry;
import com.inventrohyder.inventory_app.data.InventoryDatabaseContract.SupplierEntry;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Products;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Suppliers;

import java.util.Objects;

public class InventoryContentProvider extends ContentProvider {

    /**
     * URI matcher codes
     */
    public static final int SUPPLIERS = 0;
    public static final int SUPPLIERS_ID = 1;
    public static final int PRODUCTS = 2;
    public static final int PRODUCTS_ID = 3;
    /**
     * Logging TAG
     */
    static final String TAG = InventoryContentProvider.class.getSimpleName();
    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
      Static initializer. This is run the first time anything is called from this class.

      The calls to addURI() go here, for all of the content URI patterns that the provider
      should recognize. All paths added to the UriMatcher have a corresponding code to return
      when a match is found.
     */
    static {

        /*
          The content URI of the form "content://com.inventrohyder.inventory_app.provider/suppliers"
          will map to the integer code {@link #SUPPLIERS}. This URI is used to provide access to MULTIPLE rows
          of the suppliers table.
         */
        sUriMatcher.addURI(InventoryProviderContract.CONTENT_AUTHORITY, Suppliers.PATH, SUPPLIERS);

        // The content URI of the form "content://com.inventrohyder.inventory_app.provider/suppliers/#"
        // will map to the integer code {@link #SUPPLIERS_ID}. This URI is used to provide access to ONE single row
        // of the suppliers table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.inventrohyder.inventory_app.provider/suppliers/3" matches, but
        // "content://com.inventrohyder.inventory_app.provider/suppliers" (without a number at the end)
        // doesn't match.
        sUriMatcher.addURI(InventoryProviderContract.CONTENT_AUTHORITY, Suppliers.PATH + "/#", SUPPLIERS_ID);

        // The content URI of the form "content://com.inventrohyder.inventory_app.provider/products"
        // will map to the integer code {@link #PRODUCTS}. This URI is used to provide access to MULTIPLE rows
        // of the products table.
        sUriMatcher.addURI(InventoryProviderContract.CONTENT_AUTHORITY, Products.PATH, PRODUCTS);

        // The content URI of the form "content://com.inventrohyder.inventory_app.provider/products/#"
        // will map to the integer code {@link #PRODUCTS_ID}. This URI is used to provide access to ONE single row
        // of the products table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.inventrohyder.inventory_app.provider/products/3" matches, but
        // "content://com.inventrohyder.inventory_app.provider/products" (without a number at the end)
        // doesn't match.
        sUriMatcher.addURI(InventoryProviderContract.CONTENT_AUTHORITY, Products.PATH + "/#", PRODUCTS_ID);
    }

    /**
     * Database Helper object
     */
    private InventoryDbHelper mDbHelper;

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long rowId;

        int deletedRows;

        switch (sUriMatcher.match(uri)) {
            case SUPPLIERS:
                deletedRows = db.delete(SupplierEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCTS:
                deletedRows = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SUPPLIERS_ID:
                rowId = ContentUris.parseId(uri);
                selection = SupplierEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(rowId)};
                deletedRows = db.delete(SupplierEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCTS_ID:
                rowId = ContentUris.parseId(uri);
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(rowId)};
                deletedRows = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case UriMatcher.NO_MATCH:
            default:
                throw new IllegalArgumentException("Cannot delete unknown URI " + uri);
        }

        if (deletedRows > 0) {
            // Notify all listeners that the data has changed for the inventory content URI
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return deletedRows;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SUPPLIERS:
                return Suppliers.CONTENT_LIST_TYPE;
            case SUPPLIERS_ID:
                return Suppliers.CONTENT_ITEM_TYPE;
            case PRODUCTS:
                return Products.CONTENT_LIST_TYPE;
            case PRODUCTS_ID:
                return Products.CONTENT_ITEM_TYPE;
            case UriMatcher.NO_MATCH:
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        switch (sUriMatcher.match(uri)) {
            case SUPPLIERS:
                return insertSupplier(values, uri);
            case PRODUCTS:
                return insertProduct(values, uri);
            case SUPPLIERS_ID:
            case PRODUCTS_ID:
                throw new IllegalArgumentException("Cannot insert into the item URI: " + uri);
            case UriMatcher.NO_MATCH:
            default:
                throw new IllegalArgumentException("Cannot insert unknown URI " + uri);
        }
    }

    private Uri insertSupplier(ContentValues values, Uri uri) {

        ensureValidSupplierName(values);
        ensureValidSupplierInsertCommunication(values);

        long rowId;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        rowId = db.insert(SupplierEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (rowId == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the inventory content URI
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(Suppliers.CONTENT_URI, rowId);
    }

    private void ensureValidSupplierInsertCommunication(ContentValues values) {
        // Check that a mode of communication is added
        String phoneNumber = values.getAsString(SupplierEntry.COLUMN_TEL_NUMBER);
        String email = values.getAsString(SupplierEntry.COLUMN_EMAIL);
        if (
                (phoneNumber == null || phoneNumber.isEmpty())
                        && (email == null || email.isEmpty())
        ) {
            throw new IllegalArgumentException("Supplier requires at least one medium of communication");
        }
    }

    private void ensureValidSupplierName(ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(SupplierEntry.COLUMN_NAME);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Supplier requires a name");
        }
    }

    private Uri insertProduct(ContentValues values, Uri uri) {
        ensureValidProductName(values);
        ensureValidProductSupplierId(values);
        ensureValidProductQuantity(values);
        ensureValidProductPrice(values);
        ensureValidProductAvailability(values);

        long rowId;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        rowId = db.insert(ProductEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (rowId == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the inventory content URI
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(Products.CONTENT_URI, rowId);
    }

    private void ensureValidProductAvailability(ContentValues values) {
        // Ensure valid availability
        String availability = values.getAsString(ProductEntry.COLUMN_IS_AVAILABLE);
        if (availability != null
                && (
                Integer.parseInt(availability) != ProductEntry.AVAILABLE
                        && Integer.parseInt(availability) != ProductEntry.NOT_AVAILABLE)) {
            throw new IllegalArgumentException("Product availability is invalid");
        }
    }

    private void ensureValidProductPrice(ContentValues values) {
        // Ensure valid price
        String price = values.getAsString(ProductEntry.COLUMN_PRICE);
        if (price != null && Float.parseFloat(price) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
    }

    private void ensureValidProductQuantity(ContentValues values) {
        // Ensure valid quantity count
        String quantity = values.getAsString(ProductEntry.COLUMN_QUANTITY);
        if (quantity != null && Integer.parseInt(quantity) < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }
    }

    private void ensureValidProductSupplierId(ContentValues values) {
        // Check the supplier id
        String supplierId = values.getAsString(ProductEntry.COLUMN_SUPPLIER_ID);
        if (supplierId == null || !(Integer.parseInt(supplierId) > 0)) {
            throw new IllegalArgumentException("Product requires a valid supplier");
        }
    }

    private void ensureValidProductName(ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(ProductEntry.COLUMN_NAME);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product requires a name");
        }
    }

    /**
     * Initialize the provider and the database open helper
     *
     * @return true always
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        long rowId;
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case SUPPLIERS:
                cursor = db.query(SupplierEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCTS:
                cursor = db.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SUPPLIERS_ID:
                rowId = ContentUris.parseId(uri);
                selection = SupplierEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(rowId)};
                cursor = db.query(SupplierEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCTS_ID:
                rowId = ContentUris.parseId(uri);
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(rowId)};
                cursor = db.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case UriMatcher.NO_MATCH:
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        long rowId;

        switch (sUriMatcher.match(uri)) {
            case SUPPLIERS:
                return updateSupplier(uri, values, selection, selectionArgs);
            case PRODUCTS:
                return updateProduct(uri, values, selection, selectionArgs);
            case SUPPLIERS_ID:
                rowId = ContentUris.parseId(uri);
                selection = SupplierEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(rowId)};
                return updateSupplier(uri, values, selection, selectionArgs);
            case PRODUCTS_ID:
                rowId = ContentUris.parseId(uri);
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(rowId)};
                return updateProduct(uri, values, selection, selectionArgs);
            case UriMatcher.NO_MATCH:
            default:
                throw new IllegalArgumentException("Cannot update unknown URI " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // If the {@link Products#COLUMN_NAME} key is present,
        // Check that the name is valid
        if (values.containsKey(Products.COLUMN_NAME)) {
            ensureValidProductName(values);
        }

        // If the {@link Products#COLUMN_SUPPLIER_ID} key is present,
        // Check that the supplier Id is valid
        if (values.containsKey(Products.COLUMN_SUPPLIER_ID)) {
            ensureValidProductSupplierId(values);
        }

        // If the {@link Products#COLUMN_PRODUCT_QUANTITY} key is present,
        // Check that the quantity is valid
        if (values.containsKey(Products.COLUMN_PRODUCT_QUANTITY)) {
            ensureValidProductQuantity(values);
        }

        // If the {@link Products#COLUMN_PRODUCT_PRICE} key is present,
        // Check that the price is valid
        if (values.containsKey(Products.COLUMN_PRODUCT_PRICE)) {
            ensureValidProductPrice(values);
        }

        // If the {@link Products#COLUMN_IS_AVAILABLE} key is present,
        // Check that the availability is valid
        if (values.containsKey(Products.COLUMN_IS_AVAILABLE)) {
            ensureValidProductAvailability(values);
        }

        int updatedRows = db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

        if (updatedRows > 0) {
            // Notify all listeners that the data has changed for the inventory content URI
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;
    }

    private int updateSupplier(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // If the {@link Suppliers#COLUMN_NAME} key is present,
        // Check that the name is not null
        if (values.containsKey(Suppliers.COLUMN_NAME)) {
            ensureValidSupplierName(values);
        }

        // If the {@link Suppliers#COLUMN_SUPPLIER_EMAIL} key
        // or {@link Suppliers#COLUMN_SUPPLIER_PHONE} is present,
        // Check that the communication is valid
        if (values.containsKey(Suppliers.COLUMN_SUPPLIER_EMAIL)
                || values.containsKey(Suppliers.COLUMN_SUPPLIER_PHONE)) {
            ensureValidSupplierUpdateCommunication(values);
        }

        int updatedRows = db.update(SupplierEntry.TABLE_NAME, values, selection, selectionArgs);

        if (updatedRows > 0) {
            // Notify all listeners that the data has changed for the inventory content URI
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;
    }

    private void ensureValidSupplierUpdateCommunication(ContentValues values) {
        // Check that a mode of communication is added
        String phoneNumber = values.getAsString(SupplierEntry.COLUMN_TEL_NUMBER);
        String email = values.getAsString(SupplierEntry.COLUMN_EMAIL);
        if (
                (phoneNumber == null || phoneNumber.isEmpty())
                        || (email == null || email.isEmpty())
        ) {
            throw new IllegalArgumentException("Supplier requires at least one medium of communication");
        }
    }
}
