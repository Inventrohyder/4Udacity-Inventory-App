package com.inventrohyder.inventory_app.data.contextResolver;

import android.content.ContentResolver;
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
public class UpdateSupplierTest extends TestCase {

    private static ContentResolver mContentResolver;
    private static ContentValues mInitialValues;
    private ContentValues mValues;
    private static Uri mToUpdateUri;

    @BeforeClass
    public static void initialSetup() {
        mContentResolver = ApplicationProvider.getApplicationContext().getContentResolver();

        mInitialValues = new ContentValues();

        mInitialValues.put(Suppliers.COLUMN_NAME, InputTestUtils.Supplier_1.name);
        mInitialValues.put(Suppliers.COLUMN_SUPPLIER_EMAIL, InputTestUtils.Supplier_1.email);
        mInitialValues.put(Suppliers.COLUMN_SUPPLIER_PHONE, InputTestUtils.Supplier_1.phone);

        mToUpdateUri = mContentResolver.insert(Suppliers.CONTENT_URI, mInitialValues);
    }


    @Before
    public void setUp() {
        mValues = new ContentValues(mInitialValues);
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