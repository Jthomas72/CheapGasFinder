package edu.csuchico.cheapgasfinder;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides access to CarQueryAPI (http://www.carqueryapi.com/).
 * The API returns lists of years, makes, models, and trims, and
 * gives information like MPG and tank size of a given car.
 *
 */
public class CarQueryAPI {

    final static private String GET_YEARS_URL      = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getYears";
    final static private String GET_MAKES_URL      = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getMakes&sold_in_us=1";
    final static private String GET_MODELS_URL     = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModels&sold_in_us=1";
    final static private String GET_TRIMS_URL      = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getTrims";
    final static private String GET_MODEL_DATA_URL = "http://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModel";

    /**
     * @return ArrayList containing all possible years of cars
     * @throws IOException
     * @throws JSONException
     */
    public ArrayList<String> getYears() throws IOException, JSONException {
        ArrayList<String> yearList = new ArrayList<String>();
        String jsonURL = GET_YEARS_URL;
        Log.d("JSON_URL", jsonURL);
        GetJson jsonAPI = new GetJson(jsonURL);
        Log.w("JSON_out", jsonAPI.getJSONString());

        int min_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("min_year");
        int max_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("max_year");

        for (int i = max_year; i >= min_year; i--) {
            yearList.add(Integer.toString(i));
        }

        return yearList;
    }

    /**
     * @param year The year to get models for
     * @return An ArrayList of possible models for a given year
     * @throws JSONException
     * @throws IOException
     */
    public ArrayList<String> getMakes(int year) throws JSONException, IOException {
        ArrayList<String> makeList = new ArrayList<String>();
        String jsonURL = GET_MAKES_URL + "&year=" + Integer.toString(year);
        Log.d("JSON_URL", jsonURL);
        GetJson jsonAPI = new GetJson(jsonURL);
        Log.w("JSON_out", jsonAPI.getJSONString());
        JSONArray makesArray = jsonAPI.parseJSONObject().getJSONArray("Makes");

        for (int i = 0; i < makesArray.length(); i++) {
            makeList.add(makesArray.getJSONObject(i).getString("make_display"));
        }

        return makeList;
    }

    /**
     * @param make The make to get models for
     * @param year The year to get models for
     * @return An ArrayList of possible models for given year and makes
     * @throws IOException
     * @throws JSONException
     */
    public ArrayList<String> getModels(String make, int year) throws IOException, JSONException {
        ArrayList<String> modelList = new ArrayList<String>();
        String jsonURL = GET_MODELS_URL + "&year=" + Integer.toString(year) + "&make=" + make;
        Log.d("JSON_URL", jsonURL);
        GetJson jsonAPI = new GetJson(jsonURL);
        Log.d("JSON_out", jsonAPI.getJSONString());
        JSONArray modelsArray = jsonAPI.parseJSONObject().getJSONArray("Models");

        for (int i = 0; i < modelsArray.length(); i++) {
            modelList.add(modelsArray.getJSONObject(i).getString("model_name"));
        }

        return modelList;
    }

    /**
     * @param model The model to get trims for
     * @param make The make to get models for
     * @param year The year to get models for
     * @return An ArrayList of possible trims for given year, make, and model
     * @throws JSONException
     * @throws IOException
     */
    public Map<String,Integer> getTrims(String model, String make, int year) throws JSONException, IOException {
        Map<String, Integer> trimMap = new HashMap<String, Integer>();
        String jsonURL = GET_TRIMS_URL + "&year=" + Integer.toString(year) + "&make=" + make + "&model=" + model;
        GetJson jsonAPI = new GetJson(jsonURL);
        Log.d("JSON_URL", jsonURL);
        Log.d("JSON_out", jsonAPI.getJSONString());
        JSONArray trimsArray = jsonAPI.parseJSONObject().getJSONArray("Trims");

        for (int i = 0; i < trimsArray.length(); i++ ) {
            String trimName = trimsArray.getJSONObject(i).getString("model_trim");
            if (trimName.isEmpty()) trimName = "None";
            int trimID = trimsArray.getJSONObject(i).getInt("model_id");
            trimMap.put(trimName, trimID);
        }

        return trimMap;
    }

    /**
     * Updates the TextView with MPG and tanksize info. Returns a Map with
     * MPG and tank size information.
     *
     * @param modelID The modelID of the trim to get info for. This ID comes from
     *                the JSON call to get trim information.
     * @return A Map with keys for each attribute that returns the value of that attribute.
     * @throws IOException
     * @throws JSONException
     */
    public Map<String, String> getCarInfo(int modelID) throws IOException, JSONException {
        Map<String, String> infoHash = new HashMap<String, String>();
        String jsonURL = GET_MODEL_DATA_URL + "&model=" + modelID;
        Log.d("JSON_URL", jsonURL);
        GetJson jsonAPI = new GetJson(jsonURL);
        Log.d("JSON_out", jsonAPI.getJSONString());

        JSONObject carInfo = jsonAPI.parseJSONArray().getJSONObject(0);
        infoHash.put("mpg", carInfo.getString("model_mpg_city"));
        infoHash.put("tankSize", carInfo.getString("model_fuel_cap_g"));

        return infoHash;
    }
}
