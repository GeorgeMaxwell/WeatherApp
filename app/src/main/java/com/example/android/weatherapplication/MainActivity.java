package com.example.android.weatherapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ListView cityListView;
    static String temperature;
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
        String locationEntered = "london";
        populateList(locationEntered);
    }
    public void addCity (){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.editbox_add_city, null);
        dialogBuilder.setView(dialogView);

        final EditText cityName = (EditText) dialogView.findViewById(R.id.edit_add_city);

        dialogBuilder.setTitle("Custom dialog");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String locationEntered = cityName.getText().toString();
                populateList(locationEntered);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
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
        weatherDataForLocation.put("Temperature", weatherInformation[0]);
        weatherDataForLocation.put("Location", weatherInformation[1]);
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
