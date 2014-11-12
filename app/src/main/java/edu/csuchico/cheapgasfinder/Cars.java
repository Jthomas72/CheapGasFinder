package edu.csuchico.cheapgasfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Provides access the cars that have been saved in SharedPreferences
 *
 */
public class Cars {
    private Context context;
    final static String PREFS_NAME = "UserPrefs";

    /**
     * Instantiates a new Cars object
     *
     * @param context A Context is needed to use SharedPreferences. From an Activity, you can
     *                pass this, for example: Cars cars = new Cars(this).
     */
    public Cars(Context context) {
        this.context = context;
    }

    /**
     * Saves the car in SharedPreferences in JSON format, using
     * the Gson library.
     */
    public void save(Car car) {
        Gson gson = new Gson();
        String carJson = gson.toJson(car);
        Log.d("JSON_car", carJson);

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        Set<String> cars = context.getSharedPreferences(PREFS_NAME, 0).getStringSet("cars", null);

        if (cars == null) {
            cars = new HashSet<String>();
        }
        cars.add(carJson);

        editor.putStringSet("cars", cars);
        editor.apply(); // apply() writes to preferences asynchronously, and commit() doesn't
    }

    public void save(ArrayList<Car> carsList) {
        Set<String> cars = context.getSharedPreferences(PREFS_NAME, 0).getStringSet("cars", null);
        cars.clear();

        for (Car car : carsList) {
            this.save(car);
        }
    }

    public void delete(String name) {
        ArrayList<Car> carsList = getCars();

        for (Iterator<Car> it = carsList.iterator(); it.hasNext(); ) {
            Car car = it.next();
            if (car.getName().equals(name)) {
                it.remove();
            }
        }

        this.save(carsList);
    }

    /**
     * @return an ArrayList of Strings, containing the JSON representations
     * of all saved cars
     */
    public ArrayList<String> getCarsJSON() {
        Set<String> cars = context.getSharedPreferences(PREFS_NAME, 0).getStringSet("cars", null);
        ArrayList<String> carsList;

        /* If there is no preferences file, such as when this app is opened for the first time,
        cars will be null, and so will the resulting ArrayList, so this makes a blank one. The app
        will crash otherwise when there are no saved cars. */
        if (cars != null) {
            carsList = new ArrayList<String>(cars);
        } else {
           carsList = new ArrayList<String>();
        }
        return carsList;
    }

    /**
     * @return an ArrayList of all saved Cars
     */
    public ArrayList<Car> getCars() {
        ArrayList<String> carsJSON = this.getCarsJSON();
        ArrayList<Car> cars = new ArrayList<Car>();

        for (String carJSON : carsJSON) { // for each carJSON in carsJSON
            Gson gson = new Gson();
            Car newCar = gson.fromJson(carJSON, Car.class);
            cars.add(newCar);
        }

        return cars;
    }

    /**
     * @return an ArrayList of Strings, containing the names of all saved cars
     */
    public ArrayList<String> getCarsNames() {
        ArrayList<String> carsJSON = this.getCarsJSON();
        ArrayList<String> carNames = new ArrayList<String>();

        for (String carJSON : carsJSON) { // for each carJSON in carsJSON
            Gson gson = new Gson();
            Car newCar = gson.fromJson(carJSON, Car.class);
            carNames.add(newCar.getName());
        }

        return carNames;
    }
}
