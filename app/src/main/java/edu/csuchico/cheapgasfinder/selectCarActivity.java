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

public class selectCarActivity extends ListActivity {
    Cars cars;
    ListView carsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);
        cars = new Cars(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        carsListView = (ListView) findViewById(android.R.id.list);
        cars = new Cars(this);

        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cars.getCarsNames());
        carsListView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("Car_selected", cars.getCars().get(position).getName());
        Intent intent = new Intent(selectCarActivity.this, selectDestinationActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, cars.getCarsJSON().get(position));
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addCar(View view) {
        Intent intent = new Intent(selectCarActivity.this, newCarActivity.class);
        startActivity(intent);
    }
}
