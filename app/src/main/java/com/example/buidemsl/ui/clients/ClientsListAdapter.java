package com.example.buidemsl.ui.clients;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;

public class ClientsListAdapter extends SimpleCursorAdapter {

    private final ClientsFragment parentFragment;

    public ClientsListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Fragment parentFragment) {
        super(context, layout, c, from, to, flags);
        this.parentFragment = (ClientsFragment) parentFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        // Se recupera el cliente
        Cursor client = (Cursor) getItem(position);

        // Se extrae el Email y el Telefono
        final String EMAIL = client.getString(client.getColumnIndexOrThrow(BuidemHelper.CLIENT_EMAIL));
        final String PHONE = client.getString(client.getColumnIndexOrThrow(BuidemHelper.CLIENT_TELEFON));

        // Se busca el botón para enviar el email y el de la llamada
        ImageView btnEmail = (ImageView) view.findViewById(R.id.list_item_client_img_email);
        ImageView btnPhone = (ImageView) view.findViewById(R.id.list_item_client_img_phone);

        // Si hay un email configurado, se activa el botón par enviar el email.
        // Sino, se hace invisible
        if (EMAIL.length() > 0) {
            btnEmail.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, EMAIL);
                parentFragment.startActivity(intent);
            });
        }
        else {
            btnEmail.setVisibility(View.GONE);
        }

        // Si hay un teléfono configurado se activa el botón para marcarlo
        // sino, se hace invisible
        if (PHONE.length() > 0) {
            btnPhone.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + PHONE));
                parentFragment.startActivity(intent);
            });
        }
        else {
            btnPhone.setVisibility(View.GONE);
        }

        view.setOnClickListener(v -> parentFragment.mostrarAlertCliente(getItemId(position)));

        view.setOnLongClickListener(v -> {
            parentFragment.mostrarAlertEliminarCliente(getItemId(position));
            return true;
        });

        return view;
    }
}
