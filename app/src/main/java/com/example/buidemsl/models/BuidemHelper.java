package com.example.buidemsl.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuidemHelper extends SQLiteOpenHelper {

    /* .: 1. DEFINICIÓ DE LES DADES DE LA BBDD :. */
    // Dades sobre la base de dades
    private static final int DATABSE_VERSION = 1;
    private static final String DATABASE_NAME = "Buidem_SL_DataBase";

    // Taules
    private static final String TABLE_CLIENT = "client";
    private static final String TABLE_MAQUINA = "maquina";
    private static final String TABLE_TIPUS = "tipus";
    private static final String TABLE_ZONA = "zona";

    // Taula client
    private static final String CLIENT_ID = "_id";
    private static final String CLIENT_NOM = "nom";
    private static final String CLIENT_COGNOMS = "cognoms";
    private static final String CLIENT_EMAIL = "email";
    private static final String CLIENT_TELEFON = "telefon";

    // Taula tipus
    private static final String TIPUS_ID = "_id";
    private static final String TIPUS_DESCRIPCIO = "descripcio";

    // Taula zona
    private static final String ZONA_ID = "_id";
    private static final String ZONA_DESCRIPCIO = "descripcio";

    // Taula maquina
    private static final String MAQUINA_ID = "_id";
    private static final String MAQUINA_ADRECA = "adreca";
    private static final String MAQUINA_CODI_POSTAL = "codi_postal";
    private static final String MAQUINA_POBLACIO = "poblacio";
    private static final String MAQUINA_NUMERO_SERIE = "numero_serie";
    private static final String MAQUINA_ULTIMA_REVISIO = "ultima_revisio";
    private static final String MAQUINA_CLIENT = "client_id";
    private static final String MAQUINA_TIPUS = "tipus_id";
    private static final String MAQUINA_ZONA = "zona_id";

    /** Constructor per crear la base de dades el primer cop que s'instal·la l'aplicació */
    public BuidemHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creació de la taula CLIENT
        String sqlCode =
                "CREATE TABLE " + TABLE_CLIENT + "(" +
                CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CLIENT_NOM + " TEXT NOT NULL," +
                CLIENT_COGNOMS + " TEXT NOT NULL," +
                CLIENT_EMAIL + " TEXT," +
                CLIENT_TELEFON + " TEXT);";
        db.execSQL(sqlCode);

        // Creació de la taula ZONA
        sqlCode =
                "CREATE TABLE " + TABLE_ZONA + "(" +
                ZONA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ZONA_DESCRIPCIO + " TEXT NOT NULL UNIQUE);";
        db.execSQL(sqlCode);

        // Creació de la taula TIPUS
        sqlCode =
                "CREATE TABLE " + TABLE_ZONA + "(" +
                TIPUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TIPUS_DESCRIPCIO + " TEXT NOT NULL UNIQUE);";
        db.execSQL(sqlCode);

        // Creació de la taula MAQUINA
        sqlCode =
                "CREATE TABLE " + TABLE_MAQUINA + "(" +
                MAQUINA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MAQUINA_NUMERO_SERIE + " TEXT NOT NULL UNIQUE," +
                MAQUINA_ADRECA + " TEXT NOT NULL," +
                MAQUINA_CODI_POSTAL + " TEXT NOT NULL," +
                MAQUINA_POBLACIO + " TEXT NOT NULL," +
                MAQUINA_ULTIMA_REVISIO + " TEXT," +
                MAQUINA_CLIENT + " INTEGER NOT NULL," +
                MAQUINA_TIPUS + " INTEGER NOT NULL," +
                MAQUINA_ZONA + " INTEGER NOT NULL," +

                // Foreign key amb la taula CLIENT
                "FOREIGN KEY (" + MAQUINA_CLIENT + ") REFERENCES " + TABLE_CLIENT + '(' + CLIENT_ID + ')' +
                    "ON DELETE RESTRICT " +
                    "ON UPDATE CASCADE, " +

                // Foreign key amb la taula TIPUS
                "FOREIGN KEY (" + MAQUINA_TIPUS + ") REFERENCES " + TABLE_TIPUS + '(' + TIPUS_ID + ')' +
                    "ON DELETE RESTRICT " +
                    "ON UPDATE CASCADE, " +

                // Foreign key amb la taula ZONA
                "FOREIGN KEY (" + MAQUINA_ZONA + ") REFERENCES " + TABLE_ZONA + '(' + ZONA_ID + ')' +
                    "ON DELETE RESTRICT " +
                    "ON UPDATE CASCADE);";
        db.execSQL(sqlCode);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
