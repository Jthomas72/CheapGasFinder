package edu.csuchico.cheapgasfinder;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MyGasFeedAPITest extends TestCase {

    JSONArray jsonArray;
    MyGasFeedAPI myGasFeedAPI;
    String jsonData;

    protected void setUp() throws JSONException {
        jsonData =
                "[\n" +
                        "        {\n" +
                        "            \"country\": \"Canada\",\n" +
                        "            \"reg_price\": \"3.65\",\n" +
                        "            \"mid_price\": \"3.65\",\n" +
                        "            \"pre_price\": \"3.65\",\n" +
                        "            \"diesel_price\": \"3.65\",\n" +
                        "            \"address\": \"3885, Boulevard Saint-Rose\",\n" +
                        "            \"diesel\": \"0\",\n" +
                        "            \"id\": \"33862\",\n" +
                        "            \"lat\": \"45.492367\",\n" +
                        "            \"lng\": \"-73.710915\",\n" +
                        "            \"station\": \"Shell\",\n" +
                        "            \"region\": \"Quebec\",\n" +
                        "            \"city\": \"Saint-Laurent\",\n" +
                        "            \"reg_date\": \"3 hours agp\",\n" +
                        "            \"mid_date\": \"3 hours agp\",\n" +
                        "            \"pre_date\": \"3 hours agp\",\n" +
                        "            \"diesel_date\": \"3 hours agp\",\n" +
                        "            \"distance\": \"1.9 miles\"\n" +
                        "        }\n" +
                        "    ]";

        jsonArray = new JSONArray(jsonData);
        myGasFeedAPI = new MyGasFeedAPI(jsonArray);

    }

    public void testGetStations() throws IOException, JSONException {
        ArrayList<GasStation> stations = myGasFeedAPI.getStations(0, 0, 0, "", "");
        assertTrue(stations.get(0).getRegPrice() == 3.65);
    }

    public void testValidGasPrice() {
        assertTrue(myGasFeedAPI.validGasPrice("3.60"));
    }

    public void testValidGasPriceOneDecimalPlace() {
        assertFalse(myGasFeedAPI.validGasPrice("3.6"));
    }

    public void testValidGasPriceNoDecimalPlace() {
        assertFalse(myGasFeedAPI.validGasPrice("3"));
    }

    public void testValidGasPriceExtraDecimalPlace() {
        assertFalse(myGasFeedAPI.validGasPrice("3.605"));
    }

    public void testValidGasPriceInvalidDollarSign() {
        assertFalse(myGasFeedAPI.validGasPrice("$3.60"));
    }

    public void testValidGasPriceInvalidAlpha() {
        assertFalse(myGasFeedAPI.validGasPrice("a"));
    }
}
