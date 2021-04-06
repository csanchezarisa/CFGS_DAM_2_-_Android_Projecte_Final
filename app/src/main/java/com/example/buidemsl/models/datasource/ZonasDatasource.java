package com.example.buidemsl.models.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.buidemsl.models.BuidemHelper;

public class ZonasDatasource {

    private final BuidemHelper dbHelper;
    private final SQLiteDatabase dbW, dbR;

    public ZonasDatasource(Context context) {
        dbHelper = new BuidemHelper(context);

        dbW = dbHelper.getWritableDatabase();
        dbR = dbHelper.getReadableDatabase();
    }

    @Override
    protected void finalize() throws Throwable {
        dbW.close();
        dbR.close();
        dbHelper.close();
        super.finalize();
    }

    public Cursor getZonas() {
        Cursor zonas = dbR.query(
                BuidemHelper.TABLE_ZONA,
                new String[]{BuidemHelper.ZONA_ID, BuidemHelper.ZONA_DESCRIPCIO},
                null,
                null,
                null,
                null,
                null
        );

        return zonas;
    }

}
