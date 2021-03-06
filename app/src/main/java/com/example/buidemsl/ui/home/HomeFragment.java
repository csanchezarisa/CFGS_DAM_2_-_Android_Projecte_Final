package com.example.buidemsl.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private MainDatasource datasource;
    private MachineListAdapter adapter;

    private TextView listEmptyText;
    private ImageView listEmptyImg;
    private Menu menu;

    private MachineOrderByEnum orderByEnum = MachineOrderByEnum.CLIENT_NAME;
    private String filter = "";

    private final String[] from = new String[]{
            BuidemHelper.MAQUINA_NUMERO_SERIE,
            BuidemHelper.MAQUINA_ADRECA,
            BuidemHelper.MAQUINA_CODI_POSTAL,
            BuidemHelper.MAQUINA_POBLACIO,
            BuidemHelper.CLIENT_TELEFON,
            BuidemHelper.CLIENT_EMAIL,
            "tDescripcio",
            "zDescripcio"
    };

    private final int[] to = new int[]{
            R.id.txt_list_item_maquina_serial,
            R.id.txt_list_item_maquina_adreca,
            R.id.txt_list_item_maquina_codi_postal,
            R.id.txt_list_item_maquina_poblacio,
            R.id.txt_list_item_maquina_client_phone,
            R.id.txt_list_item_maquina_client_email,
            R.id.txt_list_item_maquina_tipus,
            R.id.txt_list_item_maquina_zona
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Para editar las opciones del men?? por las personalizadas
        setHasOptionsMenu(true);

        datasource = new MainDatasource(getContext());

        listEmptyText = (TextView) root.findViewById(R.id.txt_maquinas_empty);
        listEmptyImg = (ImageView) root.findViewById(R.id.img_maquinas_empty);

        ListView list = root.findViewById(R.id.list_maquines);
        adapter = new MachineListAdapter(getContext(), R.layout.fragment_home_list, datasource.getMaquinas(filter, orderByEnum.label, null), from, to, 0, this);
        list.setAdapter(adapter);

        FloatingActionButton btnAdd = root.findViewById(R.id.btn_add_maquina);
        btnAdd.setOnClickListener(v -> openMachineManagement(-1));

        mostrarEmptyText();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        setFilterMenuIcon();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_menu_filter:
                mostrarAlertFilter();
                return true;

            case R.id.btn_menu_order:
                mostrarAlertOrderBy();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    /** Muestra un alert que permite seleccionar el Order By de la lista */
    private void mostrarAlertOrderBy() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle(R.string.fragment_home_alert_order_by_title);

        final int[] selectedItem = {orderByEnum.ordinal()};

        alert.setSingleChoiceItems(R.array.fragment_home_alert_order_by_content, selectedItem[0], (dialog, which) -> selectedItem[0] = which);

        alert.setPositiveButton(R.string.default_alert_accept, (dialog, which) -> {
            switch (selectedItem[0]) {
                case 1:
                    orderByEnum = MachineOrderByEnum.ZONE;
                    break;

                case 2:
                    orderByEnum = MachineOrderByEnum.TOWN;
                    break;

                case 3:
                    orderByEnum = MachineOrderByEnum.DIRECTION;
                    break;

                case 4:
                    orderByEnum = MachineOrderByEnum.LAST_REV_DATE;
                    break;

                default:
                    orderByEnum = MachineOrderByEnum.CLIENT_NAME;
            }

            refreshList();
        });

        alert.setNegativeButton(R.string.default_alert_cancel, (dialog, which) -> {
            // Nothing
        });

        alert.setNeutralButton(R.string.default_alert_clear, (dialog, which) -> {
            orderByEnum = MachineOrderByEnum.CLIENT_NAME;
            refreshList();
        });

        alert.show();
    }

    private void mostrarAlertFilter() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

        alertDialog.setTitle(R.string.fragment_home_alert_filter_title);

        EditText edtSerialNumber = new EditText(getContext());
        edtSerialNumber.setHint(getString(R.string.fragment_machine_managment_serial));
        edtSerialNumber.setText(filter);

        alertDialog.setView(edtSerialNumber);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.default_alert_accept), (dialog, which) -> {
            filter = edtSerialNumber.getText().toString();
            refreshList();
            setFilterMenuIcon();
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.default_alert_cancel), (dialog, which) -> {
            // Nothing
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.default_alert_clear), (dialog, which) -> {
            filter = "";
            refreshList();
            setFilterMenuIcon();
        });

        alertDialog.show();
    }

    /** M??todo que cambia el icono de la opci??n de b??squeda del men??
     * cuando se est?? filtrando por alg??n n??mero de s??rie.
     * Si no se est?? filtrando, pone el icono de la lupa*/
    private void setFilterMenuIcon() {
        if (this.filter.length() > 0)
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_filter));
        else
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_search));
    }

    /** Actualiza el contenidoq que se muestra en la lista */
    private void refreshList() {
        adapter.changeCursor(datasource.getMaquinas(filter, orderByEnum.label, null));
        mostrarEmptyText();
    }

    /** Navega hasta el fragment para gestionar
     * una maquina
     * @param id long con el ID de la maquina a
     *           gestionar. En caso de querer crear
     *           una hay que pasar un n??mero negativo*/
    public void openMachineManagement(long id) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_machineManagmentFragment, bundle);
    }

    /** Navega hasta el fragment del mapa pasando
     * el ID de la maquina seleccionada como par??metro
     * @param id long con el ID de la maquina a mostrar */
    public void openMap(long id) {
        Bundle bundle = new Bundle();
        bundle.putString("column_name", BuidemHelper.MAQUINA_ID);
        bundle.putLong("id", id);
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_mapsFragment, bundle);
    }
}