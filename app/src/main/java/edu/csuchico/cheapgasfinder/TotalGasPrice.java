package edu.csuchico.cheapgasfinder;

/**
 * Given a Station and a Car, calculates the cost of getting gas.
 */
public class TotalGasPrice {
    private Car car;

    private double gallonsUsed;
    private double fuelPrice;

    /**
     * Sets up a new TotalGasPrice. Calculates gallons used to get to the station, and sets the type
     * of fuel based on the car.
     *
     * @param station The gas station to calculate results for.
     * @param car The car that will be used to drive to the station.
     * @param returnTrip True if the user will be returning to the starting point.
     */

    public TotalGasPrice(GasStation station, Car car, boolean returnTrip) {
        this.car = car;

        gallonsUsed = station.getDistance() / car.getMpg(); // miles * (gallons / miles) = gallons

        if (returnTrip)
            gallonsUsed = gallonsUsed * 2;

        if (car.getFuelType().equals("Regular"))
            fuelPrice = station.getRegPrice();
        else if (car.getFuelType().equals("Mid"))
            fuelPrice = station.getMidPrice();
        else if (car.getFuelType().equals("Premium"))
            fuelPrice = station.getPrePrice();
        else if (car.getFuelType().equals("Diesel"))
            fuelPrice = station.getDieselPrice();
    }

    /**
     * Calculates the total cost of gas given a distance, i.e. factoring in the cost of driving to
     * the gas station from the current location.
     *
     * @param gallonsPurchased The number of gallons to be purchased.
     *                         This must be less than the car's gas tank.
     *
     * @return The total price of getting gas, rounded to two decimal places.
     */
    public double getTotalPrice(double gallonsPurchased) {
        if (BuildConfig.DEBUG && !(gallonsPurchased <= car.getTankSize()))
            throw new RuntimeException("gallonsPurchased should not exceed car.getTankSize()");

        double price = (fuelPrice * (gallonsUsed + gallonsPurchased));
        return Math.round(price * 100.0) / 100.0; // Doubles don't have decimals, but this is a way to round them
    }

    /**
     * @return The price in dollars per gallon at the station
     * for the car's fuel type
     */
    public double getGasPrice() {
        return fuelPrice;
    }
}
