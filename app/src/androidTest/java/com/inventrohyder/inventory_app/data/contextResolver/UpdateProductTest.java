package com.inventrohyder.inventory_app.data.contextResolver;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inventrohyder.inventory_app.InputTestUtils;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Products;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Suppliers;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UpdateProductTest extends TestCase {

    private static ContentResolver mContentResolver;
    private static ContentValues mInitialValues;
    private static Uri mToUpdateUri;
    private static long mSupplierId;
    private ContentValues mValues;

    @BeforeClass
    public static void initialSetup() {
        mContentResolver = ApplicationProvider.getApplicationContext().getContentResolver();

        ContentValues values = new ContentValues();

        values.put(Suppliers.COLUMN_NAME, InputTestUtils.Supplier_1.name);
        values.put(Suppliers.COLUMN_SUPPLIER_EMAIL, InputTestUtils.Supplier_1.email);
        values.put(Suppliers.COLUMN_SUPPLIER_PHONE, InputTestUtils.Supplier_1.phone);

        Uri rowUri = mContentResolver.insert(Suppliers.CONTENT_URI, values);
        assertNotNull(
                "There should be a successful insertion of a supplier",
                rowUri
        );
        mSupplierId = ContentUris.parseId(rowUri);

        mInitialValues = new ContentValues();
        mInitialValues.put(Products.COLUMN_NAME, InputTestUtils.Product_1.name);
        mInitialValues.put(Products.COLUMN_PRODUCT_QUANTITY, InputTestUtils.Product_1.quantity);
        mInitialValues.put(Products.COLUMN_PRODUCT_PRICE, InputTestUtils.Product_1.price);
        mInitialValues.put(Products.COLUMN_IS_AVAILABLE, InputTestUtils.Product_1.availability);
        mInitialValues.put(Products.COLUMN_PRODUCT_PICTURE, InputTestUtils.Product_1.picture);
        mInitialValues.put(Products.COLUMN_SUPPLIER_ID, mSupplierId);

        mToUpdateUri = mContentResolver.insert(Products.CONTENT_URI, mInitialValues);
    }

    @Before
    public void setUp() {
        mValues = new ContentValues(mInitialValues);
    }

    @Test
    public void testUpdateProductWithoutName() {
        mValues.remove(Products.COLUMN_NAME);
        assertCorrectUpdate();
    }

    @Test
    public void testUpdateProductWithoutSupplier() {
        mValues.remove(Products.COLUMN_SUPPLIER_ID);
        assertCorrectUpdate();
    }

    @Test
    public void testUpdateProductWithoutQuantity() {
        mValues.remove(Products.COLUMN_PRODUCT_QUANTITY);
        assertCorrectUpdate();
    }

    @Test
    public void testUpdateProductWithoutPrice() {
        mValues.remove(Products.COLUMN_PRODUCT_PRICE);
        assertCorrectUpdate();
    }

    @Test
    public void testUpdateProductWithoutPicture() {
        mValues.remove(Products.COLUMN_PRODUCT_PICTURE);
        assertCorrectUpdate();
    }

    @Test
    public void testUpdateProductWithoutAvailability() {
        mValues.remove(Products.COLUMN_IS_AVAILABLE);
        assertCorrectUpdate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertProductWithInvalidQuantity() {
        mValues.remove(Products.COLUMN_PRODUCT_QUANTITY);
        mValues.put(Products.COLUMN_PRODUCT_QUANTITY, -19);
        mContentResolver.update(mToUpdateUri, mValues, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertProductWithInvalidPrice() {
        mValues.remove(Products.COLUMN_PRODUCT_PRICE);
        mValues.put(Products.COLUMN_PRODUCT_PRICE, -19);
        mContentResolver.update(mToUpdateUri, mValues, null, null);
    }

    @Test
    public void testInsertProduct() {
        assertCorrectUpdate();
    }

    private void assertCorrectUpdate() {
        int updatedCount = mContentResolver.update(mToUpdateUri, mValues, null, null);
        assertTrue(
                "If successful product insertion update should occur",
                updatedCount > 0
        );
    }

}