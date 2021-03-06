package com.example.android.weatherapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ListView cityListView;
    ListViewAdapter mAdapter;
    ArrayList<LocationWeather> weatherData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addCity();
            }
        });

        cityListView = findViewById(R.id.city_listview);
        mAdapter = new ListViewAdapter(weatherData);
        cityListView.setAdapter(mAdapter);

        cityListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                removeCity(pos, id);
                return true;
            }
        });


        loadCities();
    }
    public void loadCities(){
        SharedPreferences prefs = getSharedPreferences("LIST_OF_CITIES", Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("CITY_VALUES", null);
        if(set != null) {
            for (String locationEntered : set) {
                populateList(locationEntered);
            }
        }
        //getSharedPreferences("LIST_OF_CITIES",Context.MODE_PRIVATE).edit().clear().commit(); //to clear current preferences.
    }
    public void storeCities(){
        SharedPreferences prefs = getSharedPreferences("LIST_OF_CITIES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("CITY_VALUES", new HashSet<String>(getLocations()));
        editor.commit();
    }
    public  void removeCity(int pos, long id){
        this.weatherData.remove(this.weatherData.get(pos));
        mAdapter.notifyDataSetChanged();
        storeCities();
    }

    public void addCity (){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_city_dialog);
        dialog.setTitle("Add City");
        dialog.setCancelable(true);

        final EditText cityName = (EditText) dialog.findViewById(R.id.add_city_edit_text);
        cityName.requestFocus();

        Button addCityBtn = (Button) dialog.findViewById(R.id.add_city_submit_btn);
        Button cancelCityBtn = (Button) dialog.findViewById(R.id.add_city_cancel_btn);

        addCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationEntered = cityName.getText().toString().trim().toUpperCase();
                if (locationEntered.matches("[ a-zA-Z\\-]+") && !getLocations().contains(locationEntered)) {
                    populateList(locationEntered);
                    dialog.dismiss();
                } else if (getLocations().contains(locationEntered)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.duplicate_city_error_message), Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_character_error_message), Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void populateList(String locationEntered){
        LocationWeather weatherForLocationObj = null;
        try {
            weatherForLocationObj = new GetWeatherData().execute(locationEntered, getString(R.string.OPENWEATHER_API_KEY)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (weatherForLocationObj != null) {
            this.weatherData.add(weatherForLocationObj);
            storeCities();
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.city_not_found_error_message), Toast.LENGTH_LONG).show();
        }
    }

    ArrayList<String> getLocations() {
        ArrayList<String> locations = new ArrayList<>();
        for (LocationWeather locationWeatherObj : this.weatherData) {
            locations.add(locationWeatherObj.getCityName().toUpperCase());
        }

        return locations;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
