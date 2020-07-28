package com.inventrohyder.inventory_app.ui.suppliers;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inventrohyder.inventory_app.R;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Suppliers;

public class SuppliersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_SUPPLIERS = 0;
    public final String TAG = this.getClass().getSimpleName();

    private SuppliersRecyclerAdapter mSuppliersRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LoaderManager.getInstance(this).initLoader(LOADER_SUPPLIERS, null, this);

        mSuppliersRecyclerAdapter = new SuppliersRecyclerAdapter(requireContext(), null);


        View root = inflater.inflate(R.layout.fragment_suppliers, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.suppliers_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        recyclerView.setAdapter(mSuppliersRecyclerAdapter);

        return root;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LOADER_SUPPLIERS) {
            String[] projection = {
                    Suppliers._ID,
                    Suppliers.COLUMN_NAME,
                    Suppliers.COLUMN_SUPPLIER_EMAIL,
                    Suppliers.COLUMN_SUPPLIER_PHONE
            };

            String orderBy = Suppliers.COLUMN_NAME;

            return new CursorLoader(requireContext(), Suppliers.CONTENT_URI,
                    projection, null, null, orderBy);
        }
        Log.e(TAG, "Loader id: " + id);
        throw new IllegalArgumentException("Loader id is unknown");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mSuppliersRecyclerAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mSuppliersRecyclerAdapter.changeCursor(null);
    }
}