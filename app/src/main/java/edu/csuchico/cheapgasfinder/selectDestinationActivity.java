package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles events for activity_select_destination.xml
 *
 */
public class selectDestinationActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private GoogleMap map;
    private LocationClient locationClient;
    private MyGasFeedAPI myGasFeed;
    private Bundle extras;
    private Car car;

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

        locationClient = new LocationClient(this, this, this);

        myGasFeed = new MyGasFeedAPI();

        Gson gson = new Gson();
        extras = getIntent().getExtras();
        car = gson.fromJson(extras.getString("car_json"), Car.class);
    }

    /**
     * Makes an API call to MyGasFeedAPI and populates the map with markers for each gas station
     *
     * @throws IOException
     * @throws JSONException
     */
    private void addStationMakers() throws IOException, JSONException {
        Location currentLocation;
        currentLocation = locationClient.getLastLocation();

        // TODO: Currently this uses a radius of 50 miles, but it should be adjustable
        ArrayList <GasStation> stations
                = myGasFeed.getStations(currentLocation.getLatitude(), currentLocation.getLongitude(),
                  50, "reg", "distance");

        // For each station in stations
        for (GasStation station : stations) {
            double latitude = station.getLatitude();
            double longitude = station.getLongitude();
            String name = station.getName();

            double price = 0;
            if (car.getFuelType().equals("Regular"))
                price = station.getPrePrice();
            else if (car.getFuelType().equals("Mid"))
                price = station.getMidPrice();
            else if (car.getFuelType().equals("Premium"))
                price = station.getPrePrice();
            else if (car.getFuelType().equals("Diesel"))
                price = station.getDieselPrice();

            map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name)
                .snippet(car.getFuelType() + ": " + price)
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

        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
            case 2: //out date
                try {
                    GooglePlayServicesUtil.getErrorDialog(2, this, 0).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        locationClient.connect();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true); //Enables the button that moves the camera to user location
    }

    /**
     * Called when the activity is no longer visible.
     *
     */
    @Override
    protected void onStop() {
        locationClient.disconnect();
        super.onStop();
    }

    /**
     * After calling connect(), this method will be called when the connection has
     * completed successfully.
     *
     * @param connectionHint Bundle of data provided to clients by Google Play services.
     *                       May be null if no content is provided by the service.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Location currentLocation;
        currentLocation = locationClient.getLastLocation();
        float zoomLevel = 12; // From 2.0 - 21.0, how far to zoom in. Larger numbers are zoomed in more.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), zoomLevel )
        );

        try {
            addStationMakers();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
