package edu.csuchico.cheapgasfinder;

import android.location.Location;
import android.util.Log;

/**
 * Stores the fields for a gas station object.
 * The information will come from MyGasFeedAPI.
 */
public class GasStation {
    private double regPrice, midPrice, prePrice, dieselPrice,
            distance, latitude, longitude;
    private String address, region, city, name;

    public double getRegPrice() {
        return regPrice;
    }

    public void setRegPrice(double regPrice) {
        this.regPrice = regPrice;
    }

    public double getMidPrice() {
        return midPrice;
    }

    public void setMidPrice(double midPrice) {
        this.midPrice = midPrice;
    }

    public double getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(double prePrice) {
        this.prePrice = prePrice;
    }

    public double getDieselPrice() {
        return dieselPrice;
    }

    public void setDieselPrice(double dieselPrice) {
        this.dieselPrice = dieselPrice;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDistance(String distance) {
        this.distance = distanceStringtoDouble(distance);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Converts a String describing the distance into a double containing the distance in miles.
     *
     * @param dist String describing a distance, like "0.5 miles." This should only end in miles.
     * @return A double containing a distance in miles
     */
    private double distanceStringtoDouble(String dist) {
        String distance[] = dist.split(" ");

        if (BuildConfig.DEBUG && !(distance[1].equals("miles")))
            throw new RuntimeException("Distance should be in miles");

        // distance[0] should contain a double
        return Double.parseDouble(distance[0]);
    }

    /**
     * Sets the distance based upon how many extra miles it will take to drive to
     * the gas station on the way to your destination, instead of directly to the
     * destination.
     *
     * @param origin The starting location.
     * @param destination The ending location.
     */
    public void setDistance(Location origin, Location destination) {
        Float toDestinationMeters = origin.distanceTo(destination);
        Log.w("GasStation toDestinationMeters", toDestinationMeters.toString());

        // the value of the constructor param doesn't matter since the lat/long values are set from known values
        Location stationLocation = new Location("MyGasFeed");

        stationLocation.setLatitude(this.latitude);
        stationLocation.setLongitude(this.longitude);
        Float toGasStationMeters = origin.distanceTo(stationLocation) + stationLocation.distanceTo(destination);
        Log.w("GasStation toGasStationMeters", toGasStationMeters.toString());

        // converts meters into miles
        this.distance = (toGasStationMeters - toDestinationMeters) / 1609.34;
        Log.w("GasStation distance", Double.toString(this.distance));
    }
}
