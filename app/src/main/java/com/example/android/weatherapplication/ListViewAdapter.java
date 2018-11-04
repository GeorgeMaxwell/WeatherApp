package com.example.android.weatherapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    ArrayList<LocationWeather> weatherForLocations = new ArrayList<>();

    public ListViewAdapter(ArrayList<LocationWeather> weatherDataToDisplay) {
        this.weatherForLocations = weatherDataToDisplay;
    }

    @Override
    public int getCount() {
        return this.weatherForLocations.size();
    }

    @Override
    public LocationWeather getItem(int position) {
        return this.weatherForLocations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        LocationWeather currentItem = (LocationWeather) getItem(position);

        // get the TextView for item name and item description
        TextView textViewLocation = (TextView)
                convertView.findViewById(android.R.id.text1);
        TextView textViewTempVal = (TextView)
                convertView.findViewById(android.R.id.text2);

        //sets the text for item name and item description from the current item object
        textViewLocation.setText(currentItem.getCityName());
        textViewTempVal.setText(currentItem.getTempText());

        return convertView;
    }
}