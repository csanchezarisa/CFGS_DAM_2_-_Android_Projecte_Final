package com.example.buidemsl.ui.zonas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.google.android.material.snackbar.Snackbar;

public class ZonasFragment extends Fragment {

    private ListView list;
    private MainDatasource datasource;
    private CursorAdapter adapter;

    private String[] from = new String[]{
            BuidemHelper.ZONA_ID,
            BuidemHelper.ZONA_DESCRIPCIO
    };
    private int[] to = new int[]{
            R.id.list_item_zona_id,
            R.id.list_item_zona_descripcio
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_zonas, container, false);
        datasource = new MainDatasource(getContext());

        // Se busca la lista y se le asigna el adapter. Se crean los listeners
        list = (ListView) root.findViewById(R.id.list_zonas);
        adapter = new SimpleCursorAdapter(getContext(), R.layout.fragment_zonas_list, datasource.getZonas(), from, to, 0);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarAlertZona(id);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarAlertEliminarZona(id);
                return true;
            }
        });

        return root;
    }

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
            }
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.default_alert_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Nothing
            }
        });

        alert.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.default_alert_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mostrarAlertEliminarZona(id);
            }
        });

        alert.show();
    }

    private void mostrarAlertEliminarZona(long id) {
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        alert.setTitle(getString(R.string.default_alert_delete));
        alert.setMessage(getString(R.string.default_alert_delete_confirmation) + " " + getString(R.string.fragment_zonas_alert_edit_title) + " " + id + "?");

        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long status = datasource.deleteZona(id);
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

    private void mostrarSnackbarError(String message) {

        View parentView = getView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(
                parentView,
                Html.fromHtml("<font color=\"#FFFFFF\">" + message + "</font>"),
                Snackbar.LENGTH_LONG
        );

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getContext().getColor(R.color.design_default_color_error));

        snackbar.show();
    }

    private void mostrarSnackbarSuccess(String message) {

        View parentView = getView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(
                parentView,
                Html.fromHtml("<font color=\"#FFFFFF\">" + message + "</font>"),
                Snackbar.LENGTH_LONG
        );

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getContext().getColor(android.R.color.holo_green_dark));

        snackbar.show();
    }

    private void refreshList() {
        adapter.changeCursor(datasource.getZonas());
    }
}