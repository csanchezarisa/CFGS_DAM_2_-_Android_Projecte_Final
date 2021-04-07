package com.example.buidemsl.ui.tipos;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

public class TiposListAdapter extends SimpleCursorAdapter {

    private TiposFragment parentFragment;

    public TiposListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Fragment parentFragment) {
        super(context, layout, c, from, to, flags);
        this.parentFragment = (TiposFragment) parentFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        return view;
    }
}
