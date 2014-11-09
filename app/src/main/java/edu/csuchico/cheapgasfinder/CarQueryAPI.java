package edu.csuchico.cheapgasfinder;

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

    final static private String BASE_URL           = "http://www.carqueryapi.com/api/0.3/";

    final static private String GET_YEARS_URL      = BASE_URL + "?callback=?&cmd=getYears";
    final static private String GET_MAKES_URL      = BASE_URL + "?callback=?&cmd=getMakes&sold_in_us=1";
    final static private String GET_MODELS_URL     = BASE_URL + "?callback=?&cmd=getModels&sold_in_us=1";
    final static private String GET_TRIMS_URL      = BASE_URL + "?callback=?&cmd=getTrims";
    final static private String GET_MODEL_DATA_URL = BASE_URL + "?callback=?&cmd=getModel";

    /**
     * @return ArrayList containing all possible years of cars
     * @throws IOException
     * @throws JSONException
     */
    public ArrayList<String> getYears() throws IOException, JSONException {
        GetJson jsonAPI = new GetJson(GET_YEARS_URL);
        int min_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("min_year");
        int max_year = jsonAPI.parseJSONObject().getJSONObject("Years").getInt("max_year");

        ArrayList<String> yearList = new ArrayList<String>();
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
        GetJson jsonAPI = new GetJson(GET_MAKES_URL + "&year=" + Integer.toString(year));
        JSONArray makesArray = jsonAPI.parseJSONObject().getJSONArray("Makes");

        ArrayList<String> makeList = new ArrayList<String>();
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
        GetJson jsonAPI = new GetJson(GET_MODELS_URL + "&year=" + Integer.toString(year) + "&make=" + make);
        JSONArray modelsArray = jsonAPI.parseJSONObject().getJSONArray("Models");

        ArrayList<String> modelList = new ArrayList<String>();
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
        GetJson jsonAPI = new GetJson(GET_TRIMS_URL + "&year=" + Integer.toString(year) + "&make=" + make + "&model=" + model);
        JSONArray trimsArray = jsonAPI.parseJSONObject().getJSONArray("Trims");

        Map<String, Integer> trimMap = new HashMap<String, Integer>();
        for (int i = 0; i < trimsArray.length(); i++ ) {
            String trimName = trimsArray.getJSONObject(i).getString("model_trim");
            if (trimName.isEmpty()) trimName = "None";
            int trimID = trimsArray.getJSONObject(i).getInt("model_id");
            trimMap.put(trimName, trimID);
        }

        return trimMap;
    }

    /**
     * Returns a Map with MPG and tank size information.
     *
     * @param modelID The modelID of the trim to get info for. This ID comes from
     *                the JSON call to get trim information.
     * @return A Map with keys for each attribute that returns the value of that attribute.
     * @throws IOException
     * @throws JSONException
     */
    public Map<String, String> getCarInfo(int modelID) throws IOException, JSONException {
        GetJson carQueryAPI = new GetJson(GET_MODEL_DATA_URL + "&model=" + modelID);
        JSONObject carInfo = carQueryAPI.parseJSONArray().getJSONObject(0);

        Map<String, String> infoHash = new HashMap<String, String>();
        infoHash.put("mpg", carInfo.getString("model_mpg_city"));
        infoHash.put("tankSize", carInfo.getString("model_fuel_cap_g"));

        String fuelType = carInfo.getString("model_engine_fuel");
        if (fuelType.contains("Regular"))
            fuelType = "Regular";
        else if (fuelType.contains("Mid"))
            fuelType = "Mid";
        else if (fuelType.contains("Premium"))
            fuelType = "Premium";
        else if (fuelType.contains("Diesel"))
            fuelType = "Diesel";
        else if (BuildConfig.DEBUG)
            throw new RuntimeException("Unknown fuel type");

        infoHash.put("fuelType", fuelType);

        return infoHash;
    }
}