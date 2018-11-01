package com.example.android.weatherapplication;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {
    private final ArrayList mWeather;

    public ListViewAdapter(Map<String, String> map) {
        mWeather = new ArrayList();
        mWeather.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mWeather.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mWeather.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_adapter_item, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, String> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(android.R.id.text1)).setText(item.getKey());
        ((TextView) result.findViewById(android.R.id.text2)).setText(item.getValue());

        return result;
    }
}