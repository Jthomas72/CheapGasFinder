package edu.csuchico.cheapgasfinder;

import android.location.Location;

import junit.framework.TestCase;
/**
 * Created by Mason on 11/2/2014.
 */
public class GasStationTest extends TestCase{

    Location origin, destination;
    GasStation gasStation;

    protected void setUp() {
        // Trader Joe's Chico
        origin = new Location("test");
        origin.setLatitude(39.760977);
        origin.setLongitude(-121.833993);

        // Winco Chico
        destination = new Location("test");
        destination.setLatitude(39.727234);
        destination.setLongitude(-121.800919);

        // Costco Chico
        gasStation = new GasStation();
        gasStation.setLatitude(39.728494);
        gasStation.setLongitude(-121.837478);
        gasStation.setDistance(origin, destination);
    }

    public void testSetDistanceGreaterThanZero() {
        assertTrue("Distance should be greater than 0", gasStation.getDistance() > 0);
    }

    public void testSetDistanceLessThan5() {
        assertTrue("Distance is most likely returning too large of a value", gasStation.getDistance() < 3);
    }

}
