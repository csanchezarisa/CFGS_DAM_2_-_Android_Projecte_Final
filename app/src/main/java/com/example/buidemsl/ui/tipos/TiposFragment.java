package com.example.buidemsl.ui.tipos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;

public class TiposFragment extends Fragment {

    private ListView list;
    private MainDatasource datasource;
    private TiposListAdapter adapter;

    private String[] from = new String[]{
            BuidemHelper.TIPUS_ID,
            BuidemHelper.TIPUS_DESCRIPCIO
    };
    private int[] to = new int[]{
            R.id.list_item_tipo_id,
            R.id.list_item_tipo_descripcio
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tipos, container, false);
        datasource = new MainDatasource(getContext());

        // Se busca la lista y se le asigna el adapter. Se crean los listeners
        list = (ListView) root.findViewById(R.id.list_tipos);
        adapter = new TiposListAdapter(getContext(), R.layout.fragment_tipos_list, datasource.getTipos(), from, to, 0, this);
        list.setAdapter(adapter);

        return root;
    }
}