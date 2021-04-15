package com.example.buidemsl.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buidemsl.R;
import com.example.buidemsl.models.BuidemHelper;
import com.example.buidemsl.models.datasource.MainDatasource;
import com.example.buidemsl.ui.weather.Weather;
import com.example.buidemsl.util.CursorsUtil;
import com.example.buidemsl.util.MapsUtil;
import com.example.buidemsl.util.objects.models.Maquina;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private MainDatasource datasource;
    private String columnName = "";
    private long id = -1;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            // Se crea un array customFilter para hacer la query de Maquinas
            // Si hay definido un id y un columnName, entonces se crea
            String[] customFilter = null;
            if (id >= 0 && columnName.length() > 0)
                customFilter = new String[]{columnName, String.valueOf(id)};

            // Se ejecuta la query y se pasa al método convertCursorToMaquinas própio
            // Para recuperar un ArrayList de máquinas
            ArrayList<Maquina> maquinas = CursorsUtil.convertCursorToMaquinas(datasource.getMaquinas(null, null, customFilter));

            // Se crea un Geocoder para poder encontrar la posición de las direcciones
            // de las máquinas.
            Geocoder coder = new Geocoder(getContext());

            // Objeto LatLngBounds que se encargará de situar
            // la cámara en medio de los puntos y con el zoom correcto
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            boolean setZoom = false;

            // Se recorren las máquinas extrayendo la posición
            for (Maquina maquina : maquinas) {

                // Se consigue la posición de la máquina
                LatLng position = MapsUtil.getLatLngFromMaquinas(maquina, coder);

                // ¿Se ha conseguido correctamente la posición?
                if (position != null) {

                    // Al creador de los bordes de posicionamiento se le
                    // añade la posición de la máquina actual
                    builder.include(position);
                    setZoom = true;

                    // Se crea el marcador
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(position)
                            .title(maquina.getNumeroSerie())
                            .snippet(maquina.getFullDirection());

                    try {
                        // Para cambiar el color del marcador Default hace falta el HUE del color
                        // Se ejecuta el método de ColorUtils que en la posición 0 del array deja
                        // calculado el HUE del color del tipo de la máquina y se asigna
                        float[] hueColor = new float[3];
                        ColorUtils.colorToHSL(Color.parseColor(maquina.getTipus().getColor()), hueColor);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hueColor[0]));
                    }
                    catch (Exception ignored) {

                    }

                    // Se añade el marcador en el mapa
                    googleMap.addMarker(markerOptions);

                }
            }

            // Se libera la memoria
            maquinas.clear();
            System.gc();

            // Se configuran los controles del mapa
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
            }
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // ¿Hay alguna marca con la que poder hacer zoom?
            if (setZoom) {
                // Se crean los bordes desde el builder con todas las posiciones
                // de las máquinas a mostrar
                LatLngBounds bounds = builder.build();

                // Se crea un movimiento de la cámara a partir de los bordes creados
                // y se mueve la cámara
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 5);
                googleMap.moveCamera(cameraUpdate);
                googleMap.animateCamera(cameraUpdate);
            }

            // Listener al clicar encima del popup del marker
            // abrirá el fragment para editar la maquina
            googleMap.setOnInfoWindowClickListener(marker -> {
                Weather.openWeatherDialog(getParentFragment(), marker.getPosition(), getString(R.string.app_lang));
            });

            googleMap.setOnInfoWindowLongClickListener(marker -> {
                Cursor maquina = datasource.getMaquina(marker.getTitle());
                if (maquina.moveToFirst()) {
                    final long idMaquina = maquina.getLong(maquina.getColumnIndexOrThrow(BuidemHelper.MAQUINA_ID));
                    openMaquinaFragment(idMaquina);
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        datasource = new MainDatasource(getContext());

        // ¿Se han pasado argumentos al mapa?
        if (getArguments() != null) {
            id = getArguments().getLong("id");
            columnName = getArguments().getString("column_name");
        }
        else {
            id = -1;
            columnName = "";
        }

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        solicitarPermis();
    }

    /** Revisa si el permiso de la ubicación está permitido. Sino, lo pide */
    public void solicitarPermis() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.fragment_maps_alert_permissions_title))
                    .setMessage(getString(R.string.fragment_maps_alert_permissions_message))
                    .setPositiveButton(R.string.default_alert_accept, (dialog, which) -> ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION_PERMISSION
                    ))
                    .setNegativeButton(R.string.default_alert_cancel, (dialog, which) -> {
                        // Nothing
                    })
                    .show();
        }
        else {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
        }
    }

    /** Permite abrir el fragment de edición de una máquina
     * @param id long con el ID de la máquina a abrir */
    private void openMaquinaFragment(long id) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        NavHostFragment.findNavController(this).navigate(R.id.action_mapsFragment_to_machineManagmentFragment, bundle);
    }
}