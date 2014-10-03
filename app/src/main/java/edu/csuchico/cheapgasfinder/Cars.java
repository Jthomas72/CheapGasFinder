package edu.csuchico.cheapgasfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides access the cars that have been saved in SharedPreferences
 */
public class Cars {
    private Context context;
    final static String PREFS_NAME = "UserPrefs";

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
        editor.commit();
    }

    /**
     * @return an ArrayList of Strings, containing the JSON representations
     * of all saved cars
     */
    public ArrayList<String> getCarsJSON() {
        Set<String> cars = context.getSharedPreferences(PREFS_NAME, 0).getStringSet("cars", null);

        return new ArrayList<String>(cars);
    }

    /**
     * @return an ArrayList of all saved Cars
     */
    public ArrayList<Car> getCars() {
        ArrayList<String> carsJSON = this.getCarsJSON();
        ArrayList<Car> cars = new ArrayList<Car>();

        for (int i = 0; i < carsJSON.size(); i++) {
            Gson gson = new Gson();
            Car newCar = gson.fromJson(carsJSON.get(i), Car.class);
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

        for (int i = 0; i < carsJSON.size(); i++) {
            Gson gson = new Gson();
            Car newCar = gson.fromJson(carsJSON.get(i), Car.class);
            carNames.add(newCar.name);
        }

        return carNames;
    }
}
