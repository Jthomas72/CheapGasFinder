package edu.csuchico.cheapgasfinder;

/**
 * Stores the fields for a gas station object.
 * The information will come from MyGasFeedAPI.
 */
public class GasStation {
    int id;
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
}
