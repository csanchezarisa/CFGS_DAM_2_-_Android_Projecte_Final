package com.example.buidemsl.ui.clients;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;

public class ClientsListAdapter extends SimpleCursorAdapter {

    private ClientsFragment parentFragment;

    public ClientsListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Fragment parentFragment) {
        super(context, layout, c, from, to, flags);
        this.parentFragment = (ClientsFragment) parentFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Cursor client = (Cursor) getItem(position);

        final String EMAIL = client.getString(client.getColumnIndexOrThrow(BuidemHelper.CLIENT_EMAIL));
        final String PHONE = client.getString(client.getColumnIndexOrThrow(BuidemHelper.CLIENT_TELEFON));

        ImageView btnEmail = (ImageView) view.findViewById(R.id.list_item_client_img_email);
        ImageView btnPhone = (ImageView) view.findViewById(R.id.list_item_client_img_phone);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFragment.mostrarAlertCliente(getItemId(position));
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                parentFragment.mostrarAlertEliminarCliente(getItemId(position));
                return true;
            }
        });

        return view;
    }
}
