package com.example.buidemsl.util;

import android.widget.SpinnerAdapter;

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
}
