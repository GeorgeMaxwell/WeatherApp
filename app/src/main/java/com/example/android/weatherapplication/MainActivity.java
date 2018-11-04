package com.example.android.weatherapplication;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
    ArrayList<HashMap<String,String>> weatherData = new ArrayList<HashMap<String, String>>();
    Set<String> citiesAdded = new HashSet<>();
    Boolean visible = true;

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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        cityListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        cityListView.setMultiChoiceModeListener(new  AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean  onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
                return false;
            }
            @Override
            public void  onDestroyActionMode(ActionMode mode) {
                // TODO  Auto-generated method stub
            }
            @Override
            public boolean  onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
                mode.getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }
            @Override
            public boolean  onActionItemClicked(final ActionMode mode, MenuItem item) {
                // TODO  Auto-generated method stub
                switch  (item.getItemId()) {
                    case R.id.select_all:
                        final int checkedCount  = weatherData.size();
                        // If item  is already selected or checked then remove or
                        // unchecked  and again select all
                        for (int i = 0; i <  checkedCount; i++) {
                            cityListView.setItemChecked(i,   true);
                            //  listviewadapter.toggleSelection(i);
                        }
                        // Set the  CAB title according to total checked items
                        // Calls  toggleSelection method from ListViewAdapter Class
                        // Count no.  of selected item and print it
                        mode.setTitle(checkedCount  + "  Selected");
                        return true;
                    case R.id.action_delete_item:
                        // Add  dialog for confirmation to delete selected item
                        // record.
                        AlertDialog.Builder  builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Do you  want to delete selected record(s)?");
                        AlertDialog alert =  builder.create();
                        alert.setIcon(R.id.action_delete_item);// dialog  Icon
                        alert.setTitle("Confirmation"); // dialog  Title
                        alert.show();
                        return true;
                    default:
                        return false;
                }
            }
            @Override

            public void  onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // TODO  Auto-generated method stub
                final int checkedCount  = cityListView.getCheckedItemCount();
                // Set the  CAB title according to total checked items
                mode.setTitle(checkedCount  + "  Selected");
                // Calls  toggleSelection method from ListViewAdapter Class
                //adapter.toggleSelection(position);
            }
        });




      /*  cityListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                removeCity(pos, id);
                return true;
            }
        });*/


        loadCities();
    }
    public void loadCities(){
        SharedPreferences prefs = getSharedPreferences("LIST_OF_CITIES", Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("CITY_VALUES", null);
        if(set !=null) {
            for (String locationEntered : set) {
                populateList(locationEntered);
            }
        }
        //getSharedPreferences("LIST_OF_CITIES",Context.MODE_PRIVATE).edit().clear().commit(); //to clear current preferences.
    }
    public void storeCities(){
        SharedPreferences prefs = getSharedPreferences("LIST_OF_CITIES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("CITY_VALUES", this.citiesAdded);
        editor.commit();
    }
    public  void removeCity(int pos, long id){
        //weatherData.get(pos);
        invalidateOptionsMenu();
        //deleteItem.setVisible(true);
        this.citiesAdded.remove(this.weatherData.get(pos).get(getString(R.string.weather_data_location_key)).toUpperCase());
        this.weatherData.remove(this.weatherData.get(pos));
        mAdapter.notifyDataSetChanged();
        // Inflate the menu; this adds items to the action bar if it is present.
        visible = true;
        invalidateOptionsMenu();
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
                if (locationEntered.matches("[ a-zA-Z\\-]+") && !citiesAdded.contains(locationEntered)) {
                    populateList(locationEntered);
                    dialog.dismiss();
                } else if (citiesAdded.contains(locationEntered)) {
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
        String[] weatherInformation = new String[2];
        try {
            weatherInformation = new GetWeatherData().execute(locationEntered, getString(R.string.OPENWEATHER_API_KEY)).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (weatherInformation != null){
        HashMap<String, String> weatherDataForLocation = new HashMap<String, String>();
        weatherDataForLocation.put(getString(R.string.weather_data_temp_key), weatherInformation[0]);
        weatherDataForLocation.put(getString(R.string.weather_data_location_key), weatherInformation[1]);
        weatherData.add(weatherDataForLocation);
        citiesAdded.add(weatherInformation[1].toUpperCase());

        storeCities();

        mAdapter.notifyDataSetChanged();
        }
        else
            Toast.makeText(getApplicationContext(), getString(R.string.city_not_found_error_message), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem deleteItem = menu.findItem(R.id.action_delete_item);

        if(visible == false) {
            deleteItem.setVisible(false);
        }
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
