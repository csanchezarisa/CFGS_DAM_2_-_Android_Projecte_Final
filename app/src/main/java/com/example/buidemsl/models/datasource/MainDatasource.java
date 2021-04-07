package com.example.buidemsl.models.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.buidemsl.models.BuidemHelper;

public class MainDatasource {

    private final BuidemHelper dbHelper;
    private final SQLiteDatabase dbW, dbR;

    public MainDatasource(Context context) {
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

    /** Consulta que devuelve todas las zonas
     * @return Cursor con las zonas que hay en la BBDD */
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

    /** Consulta que devuelve una zona buscada por ID
     * @param id long con el id a buscar
     * @return Cursor con la zona buscada */
    public Cursor getZona(long id) {
        Cursor zona = dbR.query(
                BuidemHelper.TABLE_ZONA,
                new String[]{BuidemHelper.ZONA_ID, BuidemHelper.ZONA_DESCRIPCIO},
                BuidemHelper.ZONA_ID + " = " + id,
                null,
                null,
                null,
                null
        );

        return zona;
    }

    /** Actualiza la información de una zona
     * @param id long con el id de la zona a actualizar
     * @param descripcion String con el contenido de la nueva
     * descripción
     * @return int con el número de filas afectadas*/
    public int updateZona(long id, String descripcion) {

        ContentValues values = new ContentValues();
        values.put(BuidemHelper.ZONA_DESCRIPCIO, descripcion);

        int rows = dbW.update(
                BuidemHelper.TABLE_ZONA,
                values,
                BuidemHelper.ZONA_ID + " = " + id,
                null
        );

        return rows;
    }

    /** Inserta una nueva zona.
     * @param descripcion String con la descripción
     * de la zona a insertar
     * @return long con el id del nuevo insert*/
    public long insertZona(String descripcion) {

        ContentValues values = new ContentValues();
        values.put(BuidemHelper.ZONA_DESCRIPCIO, descripcion);

        long id = dbW.insert(
                BuidemHelper.TABLE_ZONA,
                null,
                values
        );

        return id;
    }

    /** Elimna la zona especificada
     * @param id long con el id de
     * la zona a eliminar
     * @return int con el número de
     * filas afectadas*/
    public int deleteZona(long id) {

        int rows = dbW.delete(
                BuidemHelper.TABLE_ZONA,
                BuidemHelper.ZONA_ID + " = " + id,
                null
        );

        return rows;
    }

}
