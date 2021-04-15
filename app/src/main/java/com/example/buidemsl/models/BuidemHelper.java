package com.example.buidemsl.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuidemHelper extends SQLiteOpenHelper {

    /* .: 1. DEFINICIÓ DE LES DADES DE LA BBDD :. */
    // Dades sobre la base de dades
    public static final int DATABSE_VERSION = 2;
    public static final String DATABASE_NAME = "Buidem_SL_DataBase";

    // Taules
    public static final String TABLE_CLIENT = "client";
    public static final String TABLE_MAQUINA = "maquina";
    public static final String TABLE_TIPUS = "tipus";
    public static final String TABLE_ZONA = "zona";

    // Taula client
    public static final String CLIENT_ID = "_id";
    public static final String CLIENT_NOM = "nom";
    public static final String CLIENT_COGNOMS = "cognoms";
    public static final String CLIENT_EMAIL = "email";
    public static final String CLIENT_TELEFON = "telefon";

    // Taula tipus
    public static final String TIPUS_ID = "_id";
    public static final String TIPUS_DESCRIPCIO = "descripcio";
    public static final String TIPUS_COLOR = "color";

    // Taula zona
    public static final String ZONA_ID = "_id";
    public static final String ZONA_DESCRIPCIO = "descripcio";

    // Taula maquina
    public static final String MAQUINA_ID = "_id";
    public static final String MAQUINA_ADRECA = "adreca";
    public static final String MAQUINA_CODI_POSTAL = "codi_postal";
    public static final String MAQUINA_POBLACIO = "poblacio";
    public static final String MAQUINA_NUMERO_SERIE = "numero_serie";
    public static final String MAQUINA_ULTIMA_REVISIO = "ultima_revisio";
    public static final String MAQUINA_CLIENT = "client_id";
    public static final String MAQUINA_TIPUS = "tipus_id";
    public static final String MAQUINA_ZONA = "zona_id";

    /** Constructor per crear la base de dades el primer cop que s'instal·la l'aplicació */
    public BuidemHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Activa las CONSTRAINTS de las Foreign Key
        String sqlCode =
                "PRAGMA foreign_keys=ON;";
        db.execSQL(sqlCode);

        // Creació de la taula CLIENT
        sqlCode =
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
                "CREATE TABLE " + TABLE_TIPUS + "(" +
                TIPUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TIPUS_DESCRIPCIO + " TEXT NOT NULL UNIQUE," +
                TIPUS_COLOR + " TEXT NOT NULL DEFAULT '#FF0000');";
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
                    "ON UPDATE RESTRICT, " +

                // Foreign key amb la taula TIPUS
                "FOREIGN KEY (" + MAQUINA_TIPUS + ") REFERENCES " + TABLE_TIPUS + '(' + TIPUS_ID + ')' +
                    "ON DELETE RESTRICT " +
                    "ON UPDATE RESTRICT, " +

                // Foreign key amb la taula ZONA
                "FOREIGN KEY (" + MAQUINA_ZONA + ") REFERENCES " + TABLE_ZONA + '(' + ZONA_ID + ')' +
                    "ON DELETE RESTRICT " +
                    "ON UPDATE RESTRICT);";
        db.execSQL(sqlCode);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String sqlCode = "ALTER TABLE " + TABLE_TIPUS +
                    " ADD COLUMN " + TIPUS_COLOR + " TEXT NOT NULL DEFAULT '#FF0000';";
            db.execSQL(sqlCode);
        }
    }
}
