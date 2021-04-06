package com.example.buidemsl.ui.tipos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.buidemsl.R;

public class TiposFragment extends Fragment {

    private TiposViewModel tiposViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tiposViewModel =
                new ViewModelProvider(this).get(TiposViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tipos, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        tiposViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}