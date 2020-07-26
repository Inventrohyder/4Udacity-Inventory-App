package com.inventrohyder.inventory_app.data.contextResolver;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inventrohyder.inventory_app.data.InventoryProviderContract.Suppliers;
import com.inventrohyder.inventory_app.data.TestUtils;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class InsertSupplierTest extends TestCase {

    private ContentResolver mContentResolver;
    private ContentValues mValues;

    @Before
    public void setUp() {
        mContentResolver = ApplicationProvider.getApplicationContext().getContentResolver();
        mValues = new ContentValues();

        mValues.put(Suppliers.COLUMN_NAME, TestUtils.Supplier.name);
        mValues.put(Suppliers.COLUMN_SUPPLIER_EMAIL, TestUtils.Supplier.email);
        mValues.put(Suppliers.COLUMN_SUPPLIER_PHONE, TestUtils.Supplier.phone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertSupplierWithoutCommunication() {
        mValues.remove(Suppliers.COLUMN_SUPPLIER_EMAIL);
        mValues.remove(Suppliers.COLUMN_SUPPLIER_PHONE);
        mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertSupplierWithoutEmail() {
        mValues.remove(Suppliers.COLUMN_SUPPLIER_EMAIL);
        mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertSupplierWithoutPhone() {
        mValues.remove(Suppliers.COLUMN_SUPPLIER_PHONE);
        mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertSupplierWithoutName() {
        mValues.remove(Suppliers.COLUMN_NAME);
        mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
    }

    @Test
    public void testInsertSupplier() {
        Uri rowUri = mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
        assertNotNull(
                "There should be a successful insertion of a supplier",
                rowUri
        );
        long rowId = ContentUris.parseId(rowUri);
        assertTrue(
                "There supplier's id should be valid",
                rowId > 0
        );
    }

}