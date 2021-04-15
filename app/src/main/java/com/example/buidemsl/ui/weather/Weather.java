package com.example.buidemsl.ui.weather;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.buidemsl.R;
import com.example.buidemsl.util.OpenWeatherMapApi;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Weather {

    private static ProgressDialog progressDialog;

    public static void openWeatherDialog(Context context, LatLng position, String language) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        getWeatherInfo(context, position, language);
    }

    private static void getWeatherInfo(Context context, LatLng position, String language) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0, 10);

        final String url = OpenWeatherMapApi.getUrlWeather(position.latitude, position.longitude, language);

        client.get(context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.setTitle(R.string.weather_dialog_getting_info_title);
                progressDialog.setMessage(context.getText(R.string.weather_dialog_getting_info_message));
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.hide();
            }
        });
    }

}
