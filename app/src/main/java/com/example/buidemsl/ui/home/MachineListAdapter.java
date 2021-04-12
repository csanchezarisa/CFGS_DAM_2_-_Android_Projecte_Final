package com.example.buidemsl.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

public class MachineListAdapter extends SimpleCursorAdapter {

    private HomeFragment parentFragment;

    public MachineListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Fragment parentFragment) {
        super(context, layout, c, from, to, flags);
        this.parentFragment = (HomeFragment) parentFragment;
    }
}
