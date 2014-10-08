package edu.csuchico.cheapgasfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Provides access to MyGasFeed API (http://www.mygasfeed.com).
 * The API returns information about current gas prices and gas stations
 * for a given location, and allows users to send updates about gas prices
 * when they visit a gas station.
 *
 */
public class MyGasFeedAPI {

    final static String API_URL = "http://api.mygasfeed.com/";
    final static String API_KEY = "vtn4c3fycf";

    public MyGasFeedAPI() {
        Log.d("JSON", "MyGasFeedAPI object created.");
    }

    /**
     * Makes an API request to get a list of stations
     *
     * @param longitude The longitude to get stations for
     * @param latitude The latitude to get stations for
     * @param distance A number of miles of the radius to search within
     * @param fuelType Can be "reg", "mid", "pre", or diesel.
     * @param sortBy Can be either "price" or "distance"
     * @return an ArrayList of GasStation objects for the given query.
     */
    public ArrayList<GasStation> getStations
        (double latitude, double longitude, double distance, String fuelType, String sortBy)
        throws IOException, JSONException {

        ArrayList<GasStation> stationList = new ArrayList<GasStation>();

        final String REQUEST_URL = API_URL + "/stations/radius/"
            + latitude + "/" + longitude + "/" + distance + "/" + fuelType + "/"
            + sortBy + "/" + API_KEY + ".json";

        Log.d("JSON_url", REQUEST_URL);
        GetJson jsonAPI = new GetJson(REQUEST_URL);
        Log.w("JSON_out", jsonAPI.getJSONString());

        JSONArray jsonArray = jsonAPI.parseJSONObject().getJSONArray("stations");

        // TODO: add more data, such as gas prices to the GasStation object
        for (int i = 0; i < jsonArray.length(); i++) {
            GasStation newStation = new GasStation();
            JSONObject stationJSON = jsonArray.getJSONObject(i);
            newStation.setName(stationJSON.getString("station"));
            newStation.setAddress(stationJSON.getString("address"));
            newStation.setLongitude(stationJSON.getDouble("lng"));
            newStation.setLatitude(stationJSON.getDouble("lat"));

            stationList.add(newStation);
        }

        return stationList;
    }
}
