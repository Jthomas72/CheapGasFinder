package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles events for activity_select_destination.xml
 *
 */
public class selectDestinationActivity extends Activity implements LocationListener {

    private GoogleMap map;
    private LocationManager locationManager;
    MyGasFeedAPI myGasFeed;

    /**
     * Sets up the the elements of the select destination view
     *
     * @param savedInstanceState This parameter is passed when the function gets called
     *                           automatically
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_destination);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true); //Enables the button that moves the camera to user location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60, 0, this);

        myGasFeed = new MyGasFeedAPI();

        try {
            addStationMakers();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes an API call to MyGasFeedAPI and populates the map with markers for each gas station
     * @throws IOException
     * @throws JSONException
     */
    private void addStationMakers() throws IOException, JSONException {
        // TODO: This is causing a null pointer exception. Find out how to properly get user location.
        /*Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d("GPS", "Lat: " + l.getLatitude());
        Log.d("GPS", "Long: " + l.getLongitude());
        */

        // TODO: get current user location. This is the location for the center of Chico.
        // TODO: Currently this uses a radius of 5 miles, but that should not be a constant value.
        ArrayList <GasStation> stations = myGasFeed.getStations(39.728494, -121.837478, 5, "reg", "distance");

        // For each station in stations
        for (GasStation station : stations) {
            double latitude = station.getLatitude();
            double longitude = station.getLongitude();
            String name = station.getName();
            map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name)
                .snippet("Regular: " + station.getRegPrice())
            );
        }


    }

    /**
     * Called when the activity will start interacting with the user.
     *
     */
    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_destination, menu);
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

    @Override
    public void onLocationChanged(Location location) {
        Log.d("map", "onLocationChanged called");

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
