package com.example.buidemsl.ui.weather;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.buidemsl.R;
import com.example.buidemsl.util.OpenWeatherMapApi;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Weather {

    private static ProgressDialog progressDialog;

    public static void openWeatherDialog(Fragment fragment, LatLng position, String language) {
        progressDialog = new ProgressDialog(fragment.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        getWeatherInfo(fragment, position, language);
    }

    private static void getWeatherInfo(Fragment fragment, LatLng position, String language) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0, 10);

        final String url = OpenWeatherMapApi.getUrlWeather(position.latitude, position.longitude, language);

        client.get(fragment.getContext(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.setTitle(R.string.weather_dialog_getting_info_title);
                progressDialog.setMessage(fragment.getText(R.string.weather_dialog_getting_info_message));
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.setTitle(R.string.weather_dialog_processing_response_title);
                progressDialog.setMessage(fragment.getText(R.string.weather_dialog_processing_response_message));

                try {
                    JSONObject jsonResponse = new JSONObject(new String(responseBody));
                    openAlertDialog(fragment, jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
            }
        });
    }

    /** Muestra un AlertDialog con la información del tiempo */
    private static void openAlertDialog(Fragment fragment, JSONObject jsonObject) throws JSONException {
        AlertDialog alert = new AlertDialog.Builder(fragment.getContext()).create();

        View dialogView = fragment.getLayoutInflater().inflate(R.layout.weather_dialog, null);
        procesarJSON(dialogView, jsonObject);

        alert.setTitle(R.string.weather_dialog_title);
        alert.setView(dialogView);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, fragment.getText(R.string.default_alert_accept), (dialog, which) -> {
            // Nothing
        });

        alert.show();
    }

    /** Procesa la información del JSON devuelto por OpenWeatherMap
     * editando la vista y sus elementos */
    private static void procesarJSON(View view, JSONObject json) throws JSONException {
        // View items
        ImageView img = (ImageView) view.findViewById(R.id.img_weather_icon);
        TextView txtCity = (TextView) view.findViewById(R.id.txt_weather_city_name);
        TextView txtWeatherDescription = (TextView) view.findViewById(R.id.txt_weather_description);
        TextView txtWeatherTemp = (TextView) view.findViewById(R.id.txt_weather_temp);

        // Es recupera el nom de la ubicació treduit des del JSON
        String string = json.getString("name");
        txtCity.setText(string);

        // Es recupera l'array amb la informació del clima. Es pasa a JSONObject
        JSONArray jsonArray = (JSONArray) json.get("weather");
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

        // Es recupera la icona
        String icon = jsonObject.getString("icon");
        icon = OpenWeatherMapApi.getUrlIcon(icon);
        Picasso.get().load(icon).into(img);

        // Es recupera la informació de la descripció i s'aplica al layout
        string = jsonObject.getString("description");
        string = string.substring(0, 1).toUpperCase() + string.substring(1);
        txtWeatherDescription.setText(string);

        // Es recupera la temperatura
        jsonObject = (JSONObject) json.get("main");
        string = String.valueOf(jsonObject.getDouble("temp"));
        string = string + "ºC";
        txtWeatherTemp.setText(string);
    }
}
