package com.example.buidemsl.ui.clients;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

public class ClientsListAdapter extends SimpleCursorAdapter {

    private ClientsFragment parentFragment;

    public ClientsListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Fragment parentFragment) {
        super(context, layout, c, from, to, flags);
        this.parentFragment = (ClientsFragment) parentFragment;
    }

}
