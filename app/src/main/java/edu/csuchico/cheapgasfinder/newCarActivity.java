package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class newCarActivity extends Activity implements AdapterView.OnItemSelectedListener {

    String GET_YEARS_URL      = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getYears";
    String GET_MAKES_URL      = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getMakes&sold_in_us=1";
    String GET_MODELS_URL     = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModels&sold_in_us=1";
    String GET_TRIMS_URL      = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getTrims";
    String GET_MODEL_DATA_URL = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModel";

    Spinner yearSpinner, makeSpinner, modelSpinner, trimSpinner;
    TextView nameTextView;
    Map<String, Integer> trimMap = new HashMap<String, Integer>();

    int year;
    String make, model, trim;
    Double mpg, tankSize;

    String PREFS_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        nameTextView = (TextView) findViewById(R.id.nickname_text);
        yearSpinner  = (Spinner) findViewById(R.id.year_spinner);
        makeSpinner  = (Spinner) findViewById(R.id.make_spinner);
        modelSpinner = (Spinner) findViewById(R.id.model_spinner);
        trimSpinner  = (Spinner) findViewById(R.id.trim_spinner);

        try {
            populateYearSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }

        yearSpinner.setOnItemSelectedListener(this);
        makeSpinner.setOnItemSelectedListener(this);
        modelSpinner.setOnItemSelectedListener(this);
        trimSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        year = Integer.parseInt((String) yearSpinner.getSelectedItem());
        make = (String) makeSpinner.getSelectedItem();
        model = (String) modelSpinner.getSelectedItem();
        trim = (String) trimSpinner.getSelectedItem();

        switch (adapterView.getId()) {
            case R.id.year_spinner:
                try {
                    populateMakeSpinner(year);
                    Log.d("year_value", Integer.toString(year));
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case R.id.make_spinner:
                try {
                    populateModelSpinner(make, year);
                    Log.d("make_value", make);
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case R.id.model_spinner:
                try {
                    populateTrimSpinner(model, make, year);
                    Log.d("model_value", model);
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case R.id.trim_spinner:
                try {
                    int modelID = trimMap.get(trim);
                    updateCarInfo(modelID);
                } catch (Exception e) { e.printStackTrace(); }
                break;
        }

    }

    public void populateYearSpinner() throws IOException, JSONException, URISyntaxException {
        List<String> yearList = new ArrayList<String>();
        String jsonURL = GET_YEARS_URL;
        Log.d("JSON_URL", jsonURL);
        GetJsonAPI jsonAPI = new GetJsonAPI(jsonURL);
        Log.w("JSON_out", jsonAPI.getJSONString());

        int min_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("min_year");
        int max_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("max_year");

        for (int i = max_year; i >= min_year; i--) {
            yearList.add(Integer.toString(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, yearList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(dataAdapter);
    }

    public void populateMakeSpinner(int year) throws IOException, URISyntaxException, JSONException {
        List<String> makeList = new ArrayList<String>();
        String jsonURL = GET_MAKES_URL + "&year=" + Integer.toString(year);
        Log.d("JSON_URL", jsonURL);
        GetJsonAPI jsonAPI = new GetJsonAPI(jsonURL);
        String jsonString =  jsonAPI.getJSONString();
        Log.w("JSON_out", jsonString);
        JSONArray makesArray = jsonAPI.parseJSONObject().getJSONArray("Makes");

        for (int i = 0; i < makesArray.length(); i++) {
            makeList.add(makesArray.getJSONObject(i).getString("make_display"));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, makeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        makeSpinner.setAdapter(dataAdapter);
    }

    public void populateModelSpinner(String make, int year) throws IOException, URISyntaxException, JSONException {
        List<String> modelList = new ArrayList<String>();
        String jsonURL = GET_MODELS_URL + "&year=" + Integer.toString(year) + "&make=" + make;
        Log.d("JSON_URL", jsonURL);
        GetJsonAPI jsonAPI = new GetJsonAPI(jsonURL);
        Log.d("JSON_out", jsonAPI.getJSONString());
        JSONArray modelsArray = jsonAPI.parseJSONObject().getJSONArray("Models");

        for (int i = 0; i < modelsArray.length(); i++) {
            modelList.add(modelsArray.getJSONObject(i).getString("model_name"));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, modelList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(dataAdapter);
    }

    private void populateTrimSpinner(String model, String make, int year) throws IOException, URISyntaxException, JSONException {
        trimMap = new HashMap<String, Integer>();
        String jsonURL = GET_TRIMS_URL + "&year=" + Integer.toString(year) + "&make=" + make + "&model=" + model;
        GetJsonAPI jsonAPI = new GetJsonAPI(jsonURL);
        Log.d("JSON_URL", jsonURL);
        Log.d("JSON_out", jsonAPI.getJSONString());
        JSONArray trimsArray = jsonAPI.parseJSONObject().getJSONArray("Trims");

        for (int i = 0; i < trimsArray.length(); i++ ) {
            String trimName = trimsArray.getJSONObject(i).getString("model_trim");
            if (trimName.isEmpty()) trimName = "None";
            int trimID = trimsArray.getJSONObject(i).getInt("model_id");
            trimMap.put(trimName, trimID);
        }

        List<String> trimList = new ArrayList<String>(trimMap.keySet());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, trimList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trimSpinner.setAdapter(dataAdapter);


    }

    public void updateCarInfo(int modelID) throws IOException, URISyntaxException, JSONException {
        TextView textView = (TextView) findViewById(R.id.car_info);
        String jsonURL = GET_MODEL_DATA_URL + "&model=" + modelID;
        Log.d("JSON_URL", jsonURL);
        GetJsonAPI jsonAPI = new GetJsonAPI(jsonURL);
        Log.d("JSON_out", jsonAPI.getJSONString());

        JSONObject carInfo = jsonAPI.parseJSONArray().getJSONObject(0);
        if (!carInfo.isNull("model_mpg_city")) {
            mpg = carInfo.getDouble("model_mpg_city");
        } else {
            mpg =  null;
        }
        if (!carInfo.isNull("model_fuel_cap_g")) {
            tankSize = carInfo.getDouble("model_fuel_cap_g");
        } else {
            tankSize =  null;
        }


        textView.setText("MPG: " + ((mpg != null) ? mpg : "Not available") +
                "\nTank size: " + ((tankSize != null) ? tankSize : "Not available"));
    }

    public void saveCar(View view) {
        String name = nameTextView.getText().toString();
        Car car = new Car(name, year, make, model, trim, mpg, tankSize);
        String carJson = car.toJSON();
        Log.d("JSON_car", carJson);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> cars = prefs.getStringSet("cars", null);
        if (cars == null) {
            cars = new HashSet<String>();
        }
        cars.add(carJson);

        editor.putStringSet("cars", cars);
        editor.commit();

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
