package edu.csuchico.cheapgasfinder;

import junit.framework.TestCase;

import org.json.JSONException;

import java.io.IOException;

public class GetJSONTest extends TestCase {

    GetJson getJson;

    public void testGetJson() throws IOException, JSONException {
        getJson = new GetJson("http://echo.jsontest.com/key/value/one/two");
        assertTrue(getJson.parseJSONObject().getString("one").equals("two"));
        assertTrue(getJson.parseJSONObject().getString("key").equals("value"));
    }

}
