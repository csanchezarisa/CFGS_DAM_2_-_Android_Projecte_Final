package com.example.buidemsl.ui.machinemanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.example.buidemsl.util.CursorsUtil;
import com.example.buidemsl.util.objects.Date;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MachineManagmentFragment extends Fragment {

    // ID de la maquina a editar
    private long id = -1;

    // Botones para añadir/editar/eliminar la maquina
    private FloatingActionButton btnAdd;
    private FloatingActionButton btnEdit;
    private FloatingActionButton btnDelete;

    // Inputs necesarios para crear una maquina
    private EditText edtSerial;
    private EditText edtDirection;
    private EditText edtPostalCode;
    private EditText edtTown;
    private EditText edtLastRevision;
    private Spinner dropClient;
    private Spinner dropType;
    private Spinner dropZone;

    // Datasource
    private MainDatasource datasource;

    // Constantes para saber a qué fragment navegar
    private static final int NAVIGATE_CLIENTS = 1;
    private static final int NAVIGATE_ZONES = 2;
    private static final int NAVIGATE_TYPES = 3;
    private static final int NAVIGATE_MAPS = 4;
    private static final int EDIT_ACTION = 1;
    private static final int CREATE_ACTION = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_machine_managment, container, false);

        if (getArguments() != null)
            id = getArguments().getLong("id");

        datasource = new MainDatasource(getContext());

        // Para editar las opciones del menú por las personalizadas
        setHasOptionsMenu(true);

        btnAdd = (FloatingActionButton) root.findViewById(R.id.btn_add_new_maquina);
        btnEdit = (FloatingActionButton) root.findViewById(R.id.btn_edit_maquina);
        btnDelete = (FloatingActionButton) root.findViewById(R.id.btn_delete_maquina);

        // Botones para añadir cliente, tipo y zona
        ImageButton btnAddClient = (ImageButton) root.findViewById(R.id.btn_add_new_client);
        ImageButton btnAddType = (ImageButton) root.findViewById(R.id.btn_add_new_tipus);
        ImageButton btnAddZone = (ImageButton) root.findViewById(R.id.btn_add_new_zona);

        edtSerial = (EditText) root.findViewById(R.id.edt_machine_serial);
        edtDirection = (EditText) root.findViewById(R.id.edt_machine_adreca);
        edtPostalCode = (EditText) root.findViewById(R.id.edt_machine_cp);
        edtTown = (EditText) root.findViewById(R.id.edt_machine_poblacio);
        edtLastRevision = (EditText) root.findViewById(R.id.edt_machine_ultima_revisio);
        dropClient = (Spinner) root.findViewById(R.id.drop_machine_client);
        dropType = (Spinner) root.findViewById(R.id.drop_machine_tipus);
        dropZone = (Spinner) root.findViewById(R.id.drop_machine_zona);

        btnAdd.setOnClickListener(v -> manageMachine(CREATE_ACTION));

        btnEdit.setOnClickListener(v -> manageMachine(EDIT_ACTION));

        btnDelete.setOnClickListener(v -> mostrarAlertEliminarZona(id));

        btnAddClient.setOnClickListener(v -> navigateFragment(0, NAVIGATE_CLIENTS));

        btnAddType.setOnClickListener(v -> navigateFragment(0, NAVIGATE_TYPES));

        btnAddZone.setOnClickListener(v -> navigateFragment(0, NAVIGATE_ZONES));

        edtLastRevision.setOnClickListener(v -> {
            Date date;
            try {
                date = new Date(edtLastRevision.getText().toString(), false);
            }
            catch (Exception e) {
                date = null;
            }
            mostrarDatePickerDialog(date);
        });

        setSpinnerAdapters();

        configureLayout();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Si se está mirando una máquina ya creada, se activa el botón para
        // verla en el mapa
        if (id >= 0)
            inflater.inflate(R.menu.fragment_machine_managment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_menu_map) {
            navigateFragment(id, NAVIGATE_MAPS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Rellena los spiners/dropdowns con el contenido  */
    private void setSpinnerAdapters() {

        final int[] to = new int[]{
                android.R.id.text1
        };

        final String[] cFrom = new String[]{
                BuidemHelper.CLIENT_COGNOMS
        };
        final String[] tFrom = new String[]{
                BuidemHelper.TIPUS_DESCRIPCIO
        };
        final String[] zFrom = new String[]{
                BuidemHelper.ZONA_DESCRIPCIO
        };

        SimpleCursorAdapter cAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, datasource.getClientes(), cFrom, to, 0);
        SimpleCursorAdapter tAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, datasource.getTipos(), tFrom, to, 0);
        SimpleCursorAdapter zAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, datasource.getZonas(), zFrom, to, 0);

        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropClient.setAdapter(cAdapter);
        dropType.setAdapter(tAdapter);
        dropZone.setAdapter(zAdapter);
    }

    /** Actualiza/crea una máquina.
     * @param action int con la constante referente
     *               a la acción a realizar */
    private void manageMachine(int action) {
        final String serial = edtSerial.getText().toString();
        final String direction = edtDirection.getText().toString();
        final String postalCode = edtPostalCode.getText().toString();
        final String town = edtTown.getText().toString();
        Date date;
        try {
            date = new Date(edtLastRevision.getText().toString(), false);
        }
        catch (Exception ignored) {
            date = null;
        }
        final long client = dropClient.getSelectedItemId();
        final long zone = dropZone.getSelectedItemId();
        final long type = dropType.getSelectedItemId();

        long status;

        switch (action) {
            case EDIT_ACTION:
                status = datasource.updateMaquina(id, serial, direction, postalCode, town, date, client, type, zone);
                break;

            case CREATE_ACTION:
                status = datasource.insertMaquina(serial, direction, postalCode, town, date, client, type, zone);
                break;

            default:
                status = -1;
        }

        if (status > 0) {
            finish();
        }
        else if (status == 0) {
            mostrarSnackbarError(getString(R.string.fragment_clients_snackbar_error_updating));
        }
        else {
            mostrarSnackbarError(getString(R.string.fragment_clients_snackbar_error_inserting));
        }
    }

    /** Finaliza el fragment */
    private void finish() {
        Navigation.findNavController(getView()).popBackStack();
    }

    /** Si el id a editar es negativo
     * esconde los botones de edición.
     * Si el id es positivo esconde el
     * botón para añadir una nueva máquina
     * y rellena los inputs con los datos
     * registrados en la bbdd */
    private void configureLayout() {
        if (id < 0) {
            // Se esconden los botones de edición
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }
        else {
            // Se esconde el botón para añadir
            btnAdd.setVisibility(View.GONE);

            try {
                Cursor maquina = datasource.getMaquina(id);
                maquina.moveToFirst();

                final String serial = maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_NUMERO_SERIE));
                final String direction = maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_ADRECA));
                final String postalCode = maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_CODI_POSTAL));
                final String town = maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_POBLACIO));
                Date date;
                try {
                    date = new Date(maquina.getString(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_ULTIMA_REVISIO)), true);
                }
                catch (Exception e) {
                    date = null;
                }
                final long client = maquina.getLong(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_CLIENT));
                final long zone = maquina.getLong(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_ZONA));
                final long type = maquina.getLong(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_TIPUS));
                maquina.close();

                edtSerial.setText(serial);
                edtDirection.setText(direction);
                edtPostalCode.setText(postalCode);
                edtTown.setText(town);
                if (date != null)
                    edtLastRevision.setText(date.getEuropeanDate());

                dropClient.setSelection(CursorsUtil.getItemPosition(dropClient.getAdapter(), client));
                dropZone.setSelection(CursorsUtil.getItemPosition(dropZone.getAdapter(), zone));
                dropType.setSelection(CursorsUtil.getItemPosition(dropType.getAdapter(), type));
            }
            catch (Exception e) {
                mostrarSnackbarError(e.toString());
            }

        }
    }

    /** Cambia el fragment al seleccionado
     * @param idMaquina long con la máquina seleccionada.
     *                  Solo se usará cuando se abra el mapa
     * @param navigate int con la constante de la acción a realizar */
    private void navigateFragment(long idMaquina, int navigate) {
        switch (navigate) {
            case NAVIGATE_CLIENTS:
                NavHostFragment.findNavController(this).navigate(R.id.action_machineManagmentFragment_to_clientsFragment);
                break;

            case NAVIGATE_TYPES:
                NavHostFragment.findNavController(this).navigate(R.id.action_machineManagmentFragment_to_nav_tipos);
                break;

            case NAVIGATE_ZONES:
                NavHostFragment.findNavController(this).navigate(R.id.action_machineManagmentFragment_to_nav_zonas);
                break;

            case NAVIGATE_MAPS:
                Bundle bundle = new Bundle();
                bundle.putString("column_name", BuidemHelper.MAQUINA_ID);
                bundle.putLong("id", idMaquina);
                NavHostFragment.findNavController(this).navigate(R.id.action_machineManagmentFragment_to_mapsFragment, bundle);
                break;
        }
    }

    /** Muestra un alert de confirmación antes de eliminar
     * @param id long con el id del elemento a eliminar */
    private void mostrarAlertEliminarZona(long id) {
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        alert.setTitle(getString(R.string.default_alert_delete));
        alert.setMessage(getString(R.string.default_alert_delete_confirmation) + " " + getString(R.string.fragment_machine_managment_alert_content) + " " + id + "?");

        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), (dialog, which) -> {
            final long status = datasource.deleteMaquina(id);

            if (status <= 0)
                mostrarSnackbarError(getString(R.string.fragment_zonas_snackbar_error_deleting));
            else {
                finish();
            }
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.default_alert_cancel), (dialog, which) -> {
            // Nothing
        });

        alert.show();
    }

    /** Muestra un DatePickerDialog que permite seleccionar
     * una fecha
     * @param date Date con la fecha que se quiere mostrar
     *             por defecto. Null si no se quiere mostrar
     *             una fecha por defecto*/
    private void mostrarDatePickerDialog(@Nullable Date date) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());

        if (date != null)
            datePickerDialog.updateDate(date.getYear(), date.getMonth(), date.getDay());

        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            final Date selectedDate = new Date(dayOfMonth, month, year);
            edtLastRevision.setText(selectedDate.getEuropeanDate());
        });

        datePickerDialog.show();
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
}