package com.example.buidemsl.ui.zonas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ZonasFragment extends Fragment {

    private MainDatasource datasource;
    private CursorAdapter adapter;

    private TextView listEmptyText;
    private ImageView listEmptyImg;

    private final String[] from = new String[]{
            BuidemHelper.ZONA_ID,
            BuidemHelper.ZONA_DESCRIPCIO
    };
    private final int[] to = new int[]{
            R.id.list_item_zona_id,
            R.id.list_item_zona_descripcio
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_zonas, container, false);
        datasource = new MainDatasource(getContext());

        listEmptyImg = (ImageView) root.findViewById(R.id.img_zonas_empty);
        listEmptyText = (TextView) root.findViewById(R.id.txt_zonas_empty);

        // Se busca la lista y se le asigna el adapter. Se crean los listeners
        ListView list = (ListView) root.findViewById(R.id.list_zonas);
        adapter = new SimpleCursorAdapter(getContext(), R.layout.fragment_zonas_list, datasource.getZonas(), from, to, 0);
        list.setAdapter(adapter);

        mostrarEmptyText();

        list.setOnItemClickListener((parent, view, position, id) -> openMap(id));

        list.setOnItemLongClickListener((parent, view, position, id) -> {
            mostrarAlertZona(id);
            return true;
        });

        // Bot??n para a??adir zonas
        FloatingActionButton bntAddZona = root.findViewById(R.id.btn_add_zona);
        bntAddZona.setOnClickListener(v -> mostrarAlertZona(-1));

        return root;
    }

    /** Revisa si en la lista hay elementos o no.
     * Si hay alg??n elemento oculta el mensaje de
     * lista vac??a, si no hay ning??n elemento lo
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
     * a??adir una zona
     * @param id long con el id a editar. Si se
     *           pasa un n??mero negativo se a??adir??*/
    private void mostrarAlertZona(long id) {
        Cursor zona = datasource.getZona(id);

        String alertTitle;
        String descriptionContent = "";

        if (zona.moveToFirst()) {
            alertTitle = getString(R.string.fragment_zonas_alert_edit_title) + " " + id;
            descriptionContent = zona.getString(zona.getColumnIndexOrThrow(BuidemHelper.ZONA_DESCRIPCIO));
        }
        else {
            alertTitle = getString(R.string.fragment_zonas_alert_add_title);
        }

        zona.close();

        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        EditText input = new EditText(getContext());
        input.setText(descriptionContent);
        input.setHint(R.string.fragment_zonas_alert_add_description);

        alert.setTitle(alertTitle);
        alert.setView(input);

        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), (dialog, which) -> {

            long status;

            if (id < 0) {
                status = datasource.insertZona(input.getText().toString());
            }
            else {
                status = datasource.updateZona(id, input.getText().toString());
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
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.default_alert_cancel), (dialog, which) -> {
            // Nothing
        });

        if (id >= 0)
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.default_alert_delete), (dialog, which) -> mostrarAlertEliminarZona(id));

        alert.show();
    }

    /** Muestra un alert de confirmaci??n antes de eliminar
     * @param id long con el id del elemento a eliminar */
    private void mostrarAlertEliminarZona(long id) {
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        alert.setTitle(getString(R.string.default_alert_delete));
        alert.setMessage(getString(R.string.default_alert_delete_confirmation) + " " + getString(R.string.fragment_zonas_alert_edit_title) + " " + id + "?");

        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), (dialog, which) -> {
            final long status = datasource.deleteZona(id);

            if (status > 0)
                mostrarSnackbarSuccess(getString(R.string.fragment_zonas_snackbar_successfuly_deleted));
            else
                mostrarSnackbarError(getString(R.string.fragment_zonas_snackbar_error_deleting));

            refreshList();
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.default_alert_cancel), (dialog, which) -> {
            // Nothing
        });

        alert.show();
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

    /** Muestra un Snackbar verde con un mensaje de ??xito
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
        adapter.changeCursor(datasource.getZonas());
        mostrarEmptyText();
    }

    /** Navega hasta el fragment del mapa pasando
     * el ID de la zona seleccionada como par??metro
     * @param id long con el ID de la zona a mostrar */
    private void openMap(long id) {
        Bundle bundle = new Bundle();
        bundle.putString("column_name", BuidemHelper.MAQUINA_ZONA);
        bundle.putLong("id", id);
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_zonas_to_mapsFragment, bundle);
    }
}