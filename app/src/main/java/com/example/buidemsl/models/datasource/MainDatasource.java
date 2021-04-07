package com.example.buidemsl.models.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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


    /* .: 1 - ZONAS :. */
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

        if (descripcion.length() > 0) {
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

        return -1;
    }

    /** Inserta una nueva zona.
     * @param descripcion String con la descripción
     * de la zona a insertar
     * @return long con el id del nuevo insert*/
    public long insertZona(String descripcion) {

        if (descripcion.length() > 0) {
            ContentValues values = new ContentValues();
            values.put(BuidemHelper.ZONA_DESCRIPCIO, descripcion);

            long id = dbW.insert(
                    BuidemHelper.TABLE_ZONA,
                    null,
                    values
            );

            return id;
        }

        return -1;
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


    /* .: 2 - TIPOS :. */
    public Cursor getTipos() {
        Cursor tipos = dbR.query(
                BuidemHelper.TABLE_TIPUS,
                new String[]{BuidemHelper.TIPUS_ID, BuidemHelper.TIPUS_DESCRIPCIO, BuidemHelper.TIPUS_COLOR},
                null,
                null,
                null,
                null,
                null
        );

        return tipos;
    }

    public Cursor getTipo(long id) {
        Cursor tipo = dbR.query(
                BuidemHelper.TABLE_TIPUS,
                new String[]{BuidemHelper.TIPUS_ID, BuidemHelper.TIPUS_DESCRIPCIO, BuidemHelper.TIPUS_COLOR},
                BuidemHelper.TIPUS_ID + " = " + id,
                null,
                null,
                null,
                null
        );

        return tipo;
    }

    /** Permite actualizar un tipo especificado
     * @param id long con el id del tipo que se
     *           quiere actualizar
     * @param descripcion String con la nueva
     *                   descripción para el tipo
     * @param color String con el nuevo color para
     *              el tipo
     * @return int con el número de filas afectadas */
    public int updateTipo(@NonNull long id, @Nullable String descripcion, @Nullable String color) {
        ContentValues values = new ContentValues();

        if (descripcion != null && descripcion.length() > 0)
            values.put(BuidemHelper.TIPUS_DESCRIPCIO, descripcion);

        if (color != null && color.length() > 0)
            values.put(BuidemHelper.TIPUS_COLOR, color);

        int rows = dbW.update(
                BuidemHelper.TABLE_TIPUS,
                values,
                BuidemHelper.TIPUS_ID + " = " + id,
                null
        );

        return rows;
    }
}
