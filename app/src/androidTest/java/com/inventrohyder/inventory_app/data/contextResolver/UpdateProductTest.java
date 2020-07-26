package com.inventrohyder.inventory_app.data.contextResolver;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inventrohyder.inventory_app.data.InventoryProviderContract.Products;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Suppliers;
import com.inventrohyder.inventory_app.data.TestUtils;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UpdateProductTest extends TestCase {

    private ContentResolver mContentResolver;
    private ContentValues mValues;
    private Uri mToUpdateUri;

    @Before
    public void setUp() {
        mContentResolver = ApplicationProvider.getApplicationContext().getContentResolver();

        ContentValues values = new ContentValues();

        values.put(Suppliers.COLUMN_NAME, TestUtils.Supplier.name);
        values.put(Suppliers.COLUMN_SUPPLIER_EMAIL, TestUtils.Supplier.email);
        values.put(Suppliers.COLUMN_SUPPLIER_PHONE, TestUtils.Supplier.phone);

        Uri rowUri = mContentResolver.insert(Suppliers.CONTENT_URI, values);
        assertNotNull(
                "There should be a successful insertion of a supplier",
                rowUri
        );
        long supplierId = ContentUris.parseId(rowUri);

        mValues = new ContentValues();
        mValues.put(Products.COLUMN_NAME, TestUtils.Product.name);
        mValues.put(Products.COLUMN_PRODUCT_QUANTITY, TestUtils.Product.quantity);
        mValues.put(Products.COLUMN_PRODUCT_PRICE, TestUtils.Product.price);
        mValues.put(Products.COLUMN_IS_AVAILABLE, TestUtils.Product.availability);
        mValues.put(Products.COLUMN_PRODUCT_PICTURE, TestUtils.Product.picture);
        mValues.put(Products.COLUMN_SUPPLIER_ID, supplierId);

        mToUpdateUri = mContentResolver.insert(Products.CONTENT_URI, mValues);

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