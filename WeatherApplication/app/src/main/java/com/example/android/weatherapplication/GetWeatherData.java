package com.example.android.weatherapplication;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class GetWeatherData extends  AsyncTask<String,Void,String>{
    String location = MainActivity.location;
    @Override
    protected String doInBackground(String... strings) {
        String temperature = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(NetworkUtils.BuildURL("london"))
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            temperature = new JSONObject(jsonData).getJSONObject("main").getString("temp") + "°C";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temperature;
    }
}
