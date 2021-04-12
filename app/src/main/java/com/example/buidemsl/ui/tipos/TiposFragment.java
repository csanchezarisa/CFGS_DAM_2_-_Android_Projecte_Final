package com.example.buidemsl.ui.tipos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        FloatingActionButton btnAdd = (FloatingActionButton) root.findViewById(R.id.btn_add_tipo);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlertTipo(-1);
            }
        });

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

    /** Muestra un alert que permite editar o
     * añadir un tipo
     * @param id long con el id a editar. Si se
     *           pasa un número negativo se añadirá*/
    public void mostrarAlertTipo(long id) {
        Cursor tipo = datasource.getTipo(id);

        String alertTitle;
        String descriptionContent = "";

        if (tipo.moveToFirst()) {
            alertTitle = getString(R.string.fragment_zonas_alert_edit_title) + " " + id;
            descriptionContent = tipo.getString(tipo.getColumnIndexOrThrow(BuidemHelper.ZONA_DESCRIPCIO));
        }
        else {
            alertTitle = getString(R.string.fragment_zonas_alert_add_title);
        }

        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        EditText input = new EditText(getContext());
        input.setText(descriptionContent);
        input.setHint(R.string.fragment_zonas_alert_add_description);

        alert.setTitle(alertTitle);
        alert.setView(input);

        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                long status;

                if (id < 0) {
                    status = datasource.insertTipo(input.getText().toString(), null);
                }
                else {
                    status = datasource.updateTipo(id, input.getText().toString(), null);
                }

                if (status > 0 && id >= 0)
                    mostrarSnackbarSuccess(getString(R.string.fragment_zonas_snackbar_successfuly_updated));
                else if (status > 0)
                    mostrarSnackbarSuccess(getString(R.string.fragment_zonas_snackbar_successfuly_inserted));
                else if (id >= 0)
                    mostrarSnackbarError(getString(R.string.fragment_zonas_snackbar_error_updating));
                else
                    mostrarSnackbarError(getString(R.string.fragment_zonas_snackbar_error_inserting));

                refreshList();
            }
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.default_alert_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Nothing
            }
        });

        if (id >= 0)
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.default_alert_delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mostrarAlertEliminarTipo(id);
                }
            });

        alert.show();
    }

    /** Muestra un alert de confirmación antes de eliminar
     * @param id long con el id del elemento a eliminar */
    private void mostrarAlertEliminarTipo(long id) {
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        alert.setTitle(getString(R.string.default_alert_delete));
        alert.setMessage(getString(R.string.default_alert_delete_confirmation) + " " + getString(R.string.fragment_zonas_alert_edit_title) + " " + id + "?");

        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long status = datasource.deleteTipo(id);
                refreshList();
            }
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.default_alert_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Nothing
            }
        });

        alert.show();
    }

    /** Muestra un alert que permite seleccionar el color
     * de manera gráfica.
     * @param id long con el id del tipo a editar
     * @param selectedColor int con el color seleccionado
     *                      anteriormente en el tipo */
    public void mostrarAlertSeleccionarColor(long id, int selectedColor) {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.createColorPickerDialog(getContext());

        colorPickerDialog.setLastColor(selectedColor);
        colorPickerDialog.setInitialColor(selectedColor);

        colorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String hexVal) {
                int rowsAfected = datasource.updateTipo(id, null, hexVal);

                if (rowsAfected > 0)
                    mostrarSnackbarSuccess(getString(R.string.fragment_tipos_snackbar_color_updated_successfuly));
                else
                    mostrarSnackbarError(getString(R.string.fragment_tipos_snackbar_color_updated_error));

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

    /** Navega hasta el fragment del mapa pasando
     * el ID del tipo seleccionada como parámetro
     * @param id long con el ID del tipo a mostrar */
    public void openMap(long id) {
        Bundle bundle = new Bundle();
        bundle.putString("column_name", BuidemHelper.TABLE_TIPUS);
        bundle.putLong("id", id);
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_tipos_to_mapsFragment, bundle);
    }
}