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
    private String content;

    public String getJSONString(String URL) throws IOException, URISyntaxException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String jsonString = EntityUtils.toString(httpResponse.getEntity());

        content = jsonString;
        return jsonString;
    }
    public JSONObject parseJSONObject() throws JSONException {
        return new JSONObject(this.content.substring(this.content.indexOf('{'), this.content.lastIndexOf('}') + 1));
    }

    public JSONArray parseJSONArray() throws JSONException {
        return new JSONArray(this.content.substring(this.content.indexOf('['), this.content.lastIndexOf(']') + 1));
    }

}
