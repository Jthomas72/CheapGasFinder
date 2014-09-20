package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class newCarActivity extends Activity {

    String GET_YEARS_URL = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getYears";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            populateYearSpinner();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void populateYearSpinner() throws IOException, JSONException, URISyntaxException {
        Spinner spinner = (Spinner) findViewById(R.id.year_spinner);
        List<String> year_list = new ArrayList<String>();

        int min_year = 0;
        int max_year = 0;

        GetJsonAPI jsonAPI = new GetJsonAPI();

        Log.w("JSON_out",  jsonAPI.getJSONString(GET_YEARS_URL));
        min_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("min_year");
        max_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("max_year");

        for (int i = min_year; i <= max_year; i++) {
            year_list.add(Integer.toString(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, year_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void populateMakeSpinner() {

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
}
