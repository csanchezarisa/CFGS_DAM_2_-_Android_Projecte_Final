package com.example.buidemsl.ui.weather;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.buidemsl.R;
import com.example.buidemsl.util.OpenWeatherMapApi;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

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

                progressDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.hide();
            }
        });
    }

    /** Muestra un AlertDialog con la información del tiempo */
    private static void openAlertDialog(Fragment fragment, JSONObject jsonObject) {
        AlertDialog alert = new AlertDialog.Builder(fragment.getContext()).create();

        View dialogView = fragment.getLayoutInflater().inflate(R.layout.weather_dialog, null);

        alert.setTitle(R.string.weather_dialog_title);
        alert.setView(dialogView);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, fragment.getText(R.string.default_alert_accept), (dialog, which) -> {
            // Nothing
        });

        alert.show();
    }
}
