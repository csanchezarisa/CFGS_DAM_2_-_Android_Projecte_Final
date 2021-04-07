package com.example.buidemsl.ui.tipos;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;

public class TiposFragment extends Fragment {

    private ListView list;
    private MainDatasource datasource;
    private TiposListAdapter adapter;

    private String[] from = new String[]{
            BuidemHelper.TIPUS_ID,
            BuidemHelper.TIPUS_DESCRIPCIO
    };
    private int[] to = new int[]{
            R.id.list_item_tipo_id,
            R.id.list_item_tipo_descripcio
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tipos, container, false);
        datasource = new MainDatasource(getContext());

        // Se busca la lista y se le asigna el adapter. Se crean los listeners
        list = (ListView) root.findViewById(R.id.list_tipos);
        adapter = new TiposListAdapter(getContext(), R.layout.fragment_tipos_list, datasource.getTipos(), from, to, 0, this);
        list.setAdapter(adapter);

        mostrarAlertSeleccionarColor(null);

        return root;
    }

    private void mostrarAlertSeleccionarColor(@Nullable String selectedColor) {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.createColorPickerDialog(getContext());

        if (selectedColor != null) {
            colorPickerDialog.setLastColor(selectedColor);
            colorPickerDialog.setInitialColor(Color.parseColor(selectedColor));
        }

        colorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String hexVal) {
                Toast.makeText(getContext(), hexVal, Toast.LENGTH_LONG).show();
            }
        });

        colorPickerDialog.show();
    }
}