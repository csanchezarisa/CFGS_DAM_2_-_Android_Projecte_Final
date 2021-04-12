package com.example.buidemsl.ui.clients;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;

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