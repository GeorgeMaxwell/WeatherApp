package com.example.android.weatherapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> hashMap = new ArrayList<>();

    public ListViewAdapter(ArrayList<HashMap<String, String>> weatherDataToDisplay) {
        this.hashMap = weatherDataToDisplay;
    }

    @Override
    public int getCount() {
        return this.hashMap.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return this.hashMap.get(position);
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

        HashMap<String, String> currentItem = (HashMap<String, String>) getItem(position);

        // get the TextView for item name and item description
        TextView textViewLocation = (TextView)
                convertView.findViewById(android.R.id.text1);
        TextView textViewTempVal = (TextView)
                convertView.findViewById(android.R.id.text2);

        //sets the text for item name and item description from the current item object
        textViewLocation.setText(currentItem.get("Location"));
        textViewTempVal.setText(currentItem.get("Temperature"));

        return convertView;
    }
}