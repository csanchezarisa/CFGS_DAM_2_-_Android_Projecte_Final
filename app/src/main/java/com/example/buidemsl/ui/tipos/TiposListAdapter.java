package com.example.buidemsl.ui.tipos;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;

public class TiposListAdapter extends SimpleCursorAdapter {

    private TiposFragment parentFragment;

    public TiposListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Fragment parentFragment) {
        super(context, layout, c, from, to, flags);
        this.parentFragment = (TiposFragment) parentFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        // Se recupera el tipo
        Cursor tipo = (Cursor) getItem(position);

        // Se cambia el color del botón por el del tipo
        Button button = (Button) view.findViewById(R.id.btn_list_item_tipo_color);
        int colorTipo = Color.parseColor(tipo.getString(tipo.getColumnIndexOrThrow(BuidemHelper.TIPUS_COLOR)));
        button.setBackgroundColor(colorTipo);

        // Se crea un listener en el botón para abrir el ColorPicker
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFragment.mostrarAlertSeleccionarColor(getItemId(position), colorTipo);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFragment.openMap(getItemId(position));
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                parentFragment.mostrarAlertTipo(getItemId(position));
                return true;
            }
        });

        return view;
    }
}
