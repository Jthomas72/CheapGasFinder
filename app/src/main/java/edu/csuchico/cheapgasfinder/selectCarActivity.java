package edu.csuchico.cheapgasfinder;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Handles events for activity_select_car.xml
 *
 */
public class selectCarActivity extends ListActivity {
    Cars cars;
    ListView carsListView;

    /**
     * Sets up the the elements of the select car view
     *
     * @param savedInstanceState This parameter is passed when the function gets called
     *                           automatically
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);
        cars = new Cars(this);
    }

    /**
     * Called when the activity will start interacting with the user.
     *
     */
    @Override
    public void onResume() {
        super.onResume();

        carsListView = (ListView) findViewById(android.R.id.list);
        cars = new Cars(this);

        // only attempt to load cars if the list is non-empty
        if (!cars.getCarsNames().isEmpty()) {
            ArrayAdapter<String> arrayAdapter
                    = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cars.getCarsNames());
            carsListView.setAdapter(arrayAdapter);
        }
    }

    /**
     * Called when an item in the list is selected.
     *
     * @param l The ListView where the click happened
     * @param v The view that was clicked within the ListView
     * @param position The position of the view in the list
     * @param id The row id of the item that was clicked
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("Car_selected", cars.getCars().get(position).getName());
        Intent intent = new Intent(selectCarActivity.this, CreateRouteActivity.class);
        intent.putExtra("car_json", cars.getCarsJSON().get(position));
        startActivity(intent);
    }

    /**
     * Starts newCarActivity
     *
     * @param view The view where this was called from, passed automatically when this function
     *             is called from the onClick event in select_car_activity.xml
     */
    public void addCar(View view) {
        Intent intent = new Intent(selectCarActivity.this, newCarActivity.class);
        startActivity(intent);
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
