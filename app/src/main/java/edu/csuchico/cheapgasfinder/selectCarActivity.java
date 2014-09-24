package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class selectCarActivity extends Activity {

    final static String PREFS_NAME = "UserPrefs";
    ListView carsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);

        carsListView = (ListView) findViewById(R.id.car_list);
        ArrayList<String> carsArray = getCars();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carsArray);
        carsListView.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_car, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addCar(View view) {
        Intent intent = new Intent(selectCarActivity.this, newCarActivity.class);
        startActivity(intent);
    }

    public ArrayList<String> getCars () {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        Set<String> cars = prefs.getStringSet("cars", null);
        return new ArrayList<String>(cars);
    }
}
