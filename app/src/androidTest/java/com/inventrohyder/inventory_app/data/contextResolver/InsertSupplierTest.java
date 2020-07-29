package com.inventrohyder.inventory_app.data.contextResolver;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inventrohyder.inventory_app.InputTestUtils;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Suppliers;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class InsertSupplierTest extends TestCase {

    private static ContentResolver mContentResolver;
    private ContentValues mValues;

    @BeforeClass
    public static void initialSetup() {
        mContentResolver = ApplicationProvider.getApplicationContext().getContentResolver();
    }

    @Before
    public void setUp() {
        mValues = new ContentValues();

        mValues.put(Suppliers.COLUMN_NAME, InputTestUtils.Supplier_1.name);
        mValues.put(Suppliers.COLUMN_SUPPLIER_EMAIL, InputTestUtils.Supplier_1.email);
        mValues.put(Suppliers.COLUMN_SUPPLIER_PHONE, InputTestUtils.Supplier_1.phone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertSupplierWithoutCommunication() {
        mValues.remove(Suppliers.COLUMN_SUPPLIER_EMAIL);
        mValues.remove(Suppliers.COLUMN_SUPPLIER_PHONE);
        mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
    }

    @Test
    public void testInsertSupplierWithoutEmail() {
        mValues.remove(Suppliers.COLUMN_SUPPLIER_EMAIL);
        Uri rowUri = mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
        asserSuccessfulInsertion(rowUri);
    }

    @Test
    public void testInsertSupplierWithoutPhone() {
        mValues.remove(Suppliers.COLUMN_SUPPLIER_PHONE);
        Uri rowUri = mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
        asserSuccessfulInsertion(rowUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertSupplierWithoutName() {
        mValues.remove(Suppliers.COLUMN_NAME);
        mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
    }

    @Test
    public void testInsertSupplier() {
        Uri rowUri = mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
        asserSuccessfulInsertion(rowUri);
    }

    private void asserSuccessfulInsertion(Uri rowUri) {
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