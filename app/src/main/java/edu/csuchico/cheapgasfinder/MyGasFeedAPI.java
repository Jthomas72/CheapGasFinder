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

    private JSONArray jsonArray;

    /**
     * Default constructor for MyGasFeedAPI
     */
    public MyGasFeedAPI() {
        Log.d("JSON", "MyGasFeedAPI object created.");
    }

    /**
     * This constructor overrides the API calls for testing.
     *
     * @param jsonArray A JSONArray formatted to MyGasFeed's specifications, containing stations.
     */
    public MyGasFeedAPI(JSONArray jsonArray) {
        Log.d("JSON", "MyGasFeedAPI object created.");
        this.jsonArray = jsonArray;
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

        if (jsonArray == null) {
            final String REQUEST_URL = API_URL + "/stations/radius/"
                    + latitude + "/" + longitude + "/" + distance + "/" + fuelType + "/"
                    + sortBy + "/" + API_KEY + ".json";

            Log.d("JSON_url", REQUEST_URL);
            GetJson jsonAPI = new GetJson(REQUEST_URL);
            Log.w("JSON_out", jsonAPI.getJSONString());
            jsonArray = jsonAPI.parseJSONObject().getJSONArray("stations");
        }

        // TODO: add more data, such as last time updated to GasStation objects
        //noinspection Convert2Diamond Changing this will break the build.
        ArrayList<GasStation> stationList = new ArrayList<GasStation>();
        for (int i = 0; i < jsonArray.length(); i++) {
            GasStation newStation = new GasStation();
            JSONObject stationJSON = jsonArray.getJSONObject(i);
            newStation.setName(stationJSON.getString("station"));
            newStation.setAddress(stationJSON.getString("address"));
            newStation.setCity(stationJSON.getString("city"));
            newStation.setRegion(stationJSON.getString("region"));
            newStation.setLongitude(stationJSON.getDouble("lng"));
            newStation.setLatitude(stationJSON.getDouble("lat"));
            newStation.setDistance(stationJSON.getString("distance"));

            String regPrice = stationJSON.getString("reg_price");
            if (validGasPrice(regPrice))
                newStation.setRegPrice(Double.parseDouble(regPrice));

            String midPrice = stationJSON.getString("mid_price");
            if (validGasPrice(midPrice))
                newStation.setMidPrice(Double.parseDouble(midPrice));

            String prePrice = stationJSON.getString("pre_price");
            if (validGasPrice(prePrice))
                newStation.setPrePrice(Double.parseDouble(prePrice));

            String dieselPrice = stationJSON.getString("diesel_price");
            if (validGasPrice(dieselPrice))
                newStation.setDieselPrice(Double.parseDouble(dieselPrice));

            stationList.add(newStation);
        }

        return stationList;
    }

    /**
     * @param price A string containing the price, without a dollar sign.
     * @return true if the price is in the format X.XX. The API will
     *         return a non-double value if it has no data for a station's price
     */
    public boolean validGasPrice(String price) {
        return price.matches("^[0-9](\\.[0-9]{2})$");
    }
}
