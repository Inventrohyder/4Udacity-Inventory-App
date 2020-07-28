package com.inventrohyder.inventory_app.ui.products;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.inventrohyder.inventory_app.R;
import com.inventrohyder.inventory_app.data.InventoryProviderContract;

public class ProductsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_PRODUCTS = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize the loader
        LoaderManager.getInstance(this).initLoader(LOADER_PRODUCTS, null, this);

        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LOADER_PRODUCTS) {
            String[] projection = {
                    InventoryProviderContract.Products._ID,
                    InventoryProviderContract.Products.COLUMN_NAME
            };

            String orderBy = InventoryProviderContract.Products.COLUMN_NAME;

            return new CursorLoader(requireContext(), InventoryProviderContract.Products.CONTENT_URI,
                    projection, null, null, orderBy);
        }
        throw new IllegalArgumentException("The loader id is unknown");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}