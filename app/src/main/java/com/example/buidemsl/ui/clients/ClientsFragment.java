package com.example.buidemsl.ui.clients;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ClientsFragment extends Fragment {

    private MainDatasource datasource;
    private ClientsListAdapter adapter;

    private TextView listEmptyText;
    private ImageView listEmptyImg;

    private final String[] from = new String[]{
            BuidemHelper.CLIENT_NOM,
            BuidemHelper.CLIENT_COGNOMS,
            BuidemHelper.CLIENT_EMAIL,
            BuidemHelper.CLIENT_TELEFON
    };

    private final int[] to = new int[]{
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

        ListView list = (ListView) root.findViewById(R.id.list_clients);
        adapter = new ClientsListAdapter(getContext(), R.layout.fragment_clients_list, datasource.getClientes(), from, to, 0, this);
        list.setAdapter(adapter);

        FloatingActionButton btnAdd = (FloatingActionButton) root.findViewById(R.id.btn_add_cliente);
        btnAdd.setOnClickListener(v -> mostrarAlertCliente(-1));

        mostrarEmptyText();

        return root;
    }

    /** Muestra un alert que permite editar o
     * añadir un cliente
     * @param id long con el id a editar. Si se
     *           pasa un número negativo se añadirá */
    public void mostrarAlertCliente(long id) {
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        // Se asigna la vista personalizada al AlertDialog con los inputs
        // necesarios para crear un cliente
        View dialogView = this.getLayoutInflater().inflate(R.layout.fragment_clients_manage_alert, null);

        alert.setView(dialogView);

        final EditText edtName = (EditText) dialogView.findViewById(R.id.edt_alert_client_name);
        final EditText edtSurname = (EditText) dialogView.findViewById(R.id.edt_alert_client_surname);
        final EditText edtEmail = (EditText) dialogView.findViewById(R.id.edt_alert_client_email);
        final EditText edtPhone = (EditText) dialogView.findViewById(R.id.edt_alert_client_phone);

        // ¿Se quiere crear o editar un cliente?
        if (id < 0) {
            alert.setTitle(R.string.fragment_clients_alert_add_title);
        }
        else {
            alert.setTitle(getString(R.string.fragment_clients_alert_edit_title) + " " + id);

            final Cursor cliente = datasource.getCliente(id);
            cliente.moveToFirst();
            edtName.setText(cliente.getString(cliente.getColumnIndexOrThrow(BuidemHelper.CLIENT_NOM)));
            edtSurname.setText(cliente.getString(cliente.getColumnIndexOrThrow(BuidemHelper.CLIENT_COGNOMS)));
            edtEmail.setText(cliente.getString(cliente.getColumnIndexOrThrow(BuidemHelper.CLIENT_EMAIL)));
            edtPhone.setText(cliente.getString(cliente.getColumnIndexOrThrow(BuidemHelper.CLIENT_TELEFON)));

            alert.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.default_alert_delete), (dialog, which) -> mostrarAlertEliminarCliente(id));
        }

        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), (dialog, which) -> {

            // Se recupera el contenido de los inputs del alert
            final String name = edtName.getText().toString();
            final String surname = edtSurname.getText().toString();
            final String email = edtEmail.getText().toString();
            final String phone = edtPhone.getText().toString();

            // Se prepara una variable para conocer el estado de la queries
            long status;

            // ¿Se tiene que crear o actualizar un cliente?
            if (id < 0) {
                status = datasource.insertCliente(name, surname, email, phone);
            }
            else {
                status = datasource.updateCliente(id, name, surname, email, phone);
            }

            // Ha ido bien
            if (status > 0)
                mostrarSnackbarSuccess(getString(R.string.fragment_clients_snackbar_success_default));
            // No se ha hecho el update
            else if (status == 0)
                mostrarSnackbarError(getString(R.string.fragment_clients_snackbar_error_updating));
            // No se ha podido crear
            else
                mostrarSnackbarError(getString(R.string.fragment_clients_snackbar_error_inserting));

            refreshList();
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.default_alert_cancel), (dialog, which) -> {
            // Nothing
        });

        alert.show();
    }

    /** Muestra un alert de confirmación antes de eliminar
     * @param id long con el id del elemento a eliminar */
    public void mostrarAlertEliminarCliente(long id) {
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        alert.setTitle(getString(R.string.default_alert_delete));
        alert.setMessage(getString(R.string.default_alert_delete_confirmation) + " " + getString(R.string.fragment_clients_alert_edit_title) + " " + id + "?");

        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), (dialog, which) -> {
            long status = datasource.deleteCliente(id);

            if (status > 0)
                mostrarSnackbarSuccess(getString(R.string.fragment_zonas_snackbar_successfuly_deleted));
            else
                mostrarSnackbarError(getString(R.string.fragment_clients_snackbar_error_deleting));

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