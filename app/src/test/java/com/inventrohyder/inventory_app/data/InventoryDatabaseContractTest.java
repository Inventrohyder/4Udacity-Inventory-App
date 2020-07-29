package com.inventrohyder.inventory_app.data;

import com.inventrohyder.inventory_app.data.InventoryDatabaseContract.ProductEntry;
import com.inventrohyder.inventory_app.data.InventoryDatabaseContract.SupplierEntry;

import org.junit.Assert;
import org.junit.Test;

public class InventoryDatabaseContractTest {

    @Test
    public void testSupplierTable() {
        String correct_sql = "CREATE TABLE tb_suppliers("
                + "_id INTEGER  PRIMARY KEY  AUTOINCREMENT ,"
                + "name TEXT  NOT NULL ,"
                + "tel_number TEXT ,"
                + "email_address TEXT "
                + ")";
        Assert.assertEquals("Ensure the SQL create statement is correct",
                correct_sql, SupplierEntry.SQL_CREATE_TABLE);
    }

    @Test
    public void testProductTable() {
        String correct_sql = "CREATE TABLE tb_products("
                + "_id INTEGER  PRIMARY KEY  AUTOINCREMENT ,"
                + "name TEXT  NOT NULL ,"
                + "supplier_id INTEGER  NOT NULL ,"
                + "quantity INTEGER  NOT NULL  DEFAULT 0,"
                + "price NUMERIC  NOT NULL  DEFAULT 0,"
                + "picture BLOB ,"
                + "is_available INTEGER  DEFAULT 0,"
                + "FOREIGN KEY (supplier_id) REFERENCES tb_suppliers(_id)"
                + ")";
        Assert.assertEquals("Ensure the SQL create statement is correct",
                correct_sql, ProductEntry.SQL_CREATE_TABLE);
    }

    @Test
    public void testQualifiedNames() {
        Assert.assertEquals(
                "The qualified name should be in the form tableName.columnName",
                "tb_suppliers.email_address",
                SupplierEntry.getQName(SupplierEntry.COLUMN_EMAIL)
        );
        Assert.assertEquals(
                "The qualified name should be in the form tableName.columnName",
                "tb_suppliers.name",
                SupplierEntry.getQName("name")
        );

        Assert.assertEquals(
                "The qualified name should be in the form tableName.columnName",
                "tb_products.price",
                ProductEntry.getQName(ProductEntry.COLUMN_PRICE)
        );
        Assert.assertEquals(
                "The qualified name should be in the form tableName.columnName",
                "tb_products.name",
                ProductEntry.getQName("name")
        );
    }

    @Test
    public void testAvailability() {
        Assert.assertTrue("1 represents being available",
                ProductEntry.isValidAvailability(ProductEntry.AVAILABLE));
        Assert.assertTrue("0 represents not available",
                ProductEntry.isValidAvailability(ProductEntry.NOT_AVAILABLE));
        Assert.assertFalse("Any other value is not valid",
                ProductEntry.isValidAvailability(-1));
    }
}