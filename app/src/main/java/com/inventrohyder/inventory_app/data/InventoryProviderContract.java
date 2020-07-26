package com.inventrohyder.inventory_app.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryProviderContract {

    public static final String CONTENT_AUTHORITY = "com.inventrohyder.inventory_app.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private InventoryProviderContract() {
    }

    protected interface IdentityColumns {
        String COLUMN_NAME = "name";
    }

    protected interface SupplierColumns {
        String COLUMN_SUPPLIER_EMAIL = "email_address";
        String COLUMN_SUPPLIER_PHONE = "tel_number";
    }

    protected interface ProductColumns {
        String COLUMN_SUPPLIER_ID = "supplier_id";
        String COLUMN_PRODUCT_QUANTITY = "quantity";
        String COLUMN_PRODUCT_PRICE = "price";
        String COLUMN_PRODUCT_PICTURE = "picture";
        String COLUMN_IS_AVAILABLE = "is_available";
    }

    public static final class Suppliers implements BaseColumns, IdentityColumns, SupplierColumns {
        public static final String PATH = "suppliers";

        // content://com.inventrohyder.inventory_app.provider/suppliers
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of suppliers.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single supplier.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
    }

    public static final class Products implements BaseColumns, IdentityColumns, ProductColumns {
        public static final String PATH = "products";

        // content://com.inventrohyder.inventory_app.provider/products
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
    }
}
