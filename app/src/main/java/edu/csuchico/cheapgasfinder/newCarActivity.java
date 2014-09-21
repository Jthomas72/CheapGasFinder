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
    String GET_MAKES_URL = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getMakes&sold_in_us=1&year=";

    Spinner yearSpinner = null;
    List<String> yearList = null;
    int selectedYear = 0;

    Spinner makeSpinner = null;
    List<String> makeList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        yearList = new ArrayList<String>();

        try {
            populateYearSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateYearSpinner() throws IOException, JSONException, URISyntaxException {
        int min_year;
        int max_year;

        GetJsonAPI jsonAPI = new GetJsonAPI();
        String jsonString = jsonAPI.getJSONString(GET_YEARS_URL);
        Log.w("JSON_out", jsonString);
        min_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("min_year");
        max_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("max_year");

        for (int i = min_year; i <= max_year; i++) {
            this.yearList.add(Integer.toString(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, this.yearList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.yearSpinner.setAdapter(dataAdapter);
        yearSpinner.setOnItemSelectedListener(this);
    }

    public void populateMakeSpinner(int year) throws IOException, URISyntaxException, JSONException {
        makeSpinner = (Spinner) findViewById(R.id.make_spinner);
        makeList = new ArrayList<String>();

        GetJsonAPI jsonAPI = new GetJsonAPI();
        String jsonString =  jsonAPI.getJSONString(GET_MAKES_URL + Integer.toString(year));
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            populateMakeSpinner(Integer.parseInt((String) adapterView.getItemAtPosition(i)));
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}