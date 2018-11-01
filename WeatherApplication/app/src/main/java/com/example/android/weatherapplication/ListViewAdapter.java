package com.example.android.weatherapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {
    private final HashMap mWeather;
    //private final ArrayList mWeather;
    int counter =0;


    public ListViewAdapter(HashMap<String, String> map) {
        this.mWeather = map;
        /*mWeather = new ArrayList();
        mWeather.addAll(map.entrySet());*/

    }

    @Override
    public int getCount() {
        return mWeather.size();
    }

    @Override
    public HashMap.Entry<String, String> getItem(int position) {
        return (HashMap.Entry) mWeather.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        } else {
            result = convertView;
        }

        HashMap.Entry<String, String> item = getItem(position);
        counter++;
        // TODO replace findViewById by ViewHolder
      ((TextView) result.findViewById(android.R.id.text1)).setText(item.getValue());
        ((TextView) result.findViewById(android.R.id.text2)).setText(item.getValue());

        return result;
    }
}