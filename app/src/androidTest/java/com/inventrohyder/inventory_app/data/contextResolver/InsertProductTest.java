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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class InsertProductTest extends TestCase {

    private static ContentResolver mContentResolver;
    private ContentValues mValues;
    private static long mSupplierId;

    @BeforeClass
    public static void initialSetup() {
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
        mSupplierId = ContentUris.parseId(rowUri);
    }

    @Before
    public void setUp() {
        mValues = new ContentValues();
        mValues.put(Products.COLUMN_NAME, TestUtils.Product.name);
        mValues.put(Products.COLUMN_PRODUCT_QUANTITY, TestUtils.Product.quantity);
        mValues.put(Products.COLUMN_PRODUCT_PRICE, TestUtils.Product.price);
        mValues.put(Products.COLUMN_IS_AVAILABLE, TestUtils.Product.availability);
        mValues.put(Products.COLUMN_PRODUCT_PICTURE, TestUtils.Product.picture);
        mValues.put(Products.COLUMN_SUPPLIER_ID, mSupplierId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertProductWithoutName() {
        mValues.remove(Products.COLUMN_NAME);
        mContentResolver.insert(Products.CONTENT_URI, mValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertProductWithoutSupplier() {
        mValues.remove(Products.COLUMN_SUPPLIER_ID);
        mContentResolver.insert(Products.CONTENT_URI, mValues);
    }

    @Test
    public void testInsertProductWithoutQuantity() {
        mValues.remove(Products.COLUMN_PRODUCT_QUANTITY);
        Uri rowUri = mContentResolver.insert(Products.CONTENT_URI, mValues);

        checkProductInsertionValidity(rowUri);
    }

    @Test
    public void testInsertProductWithoutPrice() {
        mValues.remove(Products.COLUMN_PRODUCT_PRICE);
        Uri rowUri = mContentResolver.insert(Products.CONTENT_URI, mValues);

        checkProductInsertionValidity(rowUri);
    }

    @Test
    public void testInsertProductWithoutPicture() {
        mValues.remove(Products.COLUMN_PRODUCT_PICTURE);
        Uri rowUri = mContentResolver.insert(Products.CONTENT_URI, mValues);

        checkProductInsertionValidity(rowUri);
    }

    @Test
    public void testInsertProductWithoutAvailability() {
        mValues.remove(Products.COLUMN_IS_AVAILABLE);
        Uri rowUri = mContentResolver.insert(Products.CONTENT_URI, mValues);

        checkProductInsertionValidity(rowUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertProductWithInvalidQuantity() {
        mValues.remove(Products.COLUMN_PRODUCT_QUANTITY);
        mValues.put(Products.COLUMN_PRODUCT_QUANTITY, -19);
        mContentResolver.insert(Products.CONTENT_URI, mValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertProductWithInvalidPrice() {
        mValues.remove(Products.COLUMN_PRODUCT_PRICE);
        mValues.put(Products.COLUMN_PRODUCT_PRICE, -19);
        mContentResolver.insert(Products.CONTENT_URI, mValues);
    }

    @Test
    public void testInsertProduct() {
        Uri rowUri = mContentResolver.insert(Products.CONTENT_URI, mValues);

        checkProductInsertionValidity(rowUri);
    }

    private void checkProductInsertionValidity(Uri rowUri) {
        assertNotNull(
                "There should be successful insertion of a product",
                rowUri
        );
        long rowId = ContentUris.parseId(rowUri);
        assertTrue(
                "The product should have a valid id",
                rowId > 0
        );
    }

}