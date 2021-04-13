package com.example.buidemsl.ui.home;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.util.Date;

public class MachineListAdapter extends SimpleCursorAdapter {

    private HomeFragment parentFragment;

    public MachineListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Fragment parentFragment) {
        super(context, layout, c, from, to, flags);
        this.parentFragment = (HomeFragment) parentFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Cursor maquina = (Cursor) getItem(position);

        TextView titleText;
        TextView contentText;

        // Se pone el nombre y apellidos del cliente
        contentText = (TextView) view.findViewById(R.id.txt_list_item_maquina_client);
        contentText.setText(maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.CLIENT_NOM)) + " " + maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.CLIENT_COGNOMS)));

        // Se aplica lógica para la parte del teléfono del cliente
        titleText = (TextView) view.findViewById(R.id.txt_list_item_maquina_client_phone_title);
        contentText = (TextView) view.findViewById(R.id.txt_list_item_maquina_client_phone);
        ImageButton btnPhone = (ImageButton) view.findViewById(R.id.btn_list_item_maquina_client_phone);

        // ¿Hay algún teléfono informado?
        if (contentText.getText().toString().length() > 0) {
            btnPhone.setOnClickListener(v -> {
                String phoneNumber = maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.CLIENT_TELEFON));
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                parentFragment.startActivity(intent);
            });
        }
        else {
            titleText.setVisibility(View.GONE);
            contentText.setVisibility(View.GONE);
            btnPhone.setVisibility(View.GONE);
        }

        // Se aplica lógica para la parte del email del cliente
        titleText = (TextView) view.findViewById(R.id.txt_list_item_maquina_client_email_title);
        contentText = (TextView) view.findViewById(R.id.txt_list_item_maquina_client_email);
        ImageButton btnEmail = (ImageButton) view.findViewById(R.id.btn_list_item_maquina_client_email);

        // ¿Hay algún email informado?
        if (contentText.getText().toString().length() > 0) {
            btnEmail.setOnClickListener(V -> {
                final String email = maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.CLIENT_EMAIL));
                final String subject = parentFragment.getString(R.string.fragment_clients_mail_subject) + " " + maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_NUMERO_SERIE));
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                parentFragment.startActivity(intent);
            });
        }
        else {
            titleText.setVisibility(View.GONE);
            contentText.setVisibility(View.GONE);
            btnEmail.setVisibility(View.GONE);
        }

        // Se aplica lógica para la parte de la última fecha de revisión
        titleText = (TextView) view.findViewById(R.id.txt_list_item_maquina_ultima_revisio_title);
        contentText = (TextView) view.findViewById(R.id.txt_list_item_maquina_ultima_revisio);

        // Se intenta recuperar la fecha. Si hay algún error (porque no está informada) se ocultan los TextView
        try {
            Date date = new Date(maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_ULTIMA_REVISIO)), true);
            contentText.setText(date.getEuropeanDate());
        }
        catch (Exception e) {
            titleText.setVisibility(View.GONE);
            contentText.setVisibility(View.GONE);
        }

        view.setOnClickListener(v -> {

        });

        view.setOnLongClickListener(v -> {
            parentFragment.openMachineManagement(getItemId(position));
            return true;
        });

        return view;
    }
}
