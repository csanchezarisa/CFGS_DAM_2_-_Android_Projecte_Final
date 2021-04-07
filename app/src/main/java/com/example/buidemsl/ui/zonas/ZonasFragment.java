package com.example.buidemsl.ui.zonas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;

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

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        return root;
    }
}