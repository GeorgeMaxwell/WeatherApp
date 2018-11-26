package com.example.android.weatherapplication;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    static String PARAM_QUERY = "q";
    static String PARAM_UNITS = "units";
    static String units = "metric";

    //Creates the URL to make api requests with
    public static URL BuildURL(String location, String apiKey){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, location)
                .appendQueryParameter(PARAM_UNITS, units)
                .appendQueryParameter("APPID", apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
