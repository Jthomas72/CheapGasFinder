package edu.csuchico.cheapgasfinder;

import com.google.gson.Gson;

public class Car {
    String name, make, model, trim;
    int year;
    double mpg, tank_size;

    public Car(String name, int year, String make, String model, String trim, double mpg, double tank_size) {
        this.name = name;
        this.year = year;
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.mpg = mpg;
        this.tank_size = tank_size;
    }

    public Car(String name) {

    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


}
