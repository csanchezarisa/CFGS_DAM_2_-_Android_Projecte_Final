package com.example.buidemsl.util;

import android.database.Cursor;
import android.widget.SpinnerAdapter;

import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.util.objects.Date;
import com.example.buidemsl.util.objects.models.Client;
import com.example.buidemsl.util.objects.models.Maquina;
import com.example.buidemsl.util.objects.models.Tipus;
import com.example.buidemsl.util.objects.models.Zona;

import java.util.ArrayList;


public class CursorsUtil {

    /** Recorre el Cursor del SpinerAdapter comparando los id con el buscado.
     * Devolverá -1 cuando no encuentre la posición del elemento
     * @param adapter SpinerAdapter a recorrer
     * @param idToSearch long con el ID a buscar
     * @return int con la posición que ocupa el elemento en el adapter. -1 si
     * no se ha encontrado*/
    public static int getItemPosition(SpinnerAdapter adapter, long idToSearch) {
        int position = -1;

        for (int i = adapter.getCount() - 1; i >= 0; i--) {
            if (adapter.getItemId(i) == idToSearch) {
                position = i;
                break;
            }
        }

        return position;
    }

    /** Recorre un cursor creando objetos Máquina y anidándolos a un ArrayList
     * de máquinas.
     * @param cursorMaquinas Cursor con la query de máquinas deseada */
    public static ArrayList<Maquina> convertCursorToMaquinas(Cursor cursorMaquinas) {
        ArrayList<Maquina> list = new ArrayList<>();

        if (cursorMaquinas.moveToFirst()) {
            do {
                final long id;
                final String numeroSerie;
                final String adreca;
                final String poblacio;
                final String codiPostal;
                Date date;
                Client client;
                Zona zona;
                Tipus tipus;

                id = cursorMaquinas.getLong(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.MAQUINA_ID));
                numeroSerie = cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.MAQUINA_NUMERO_SERIE));
                adreca = cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.MAQUINA_ADRECA));
                poblacio = cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.MAQUINA_POBLACIO));
                codiPostal = cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.MAQUINA_CODI_POSTAL));
                try {
                    date = new Date(
                            cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.MAQUINA_ULTIMA_REVISIO)),
                            true
                    );
                }
                catch (Exception e) {
                    date = null;
                }
                try {
                    client = new Client(
                            cursorMaquinas.getLong(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.MAQUINA_CLIENT)),
                            cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.CLIENT_NOM)),
                            cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.CLIENT_COGNOMS)),
                            cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.CLIENT_EMAIL)),
                            cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.CLIENT_TELEFON))
                    );
                }
                catch (Exception e) {
                    client = null;
                }
                try {
                    zona = new Zona(
                            cursorMaquinas.getLong(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.MAQUINA_ZONA)),
                            cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow("zDescripcio"))
                    );
                }
                catch (Exception e) {
                    zona = null;
                }
                try {
                    tipus = new Tipus(
                            cursorMaquinas.getLong(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.MAQUINA_TIPUS)),
                            cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow("tDescripcio")),
                            cursorMaquinas.getString(cursorMaquinas.getColumnIndexOrThrow(BuidemHelper.TIPUS_COLOR))
                    );
                }
                catch (Exception e) {
                    tipus = null;
                }

                try {
                    Maquina maquina = new Maquina(
                            id,
                            adreca,
                            codiPostal,
                            poblacio,
                            numeroSerie,
                            date,
                            client,
                            zona,
                            tipus
                    );

                    list.add(maquina);
                }
                catch (Exception e) {
                    continue;
                }
            }
            while (cursorMaquinas.moveToNext());
        }

        return list;
    }
}
