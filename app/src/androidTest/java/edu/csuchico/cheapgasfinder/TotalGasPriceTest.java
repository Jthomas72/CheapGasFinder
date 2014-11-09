package edu.csuchico.cheapgasfinder;

import junit.framework.TestCase;

public class TotalGasPriceTest extends TestCase {
    GasStation gasStation;
    Car car;
    TotalGasPrice totalGasPrice;

    protected void setUp() {
        gasStation = new GasStation();
        gasStation.setDistance(30.00);
        gasStation.setRegPrice(3.00);

        car = new Car();
        car.setFuelType("Regular");
        car.setMpg(30.0);
        car.setTankSize(12.0);
    }

    public void testOneGallonUsed() throws Exception {
        totalGasPrice = new TotalGasPrice(gasStation, car, false);
        assertTrue(totalGasPrice.getTotalPrice(0.0) == 3);

    }

    public void testOneGallonPurchased() throws Exception {
        gasStation.setDistance(0);
        totalGasPrice = new TotalGasPrice(gasStation, car, false);
        assertTrue(totalGasPrice.getTotalPrice(1.0) == 3);

    }


    public void testOneGallonPurchasedAndUsed() throws Exception {
        totalGasPrice = new TotalGasPrice(gasStation, car, false);
        assertTrue(totalGasPrice.getTotalPrice(1.0) == 6);

    }

    public void testTwoGallonsUsedWithReturnTrip() throws Exception {
        totalGasPrice = new TotalGasPrice(gasStation, car, true);
        assertTrue(totalGasPrice.getTotalPrice(0.0) == 6);

    }
}


