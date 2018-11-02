package com.example.android.weatherapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ListView cityListView;
    ListViewAdapter mAdapter;
    ArrayList<HashMap<String,String>> weatherData = new ArrayList<HashMap<String, String>>();

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

        String locationEntered = "London";
        populateList(locationEntered);
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
                String locationEntered = cityName.getText().toString().trim().toLowerCase();
                if (locationEntered.matches("[ a-zA-Z\\-]+")) {
                    populateList(locationEntered);
                    dialog.dismiss();
                } else{
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_city_error_message), Toast.LENGTH_LONG).show();
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
        String[] weatherInformation = new String[2];
        try {
            weatherInformation = new GetWeatherData().execute(locationEntered).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HashMap<String, String> weatherDataForLocation = new HashMap<String, String>();
        weatherDataForLocation.put(getString(R.string.weather_data_temp_key), weatherInformation[0]);
        weatherDataForLocation.put(getString(R.string.weather_data_location_key), weatherInformation[1]);
        weatherData.add(weatherDataForLocation);

        mAdapter.notifyDataSetChanged();
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
