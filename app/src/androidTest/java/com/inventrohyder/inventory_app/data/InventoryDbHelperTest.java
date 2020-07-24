package com.inventrohyder.inventory_app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inventrohyder.inventory_app.data.InventoryDatabaseContract.SupplierEntry;

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
        assertTrue(database.isOpen());
        database.close();

        database = mDbHelper.getReadableDatabase();
        assertTrue(database.isOpen());
        database.close();
    }

    @Test
    public void insertSupplier() {
        String name = "Mombasa Maize Millers";
        String email = "info@msa.mmm.co.ke";
        String telNumber = "0730 999999";

        ContentValues values = new ContentValues();
        values.put(SupplierEntry.COLUMN_NAME, name);
        values.put(SupplierEntry.COLUMN_EMAIL, email);
        values.put(SupplierEntry.COLUMN_TEL_NUMBER, telNumber);

        long supplierId = mDbHelper.getWritableDatabase()
                .insert(SupplierEntry.TABLE_NAME, null, values);
        assertTrue(supplierId != -1);


        Cursor cursor = mDbHelper.getReadableDatabase().query(
                SupplierEntry.TABLE_NAME,
                null,
                SupplierEntry._ID + "=?",
                new String[]{Long.toString(supplierId)},
                null,
                null,
                null);
        assertTrue(cursor.moveToFirst());

        int nameColIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_NAME);
        int emailColIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_EMAIL);
        int telColIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_TEL_NUMBER);

        assertEquals(name, cursor.getString(nameColIndex));
        assertEquals(email, cursor.getString(emailColIndex));
        assertEquals(telNumber, cursor.getString(telColIndex));
    }
}