package com.example.buidemsl.ui.zonas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.ZonasDatasource;

public class ZonasFragment extends Fragment {

    private ListView list;
    private ZonasDatasource datasource;
    private CursorAdapter adapter;

    private String[] from = new String[]{
            BuidemHelper.ZONA_ID,
            BuidemHelper.ZONA_DESCRIPCIO
    };
    private int[] to = new int[]{
            R.id.id,
            R.id.descripcion
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_zonas, container, false);
        list = (ListView) root.findViewById(R.id.list_zonas);
        datasource = new ZonasDatasource(getContext());
        adapter = new SimpleCursorAdapter(getContext(), R.layout.fragment_zonas_list, datasource.getZonas(), from, to, 0);
        list.setAdapter(adapter);
        return root;
    }
}