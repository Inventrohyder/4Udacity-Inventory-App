package com.inventrohyder.inventory_app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inventrohyder.inventory_app.data.InventoryDatabaseContract.SupplierEntry;
import com.inventrohyder.inventory_app.data.TestUtils.Supplier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class InventoryDbHelperTest {

    private InventoryDbHelper mDbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        mDbHelper = new InventoryDbHelper(context);
    }

    @After
    public void finish() {
        mDbHelper.close();
    }

    @Test
    public void shouldCreateDatabase() {
        String databaseName = mDbHelper.getDatabaseName();
        assertEquals(InventoryDbHelper.DATABASE_NAME, databaseName);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        assertTrue("Getting a writable database should ensure an open connection to the db",
                database.isOpen());
        database.close();

        database = mDbHelper.getReadableDatabase();
        assertTrue("Getting a readable database should ensure an open connection to the db",
                database.isOpen());
        database.close();
    }

    @Test
    public void insertSupplier() {
        ContentValues values = new ContentValues();
        values.put(SupplierEntry.COLUMN_NAME, Supplier.name);
        values.put(SupplierEntry.COLUMN_EMAIL, Supplier.email);
        values.put(SupplierEntry.COLUMN_TEL_NUMBER, Supplier.phone);

        long supplierId = mDbHelper.getWritableDatabase()
                .insert(SupplierEntry.TABLE_NAME, null, values);
        assertTrue("A successful inserts yields an id value greater than 0",
                supplierId != -1);


        Cursor cursor = mDbHelper.getReadableDatabase().query(
                SupplierEntry.TABLE_NAME,
                null,
                SupplierEntry._ID + "=?",
                new String[]{Long.toString(supplierId)},
                null,
                null,
                null);
        assertTrue("The cursor should contain at least one row",
                cursor.moveToFirst());

        int nameColIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_NAME);
        int emailColIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_EMAIL);
        int telColIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_TEL_NUMBER);

        assertEquals("The queried name must match the inserted name",
                Supplier.name, cursor.getString(nameColIndex));
        assertEquals("The queried email must match the inserted email",
                Supplier.email, cursor.getString(emailColIndex));
        assertEquals("The queried phone number must match the inserted phone number",
                Supplier.phone, cursor.getString(telColIndex));
    }
}