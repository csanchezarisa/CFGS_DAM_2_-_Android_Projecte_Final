package com.example.buidemsl.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private ListView list;
    private MainDatasource datasource;
    private MachineListAdapter adapter;

    private TextView listEmptyText;
    private ImageView listEmptyImg;

    private MachineOrderByEnum orderByEnum = MachineOrderByEnum.CLIENT_NAME;

    private String[] from = new String[]{
            BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_NUMERO_SERIE,
            BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_ADRECA,
            BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_CODI_POSTAL,
            BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_POBLACIO,
            BuidemHelper.TABLE_CLIENT + "." + BuidemHelper.CLIENT_TELEFON,
            BuidemHelper.TABLE_CLIENT + "." + BuidemHelper.CLIENT_EMAIL,
            BuidemHelper.TABLE_TIPUS + "." + BuidemHelper.TIPUS_DESCRIPCIO,
            BuidemHelper.TABLE_ZONA + "." + BuidemHelper.ZONA_DESCRIPCIO
    };

    private int[] to = new int[]{
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

        datasource = new MainDatasource(getContext());

        listEmptyText = (TextView) root.findViewById(R.id.txt_maquinas_empty);
        listEmptyImg = (ImageView) root.findViewById(R.id.img_maquinas_empty);

        list = root.findViewById(R.id.list_maquines);
        adapter = new MachineListAdapter(getContext(), R.layout.fragment_home_list, datasource.getMaquinas(orderByEnum.label), from, to, 0, this);
        list.setAdapter(adapter);

        FloatingActionButton btnAdd = root.findViewById(R.id.btn_add_maquina);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMachineManagement(-1);
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


    /** Actualiza el contenidoq que se muestra en la lista */
    private void refreshList() {
        adapter.changeCursor(datasource.getZonas());
        mostrarEmptyText();
    }

    /** Navega hasta el fragment para gestionar
     * una maquina
     * @param id long con el ID de la maquina a
     *           gestionar. En caso de querer crear
     *           una hay que pasar un número negativo*/
    public void openMachineManagement(long id) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_machineManagmentFragment, bundle);
    }

    /** Navega hasta el fragment del mapa pasando
     * el ID de la maquina seleccionada como parámetro
     * @param id long con el ID de la maquina a mostrar */
    public void openMap(long id) {
        Bundle bundle = new Bundle();
        bundle.putString("column_name", BuidemHelper.TABLE_MAQUINA);
        bundle.putLong("id", id);
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_mapsFragment, bundle);
    }
}