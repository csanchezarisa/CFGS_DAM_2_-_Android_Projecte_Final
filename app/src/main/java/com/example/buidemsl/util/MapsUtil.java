package com.example.buidemsl.util;

import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;

import com.example.buidemsl.util.objects.models.Maquina;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class MapsUtil {

    /** Crea un objeto LatLng con la dirección de la máquina pasada
     * por parámetros
     * @param maquina objeto Maquina al cual se le quiere conocer
     *                 la ubicación
     * @return posición con un objeto LatLng */
    public static LatLng getLatLngFromMaquinas(@NonNull Maquina maquina, @NonNull Geocoder coder) {

        List<Address> adress;
        LatLng position;

        try {
            adress = coder.getFromLocationName(maquina.getFullDirection(), 5);
            Address location = adress.get(0);

            position = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e) {
            position = null;
        }

        return position;
    }
}
