package com.example.buidemsl.ui.clients;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.google.android.material.snackbar.Snackbar;

public class ClientsFragment extends Fragment {

    private ListView list;
    private MainDatasource datasource;
    private ClientsListAdapter adapter;

    private TextView listEmptyText;
    private ImageView listEmptyImg;

    private String[] from = new String[]{
            BuidemHelper.CLIENT_NOM,
            BuidemHelper.CLIENT_COGNOMS,
            BuidemHelper.CLIENT_EMAIL,
            BuidemHelper.CLIENT_TELEFON
    };

    private int[] to = new int[]{
            R.id.list_item_client_name,
            R.id.list_item_client_surname,
            R.id.list_item_client_email,
            R.id.list_item_client_phone,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_clients, container, false);

        datasource = new MainDatasource(getContext());

        listEmptyText = (TextView) root.findViewById(R.id.txt_clients_empty);
        listEmptyImg = (ImageView) root.findViewById(R.id.img_clients_empty);

        list = (ListView) root.findViewById(R.id.list_clients);
        adapter = new ClientsListAdapter(getContext(), R.layout.fragment_clients_list, datasource.getClientes(), from, to, 0, this);
        list.setAdapter(adapter);

        mostrarEmptyText();

        return root;
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

    /** Muestra un alert de confirmación antes de eliminar
     * @param id long con el id del elemento a eliminar */
    public void mostrarAlertEliminarCliente(long id) {
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        alert.setTitle(getString(R.string.default_alert_delete));
        alert.setMessage(getString(R.string.default_alert_delete_confirmation) + " " + getString(R.string.fragment_clients_alert_edit_title) + " " + id + "?");

        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long status = datasource.deleteCliente(id);
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
        adapter.changeCursor(datasource.getClientes());
        mostrarEmptyText();
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
}