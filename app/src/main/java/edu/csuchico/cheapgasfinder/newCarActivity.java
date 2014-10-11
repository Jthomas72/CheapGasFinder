package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Handles events for activity_new_car.xml
 *
 */
public class newCarActivity extends Activity implements AdapterView.OnItemSelectedListener {
    Spinner yearSpinner, makeSpinner, modelSpinner, trimSpinner;
    TextView nameTextView, textView;

    int year;
    String make, model, trim;

    CarQueryAPI carQuery;
    Map<String, Integer> trimMap;
    Car newCar;
    Cars cars;

    /**
     * Sets up the elements of the new car view
     *
     * @param savedInstanceState This parameter is passed when the function gets called
     *                           automatically.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);

        /*
         Allow networking to run in the main thread. This needs to be changed so networking
         runs in its own thread, or the UI will lock up while waiting on the network
        */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set up variables for accessing the elements of the view
        nameTextView = (TextView) findViewById(R.id.nickname_text);
        yearSpinner  = (Spinner) findViewById(R.id.year_spinner);
        makeSpinner  = (Spinner) findViewById(R.id.make_spinner);
        modelSpinner = (Spinner) findViewById(R.id.model_spinner);
        trimSpinner  = (Spinner) findViewById(R.id.trim_spinner);
        textView     = (TextView) findViewById(R.id.car_info);

        // Create a blank car object and object for accessing CarQueryAPI
        newCar = new Car();
        cars = new Cars(this);
        carQuery =  new CarQueryAPI();

        // Listen for changes to the spinners
        yearSpinner.setOnItemSelectedListener(this);
        makeSpinner.setOnItemSelectedListener(this);
        modelSpinner.setOnItemSelectedListener(this);
        trimSpinner.setOnItemSelectedListener(this);

        // Start by populating the year spinner
        try {
            populateYearSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * when something is selected, a spinner in this case, this function is called.
     *
     * @param adapterView The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param index The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long id) {
        year = Integer.parseInt((String) yearSpinner.getSelectedItem());
        make = (String) makeSpinner.getSelectedItem();
        model = (String) modelSpinner.getSelectedItem();
        trim = (String) trimSpinner.getSelectedItem();

        // Take action depending on what was clicked
        switch (adapterView.getId()) {
            case R.id.year_spinner:
                try {
                    populateMakeSpinner(year);
                    newCar.setYear(year);
                    Log.d("year_value", Integer.toString(year));
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case R.id.make_spinner:
                try {
                    populateModelSpinner(make, year);
                    newCar.setMake(make);
                    Log.d("make_value", make);
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case R.id.model_spinner:
                try {
                    populateTrimSpinner(model, make, year);
                    newCar.setModel(model);
                    Log.d("model_value", model);
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case R.id.trim_spinner:
                try {
                    int modelID = trimMap.get(trim);
                    newCar.setTrim(trim);
                    updateCarInfo(modelID);
                } catch (Exception e) { e.printStackTrace(); }
                break;
        }
    }

    /**
     * Adds available years to the year spinner from CarQueryAPI
     *
     * @throws IOException
     * @throws JSONException
     * @throws URISyntaxException
     */
    public void populateYearSpinner()
            throws IOException, JSONException, URISyntaxException {
        ArrayList<String> yearList = carQuery.getYears();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, yearList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(dataAdapter);
    }

    /**
     * Adds makes to the make spinner from CarQueryAPI
     *
     * @param year The year to get models for
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    public void populateMakeSpinner(int year)
            throws IOException, URISyntaxException, JSONException {
        ArrayList<String> makeList = carQuery.getMakes(year);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, makeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        makeSpinner.setAdapter(dataAdapter);
    }

    /**
     * Adds models to the spinner from CarQueryAPI
     *
     * @param make The make to get models for
     * @param year The year to get models for
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    public void populateModelSpinner(String make, int year)
            throws IOException, URISyntaxException, JSONException {
        ArrayList<String> modelList = carQuery.getModels(make, year);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, modelList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(dataAdapter);
    }

    /**
     * Add makes to the spinner from CarQueryAPI
     *
     * @param model The model to get spinners for
     * @param make The make to get models for
     * @param year The year to get models for
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    private void populateTrimSpinner(String model, String make, int year)
            throws IOException, URISyntaxException, JSONException {
        trimMap = carQuery.getTrims(model, make, year);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new ArrayList<String>(trimMap.keySet()));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trimSpinner.setAdapter(dataAdapter);
    }

    /**
     * Adds MPG, fuel type, tank size information to the car info text box
     * @param modelID The model ID to get information from. The model id comes
     *                from CarQueryAPI when the trims are fetched.
     *
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    public void updateCarInfo(int modelID) throws IOException, URISyntaxException, JSONException {
        Map<String, String> carInfo = carQuery.getCarInfo(modelID);
        newCar.setTankSize(Double.parseDouble(carInfo.get("mpg")));
        newCar.setMpg(Double.parseDouble(carInfo.get("tankSize")));
        newCar.setFuelType(carInfo.get("fuelType"));
        TextView textView = (TextView) findViewById(R.id.car_info);
        textView.setText("MPG: " + newCar.getMpg()
                + "\nTank Size: " + newCar.getTankSize()
                + "\nFuel type: " + newCar.getFuelType());
    }

    /**
     * When the save car button is clicked, this function is called.
     *
     * @param view Automatically passed when this function is called
     */
    public void saveCar(View view) {
        String name = nameTextView.getText().toString();
        newCar.setName(name);
        cars.save(newCar);
        Intent intent = new Intent(newCarActivity.this, selectCarActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_car, menu);

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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
