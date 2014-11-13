package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.location.Location;
import android.os.StrictMode;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GasMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GasMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GasMapFragment extends Fragment implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap map;
    private LocationClient locationClient;
    private MyGasFeedAPI myGasFeed;
    private Car car;

    //Begin Location Based Variables
    private Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    protected LocationManager locationManager;
    //End Declarations


    //TODO: Figure out why to avoid non-default constructors in fragments.
    /*public GasMapFragment(Context context) {
        this.mContext = context;
        getLocation();
    }*/
    //getLocation is Below



    private OnFragmentInteractionListener mListener;

    public static GasMapFragment newInstance(Car car) {
        GasMapFragment fragment = new GasMapFragment();
        fragment.car = car;
        return fragment;
    }
    public GasMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        locationClient = new LocationClient(getActivity(), this, this);
        myGasFeed = new MyGasFeedAPI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gas_map, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(ListView l, View v, int position, long id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        locationClient.connect();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true); //Enables the button that moves the camera to user location
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

        ArrayList <TotalGasPrice> gasPrices = new ArrayList<TotalGasPrice>();

        // For each station in stations
        for (GasStation station : stations) {
            double latitude = station.getLatitude();
            double longitude = station.getLongitude();
            String name = station.getName();

            TotalGasPrice gasPrice = new TotalGasPrice(station, car, true);
            double totalPrice = gasPrice.getTotalPrice(car.getTankSize());
            gasPrices.add(gasPrice);

            String markerText;
            if (totalPrice != 0)
                markerText = "Total Price: $ " + totalPrice;
            else
                markerText = "Price not available.";

            map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(name)
                            .snippet(markerText)
            );
        }
    }


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
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
    @Override
    public void onLocationChanged(Location location) {

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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(int position);
    }


}
