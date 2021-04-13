package com.example.buidemsl.models.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.util.Date;

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

    public long insertTipo(@NonNull String descripcion, @Nullable String color) {

        long id = -1;

        if (descripcion.length() > 0) {

            ContentValues values = new ContentValues();
            values.put(BuidemHelper.TIPUS_DESCRIPCIO, descripcion);
            if (color != null)
                values.put(BuidemHelper.TIPUS_COLOR, color);

            id = dbW.insert(
                    BuidemHelper.TABLE_TIPUS,
                    null,
                    values
            );
        }

        return id;
    }

    /** Elimna la zona especificada
     * @param id long con el id de
     * la zona a eliminar
     * @return int con el número de
     * filas afectadas*/
    public int deleteTipo(long id) {

        int rows = dbW.delete(
                BuidemHelper.TABLE_TIPUS,
                BuidemHelper.TIPUS_ID + " = " + id,
                null
        );

        return rows;
    }


    /* .: 3 - CLIENTES :. */
    /** Devuelve un cursor con todos
     * los registros de la tabla
     * clients */
    public Cursor getClientes() {
        Cursor clientes = dbR.query(
                BuidemHelper.TABLE_CLIENT,
                new String[]{
                        BuidemHelper.CLIENT_ID,
                        BuidemHelper.CLIENT_NOM,
                        BuidemHelper.CLIENT_COGNOMS,
                        BuidemHelper.CLIENT_EMAIL,
                        BuidemHelper.CLIENT_TELEFON
                },
                null,
                null,
                null,
                null,
                null
        );

        return clientes;
    }

    /** Devuelve un cursor con el cliente deseado
     * @param id long con el id del cliente a buscar
     * @return Cursor con la query*/
    public Cursor getCliente(long id) {
        Cursor cliente = dbR.query(
                BuidemHelper.TABLE_CLIENT,
                new String[]{
                        BuidemHelper.CLIENT_ID,
                        BuidemHelper.CLIENT_NOM,
                        BuidemHelper.CLIENT_COGNOMS,
                        BuidemHelper.CLIENT_EMAIL,
                        BuidemHelper.CLIENT_TELEFON
                },
                BuidemHelper.CLIENT_ID + " = " + id,
                null,
                null,
                null,
                null
        );

        return cliente;
    }

    /** Permite actualizar la información de un cliente
     * @param id ID del cliente a actualizar
     * @param name Nombre del cliente
     * @param surname Apellidos del cliente
     * @param email Email del cliente
     * @param phone Teléfono del cliente
     * @return int con el número de filas afectadas*/
    public int updateCliente(@NonNull long id, @NonNull String name, @NonNull String surname, @Nullable String email, @Nullable String phone) {
        int rows = 0;

        if (name != null && surname != null &&
                name.length() > 0 && surname.length() > 0) {

            ContentValues values = new ContentValues();
            values.put(BuidemHelper.CLIENT_NOM, name);
            values.put(BuidemHelper.CLIENT_COGNOMS, surname);
            if (email != null)
                values.put(BuidemHelper.CLIENT_EMAIL, email);
            if (phone != null)
                values.put(BuidemHelper.CLIENT_TELEFON, phone);

            rows = dbW.update(
                    BuidemHelper.TABLE_CLIENT,
                    values,
                    BuidemHelper.CLIENT_ID + " = " + id,
                    null
            );
        }

        return rows;
    }

    /** Permite insertar un nuevo registro en la tabla cliente
     * @param name Nombre del cliente
     * @param surname Apellidos del cliente
     * @param email Email del cliente
     * @param phone Teléfono del clietne
     * @return long con el ID del nuevo registro*/
    public long insertCliente(@NonNull String name, @NonNull String surname, @Nullable String email, @Nullable String phone) {
        long id = -1;

        if (name != null && surname != null &&
            name.length() > 0 && surname.length() > 0) {

            ContentValues values = new ContentValues();
            values.put(BuidemHelper.CLIENT_NOM, name);
            values.put(BuidemHelper.CLIENT_COGNOMS, surname);
            if (email != null)
                values.put(BuidemHelper.CLIENT_EMAIL, email);
            if (phone != null)
                values.put(BuidemHelper.CLIENT_TELEFON, phone);

            id = dbW.insert(
                    BuidemHelper.TABLE_CLIENT,
                    null,
                    values
            );
        }

        return id;
    }

    /** Permite eliminar el cliente deseado
     * @param id long con el id del cliente a eliminar
     * @return int con el número de filas afectadas*/
    public int deleteCliente(long id) {
        int rows = dbW.delete(
                BuidemHelper.TABLE_CLIENT,
                BuidemHelper.CLIENT_ID + " = " + id,
                null
        );

        return rows;
    }


    /* .: 4 - MAQUINAS :. */
    private String queryMaquinasSelectHeaders() {
        return String.format(
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s, " +
                "%s.%s AS 'zDescripcio', " +
                "%s.%s AS 'tDescripcio', " +
                "%s.%s",
                BuidemHelper.TABLE_MAQUINA, BuidemHelper.MAQUINA_ID,
                BuidemHelper.TABLE_MAQUINA, BuidemHelper.MAQUINA_NUMERO_SERIE,
                BuidemHelper.TABLE_MAQUINA, BuidemHelper.MAQUINA_ADRECA,
                BuidemHelper.TABLE_MAQUINA, BuidemHelper.MAQUINA_CODI_POSTAL,
                BuidemHelper.TABLE_MAQUINA, BuidemHelper.MAQUINA_POBLACIO,
                BuidemHelper.TABLE_MAQUINA, BuidemHelper.MAQUINA_ULTIMA_REVISIO,
                BuidemHelper.TABLE_MAQUINA, BuidemHelper.MAQUINA_CLIENT,
                BuidemHelper.TABLE_MAQUINA, BuidemHelper.MAQUINA_TIPUS,
                BuidemHelper.TABLE_MAQUINA, BuidemHelper.MAQUINA_ZONA,
                BuidemHelper.TABLE_CLIENT, BuidemHelper.CLIENT_NOM,
                BuidemHelper.TABLE_CLIENT, BuidemHelper.CLIENT_COGNOMS,
                BuidemHelper.TABLE_CLIENT, BuidemHelper.CLIENT_EMAIL,
                BuidemHelper.TABLE_CLIENT, BuidemHelper.CLIENT_TELEFON,
                BuidemHelper.TABLE_ZONA, BuidemHelper.ZONA_DESCRIPCIO,
                BuidemHelper.TABLE_TIPUS, BuidemHelper.TIPUS_DESCRIPCIO,
                BuidemHelper.TABLE_TIPUS, BuidemHelper.TIPUS_COLOR);
    }

    /** Devuelve un cursor con todas las máquinas
     * que hay en la tabla maquina.
     * @param orderBy String Nullable para ordenar
     *                la quey
     * @return Cursor con las maquinas de la tabla */
    public Cursor getMaquinas(@Nullable String orderBy) {
        String sqlCode =
                "SELECT " + queryMaquinasSelectHeaders() +
                " FROM " + BuidemHelper.TABLE_MAQUINA  +
                " INNER JOIN " + BuidemHelper.TABLE_CLIENT +
                " ON " + BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_CLIENT + " = " + BuidemHelper.TABLE_CLIENT + "." + BuidemHelper.CLIENT_ID +
                " INNER JOIN " + BuidemHelper.TABLE_ZONA +
                " ON " + BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_ZONA + " = " + BuidemHelper.TABLE_ZONA + "." + BuidemHelper.ZONA_ID +
                " INNER JOIN " + BuidemHelper.TABLE_TIPUS +
                " ON " + BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_TIPUS + " = " + BuidemHelper.TABLE_TIPUS + "." + BuidemHelper.TIPUS_ID;

        if (orderBy != null && orderBy.length() > 0)
            sqlCode += " ORDER BY " + orderBy;

        return dbR.rawQuery(sqlCode, null);
    }

    /** Devuelve un Cursor con la máquina seleccionada
     * @param id long con el id de la máquina a buscar
     * @return Cursor con la select hecha */
    public Cursor getMaquina(long id) {
        final String sqlCode =
                "SELECT " + queryMaquinasSelectHeaders() +
                " FROM " + BuidemHelper.TABLE_MAQUINA  +
                " INNER JOIN " + BuidemHelper.TABLE_CLIENT +
                " ON " + BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_CLIENT + " = " + BuidemHelper.TABLE_CLIENT + "." + BuidemHelper.CLIENT_ID +
                " INNER JOIN " + BuidemHelper.TABLE_ZONA +
                " ON " + BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_ZONA + " = " + BuidemHelper.TABLE_ZONA + "." + BuidemHelper.ZONA_ID +
                " INNER JOIN " + BuidemHelper.TABLE_TIPUS +
                " ON " + BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_TIPUS + " = " + BuidemHelper.TABLE_TIPUS + "." + BuidemHelper.TIPUS_ID +
                " WHERE " + BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_ID + " = " + id;

        return dbR.rawQuery(sqlCode, null);
    }

    /** Actualiza una máquina ya insertada
     * @param id long con el id de la máquina a actualizar
     * @param serial String con el número de serie de la máquina
     * @param adreca String con la dirección de la máquina
     * @param codiPostal String con el codigo postal
     * @param poblacio String con la población
     * @param date Opcional. Objeto Date con la fecha de última revisión
     * @param client long con el id del Cliente
     * @param tipus long con el id del tipo
     * @param zona long con el id de la zona
     * @return int con el número de filas afectadas*/
    public int updateMaquina(@NonNull long id, @NonNull String serial, @NonNull String adreca, @NonNull String codiPostal, @NonNull String poblacio, @Nullable Date date, @NonNull long client, @NonNull long tipus, @NonNull long zona) {
        int rows = 0;

        // ¿Los Strings obligatorios estan vacíos?
        if (serial.length() > 0 && adreca.length() > 0 && codiPostal.length() > 0 && poblacio.length() > 0) {
            ContentValues values = new ContentValues();
            values.put(BuidemHelper.MAQUINA_NUMERO_SERIE, serial);
            values.put(BuidemHelper.MAQUINA_ADRECA, adreca);
            values.put(BuidemHelper.MAQUINA_CODI_POSTAL, codiPostal);
            values.put(BuidemHelper.MAQUINA_POBLACIO, poblacio);
            values.put(BuidemHelper.MAQUINA_CLIENT, client);
            values.put(BuidemHelper.MAQUINA_TIPUS, tipus);
            values.put(BuidemHelper.MAQUINA_ZONA, zona);

            if (date != null)
                values.put(BuidemHelper.MAQUINA_ULTIMA_REVISIO, date.getSQLDate());

            rows = dbW.update(
                    BuidemHelper.TABLE_MAQUINA,
                    values,
                    BuidemHelper.MAQUINA_ID + " = " + id,
                    null
            );
        }

        return rows;
    }

    /** Inserta una nueva máquina
     * @param serial String con el número de serie de la máquina
     * @param adreca String con la dirección de la máquina
     * @param codiPostal String con el codigo postal
     * @param poblacio String con la población
     * @param date Opcional. Objeto Date con la fecha de última revisión
     * @param client long con el id del Cliente
     * @param tipus long con el id del tipo
     * @param zona long con el id de la zona
     * @return long con el id del nuevo registro */
    public long insertMaquina(@NonNull String serial, @NonNull String adreca, @NonNull String codiPostal, @NonNull String poblacio, @Nullable Date date, @NonNull long client, @NonNull long tipus, @NonNull long zona) {
        long id = -1;

        // ¿Los Strings obligatorios estan vacíos?
        if (serial.length() > 0 && adreca.length() > 0 && codiPostal.length() > 0 && poblacio.length() > 0) {
            ContentValues values = new ContentValues();
            values.put(BuidemHelper.MAQUINA_NUMERO_SERIE, serial);
            values.put(BuidemHelper.MAQUINA_ADRECA, adreca);
            values.put(BuidemHelper.MAQUINA_CODI_POSTAL, codiPostal);
            values.put(BuidemHelper.MAQUINA_POBLACIO, poblacio);
            values.put(BuidemHelper.MAQUINA_CLIENT, client);
            values.put(BuidemHelper.MAQUINA_TIPUS, tipus);
            values.put(BuidemHelper.MAQUINA_ZONA, zona);

            if (date != null)
                values.put(BuidemHelper.MAQUINA_ULTIMA_REVISIO, date.getSQLDate());

            id = dbW.insert(
                    BuidemHelper.TABLE_MAQUINA,
                    null,
                    values
            );
        }

        return id;
    }

    /** Elimina una maquina.
     * @param id long con el id de la maquina
     *           a eliminar
     * @return int con el número de filas afectadas */
    public int deleteMaquina(long id) {
        int rows = dbW.delete(
                BuidemHelper.TABLE_MAQUINA,
                BuidemHelper.MAQUINA_ID + " = " + id,
                null
        );

        return rows;
    }
}
