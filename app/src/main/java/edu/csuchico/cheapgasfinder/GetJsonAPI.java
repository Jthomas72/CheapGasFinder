package edu.csuchico.cheapgasfinder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URISyntaxException;


public class GetJsonAPI  {
    private String jsonstring;
    private String url;

    public GetJsonAPI(String url) throws IOException {
        this.url = url;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String jsonString = EntityUtils.toString(httpResponse.getEntity());

        this.jsonstring = jsonString;
    }

    public String getJSONString() {
        return jsonstring;
    }
    public JSONObject parseJSONObject() throws JSONException {
        return new JSONObject(this.jsonstring.substring(this.jsonstring.indexOf('{'), this.jsonstring.lastIndexOf('}') + 1));
    }

    public JSONArray parseJSONArray() throws JSONException {
        return new JSONArray(this.jsonstring.substring(this.jsonstring.indexOf('['), this.jsonstring.lastIndexOf(']') + 1));
    }

}
