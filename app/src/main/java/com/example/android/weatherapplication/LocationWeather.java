package com.example.android.weatherapplication;

public class LocationWeather {
    String name;
    double temperature;

    public LocationWeather(String cityName, double cityTemp) {
        this.name = cityName;
        this.temperature = cityTemp;
    }

    String getCityName() {
        return this.name;
    }

    double getTemperature() {
        return this.temperature;
    }

    String getTempText() {
        return this.temperature + "°C";
    }
}
