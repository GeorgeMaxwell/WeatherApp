package com.example.android.weatherapplication;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GetWeatherData extends  AsyncTask<String, Void, LocationWeather>{
    @Override
    protected LocationWeather doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(NetworkUtils.BuildURL(strings[0], strings[1]))
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()){
                return null;
            }
            String jsonData = response.body().string();
            return new LocationWeather(new JSONObject(jsonData).getString("name"), new JSONObject(jsonData).getJSONObject("main").getDouble("temp"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
