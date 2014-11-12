package edu.csuchico.cheapgasfinder;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * Provides an easy way to get information from an API that
 * returns JSON. This class should be used within another
 * class specific to that API, a "model" with functions for
 * specific API calls.
 */
public class GetJson {
    private String jsonstring;

    /**
     * Makes an API call to the provided URL and stores the results.
     * Parse it with with either parseJSONArray or parseJSONObject,
     * depending on the expected string to be returned.
     *
     * @param url A URL containing a JSON call.
     * @throws IOException
     */
    public GetJson(String url) throws IOException {
        Log.d("JSON_URL", url);

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        this.jsonstring = EntityUtils.toString(httpResponse.getEntity());
        Log.w("JSON_out", jsonstring);
    }

    public String getJSONString() {
        return jsonstring;
    }

    /**
     * Parses a JSON Object returned from the API. JSON objects
     * are surrounded with curly braces and contain many fields
     * with Strings or other data types. This function will
     * strip any extra characters from the beginning and end of input.
     *
     * @return JSONObject
     * @throws JSONException
     */
    public JSONObject parseJSONObject() throws JSONException {
        return new JSONObject(this.jsonstring.substring(this.jsonstring.indexOf('{'), this.jsonstring.lastIndexOf('}') + 1));
    }

    /**
     * Parses a JSON Array returned from the API. JSON arrays
     * contain many JSON objects. JSON arrays are surrounded by
     * square brackets. This function will strip any extra
     * characters from the beginning and end of input.
     *
     * @return JSONArray
     * @throws JSONException
     */
    public JSONArray parseJSONArray() throws JSONException {
        return new JSONArray(this.jsonstring.substring(this.jsonstring.indexOf('['), this.jsonstring.lastIndexOf(']') + 1));
    }

}
