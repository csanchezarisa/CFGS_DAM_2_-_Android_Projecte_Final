package com.example.buidemsl.ui.tipos;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.google.android.material.snackbar.Snackbar;

public class TiposFragment extends Fragment {

    private ListView list;
    private MainDatasource datasource;
    private TiposListAdapter adapter;

    private TextView listEmptyText;
    private ImageView listEmptyImg;

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

        listEmptyImg = (ImageView) root.findViewById(R.id.img_tipos_empty);
        listEmptyText = (TextView) root.findViewById(R.id.txt_tipos_empty);

        // Se busca la lista y se le asigna el adapter. Se crean los listeners
        list = (ListView) root.findViewById(R.id.list_tipos);
        adapter = new TiposListAdapter(getContext(), R.layout.fragment_tipos_list, datasource.getTipos(), from, to, 0, this);
        list.setAdapter(adapter);

        mostrarEmptyText();

        return root;
    }

    /** Revisa si en la lista hay elementos o no.
     * Si hay algún elemento oculta el mensaje de
     * lista vacía, si no hay ningún elemento lo
     * hace visible */
    private void mostrarEmptyText() {
        if (adapter.getCount() > 0) {
            listEmptyText.setVisibility(View.GONE);
            listEmptyImg.setVisibility(View.GONE);
        }
        else {
            listEmptyImg.setVisibility(View.VISIBLE);
            listEmptyText.setVisibility(View.VISIBLE);
        }
    }

    public void mostrarAlertSeleccionarColor(long id, int selectedColor) {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.createColorPickerDialog(getContext());

        colorPickerDialog.setLastColor(selectedColor);
        colorPickerDialog.setInitialColor(selectedColor);

        colorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String hexVal) {
                int rowsAfected = datasource.updateTipo(id, null, hexVal);

                if (rowsAfected > 0) {

                }
                else {

                }

                refreshList();
            }
        });

        colorPickerDialog.show();
    }

    /** Muestra un Snackbar rojo con el mensaje de error personalizado
     * @param message Strnig con el mensaje de error */
    private void mostrarSnackbarError(String message) {

        View parentView = getView();
        Snackbar snackbar = Snackbar.make(
                parentView,
                Html.fromHtml("<font color=\"#FFFFFF\">" + message + "</font>"),
                Snackbar.LENGTH_LONG
        );

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getContext().getColor(R.color.design_default_color_error));

        snackbar.show();
    }

    /** Muestra un Snackbar verde con un mensaje de éxito
     * @param message String con el contenido del mensaje a mostrar */
    private void mostrarSnackbarSuccess(String message) {

        View parentView = getView();
        Snackbar snackbar = Snackbar.make(
                parentView,
                Html.fromHtml("<font color=\"#FFFFFF\">" + message + "</font>"),
                Snackbar.LENGTH_LONG
        );

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getContext().getColor(android.R.color.holo_green_dark));

        snackbar.show();
    }

    /** Actualiza el contenidoq que se muestra en la lista */
    private void refreshList() {
        adapter.changeCursor(datasource.getTipos());
        mostrarEmptyText();
    }
}