package com.inventrohyder.inventoryapp.data;

import com.inventrohyder.inventoryapp.data.InventoryDatabaseContract.ProductEntry;
import com.inventrohyder.inventoryapp.data.InventoryDatabaseContract.SupplierEntry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InventoryDatabaseContractTest {

    @Test
    public void supplierTable_isCorrect() {
        String correct_sql = "CREATE TABLE tb_suppliers("
                + "_id INTEGER  PRIMARY KEY  AUTOINCREMENT ,"
                + "name TEXT  NOT NULL ,"
                + "tel_number TEXT ,"
                + "email_address TEXT "
                + ")";
        assertEquals(correct_sql, SupplierEntry.SQL_CREATE_TABLE);
    }

    @Test
    public void productTable_isCorrect() {
        String correct_sql = "CREATE TABLE tb_products("
                + "_id INTEGER  PRIMARY KEY  AUTOINCREMENT ,"
                + "name TEXT  NOT NULL ,"
                + "supplier_id INTEGER  NOT NULL ,"
                + "quantity INTEGER  NOT NULL  DEFAULT 0,"
                + "price NUMERIC  NOT NULL  DEFAULT 0,"
                + "picture BLOB ,"
                + "is_available INTEGER ,"
                + "FOREIGN KEY (supplier_id) REFERENCES tb_suppliers(_id)"
                + ")";
        assertEquals(correct_sql, ProductEntry.SQL_CREATE_TABLE);
    }

    @Test
    public void qualifiedNames_isCorrect() {
        assertEquals(
                "tb_suppliers.email_address",
                SupplierEntry.getQName(SupplierEntry.COLUMN_EMAIL)
        );
        assertEquals(
                "tb_suppliers.name",
                SupplierEntry.getQName("name")
        );

        assertEquals(
                "tb_products.price",
                ProductEntry.getQName(ProductEntry.COLUMN_PRICE)
        );
        assertEquals(
                "tb_products.name",
                ProductEntry.getQName("name")
        );
    }

    @Test
    public void availability() {
        assertTrue(ProductEntry.isValidAvailability(1));
        assertTrue(ProductEntry.isValidAvailability(0));
        assertFalse(ProductEntry.isValidAvailability(-1));
    }
}