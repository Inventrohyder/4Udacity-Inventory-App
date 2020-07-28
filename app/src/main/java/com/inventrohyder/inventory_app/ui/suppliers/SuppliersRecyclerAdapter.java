package com.inventrohyder.inventory_app.ui.suppliers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.inventrohyder.inventory_app.R;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Suppliers;

class SuppliersRecyclerAdapter extends RecyclerView.Adapter<SuppliersRecyclerAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private Cursor mCursor;
    private int mNamePos;
    private int mEmailPos;
    private int mPhonePos;

    public SuppliersRecyclerAdapter(Context context, Cursor c) {
        mCursor = c;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if (mCursor == null)
            return;
        mNamePos = mCursor.getColumnIndex(Suppliers.COLUMN_NAME);
        mEmailPos = mCursor.getColumnIndex(Suppliers.COLUMN_SUPPLIER_EMAIL);
        mPhonePos = mCursor.getColumnIndex(Suppliers.COLUMN_SUPPLIER_PHONE);
    }

    void changeCursor(Cursor c) {
        if (mCursor != null && c != mCursor) {
            mCursor.close();
        }
        mCursor = c;
        populateColumnPositions();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_supplier, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String supplierName = mCursor.getString(mNamePos);
        String supplierEmail = mCursor.getString(mEmailPos);
        String supplierPhone = mCursor.getString(mPhonePos);

        holder.mTextSupplierName.setText(supplierName);
        holder.mEmail = supplierEmail;
        holder.mPhone = supplierPhone;

        if (supplierEmail != null && !supplierEmail.isEmpty() && supplierPhone != null && !supplierPhone.isEmpty()) {
            holder.mOrderButton.setOnClickListener(holder.communicationClickListener);
        } else if (supplierEmail != null && !supplierEmail.isEmpty()) {
            holder.mOrderButton.setOnClickListener(holder.emailClickListener);
        } else if (supplierPhone != null && !supplierPhone.isEmpty()) {
            holder.mOrderButton.setOnClickListener(holder.phoneClickListener);
        }

    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mTextSupplierName;
        final Button mOrderButton;
        String mEmail;
        final View.OnClickListener emailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailIntent();
            }
        };
        String mPhone;
        final View.OnClickListener phoneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsIntent();
            }
        };
        final View.OnClickListener communicationClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, R.string.choose_communication, Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.choose_communication_question)  // TODO: Move to string resources
                        .setPositiveButton(R.string.email, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                emailIntent();
                            }
                        })
                        .setNegativeButton(R.string.sms, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                smsIntent();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextSupplierName = itemView.findViewById(R.id.supplier_name);
            mOrderButton = itemView.findViewById(R.id.orderButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Supplier editor
                    Toast.makeText(mContext, "Open supplier editor", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        private void emailIntent() {
            Toast.makeText(mContext, R.string.send_email, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmail});
            intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.default_email_subject)); // TODO: Move to settings
            intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.default_order_message));
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(intent);
            }
        }

        private void smsIntent() {
            Toast.makeText(mContext, R.string.send_sms, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mPhone, null));
            intent.putExtra("sms_body", mContext.getString(R.string.default_order_message)); // TODO: Move to settings
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(intent);
            }
        }
    }
}
