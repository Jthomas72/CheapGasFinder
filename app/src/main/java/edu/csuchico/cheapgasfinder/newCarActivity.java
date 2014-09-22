package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class newCarActivity extends Activity implements AdapterView.OnItemSelectedListener {

    String GET_YEARS_URL = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getYears";
    String GET_MAKES_URL = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getMakes&sold_in_us=1";
    String GET_MODELS_URL = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModels&sold_in_us=1";

    Spinner yearSpinner, makeSpinner, modelSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        makeSpinner = (Spinner) findViewById(R.id.make_spinner);

        try {
            populateYearSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }

        yearSpinner.setOnItemSelectedListener(this);
        makeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int year = Integer.parseInt((String) yearSpinner.getSelectedItem());
        String make = (String) makeSpinner.getSelectedItem();

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

        }

    }

    public void populateYearSpinner() throws IOException, JSONException, URISyntaxException {
        List<String> yearList = new ArrayList<String>();

        int min_year;
        int max_year;

        GetJsonAPI jsonAPI = new GetJsonAPI();
        String jsonString = jsonAPI.getJSONString(GET_YEARS_URL);
        Log.w("JSON_out", jsonString);
        min_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("min_year");
        max_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("max_year");

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

        GetJsonAPI jsonAPI = new GetJsonAPI();
        String jsonString =  jsonAPI.getJSONString(GET_MAKES_URL + "&year=" + Integer.toString(year));
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

    public void populateModelSpinner(String make_id, int year) throws IOException, URISyntaxException, JSONException {
        modelSpinner = (Spinner) findViewById(R.id.model_spinner);
        List<String> modelList = new ArrayList<String>();

        GetJsonAPI jsonAPI = new GetJsonAPI();
        String jsonURL = GET_MODELS_URL + "&year=" + Integer.toString(year) + "&make=" + make_id;
        Log.d("JSON_URL", jsonURL);
        String jsonString =  jsonAPI.getJSONString(jsonURL);
        Log.d("JSON_out", jsonString);
        JSONArray modelsArray = jsonAPI.parseJSONObject().getJSONArray("Models");

        for (int i = 0; i < modelsArray.length(); i++) {
            modelList.add(modelsArray.getJSONObject(i).getString("model_name"));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, modelList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(dataAdapter);
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