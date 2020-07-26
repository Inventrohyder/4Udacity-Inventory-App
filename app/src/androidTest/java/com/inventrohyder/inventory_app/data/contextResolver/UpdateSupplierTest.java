package com.inventrohyder.inventory_app.data.contextResolver;

import android.content.ContentResolver;
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
public class UpdateSupplierTest extends TestCase {

    private ContentResolver mContentResolver;
    private ContentValues mValues;
    private Uri mToUpdateUri;

    @Before
    public void setUp() {
        mContentResolver = ApplicationProvider.getApplicationContext().getContentResolver();
        mValues = new ContentValues();

        mValues.put(Suppliers.COLUMN_NAME, TestUtils.Supplier.name);
        mValues.put(Suppliers.COLUMN_SUPPLIER_EMAIL, TestUtils.Supplier.email);
        mValues.put(Suppliers.COLUMN_SUPPLIER_PHONE, TestUtils.Supplier.phone);

        mToUpdateUri = mContentResolver.insert(Suppliers.CONTENT_URI, mValues);
    }

    @Test
    public void testUpdateSupplierWithoutCommunication() {
        mValues.remove(Suppliers.COLUMN_SUPPLIER_EMAIL);
        mValues.remove(Suppliers.COLUMN_SUPPLIER_PHONE);
        int updatedCount = mContentResolver.update(mToUpdateUri, mValues, null, null);
        assertTrue(
                "If successful supplier insertion update should occur",
                updatedCount > 0
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateSupplierWithoutEmail() {
        mValues.remove(Suppliers.COLUMN_SUPPLIER_EMAIL);
        mContentResolver.update(mToUpdateUri, mValues, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateSupplierWithoutPhone() {
        mValues.remove(Suppliers.COLUMN_SUPPLIER_PHONE);
        mContentResolver.update(mToUpdateUri, mValues, null, null);
    }

    @Test
    public void testUpdateSupplierWithoutName() {
        mValues.remove(Suppliers.COLUMN_NAME);
        int updatedCount = mContentResolver.update(mToUpdateUri, mValues, null, null);
        assertTrue(
                "If successful supplier insertion update should occur",
                updatedCount > 0
        );
    }

    @Test
    public void testUpdateSupplier() {
        int updatedCount = mContentResolver.update(mToUpdateUri, mValues, null, null);
        assertTrue(
                "The inserted supplier should be updated",
                updatedCount > 0
        );
    }

}