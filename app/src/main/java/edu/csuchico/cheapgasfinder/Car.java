package edu.csuchico.cheapgasfinder;

/**
 * Stores the fields for a user's car. It is to be
 * stored and loaded with the Cars class.
 */
public class Car {
    String name;
    String make;
    String model;
    String trim;
    int year;
    double mpg, tankSize;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getTrim() { return trim; }
    public void setTrim(String trim) { this.trim = trim; }

    public int getYear() {  return year; }
    public void setYear(int year) { this.year = year; }

    public double getMpg() { return mpg; }
    public void setMpg(double mpg) { this.mpg = mpg; }

    public double getTankSize() { return tankSize; }
    public void setTankSize(double tankSize) { this.tankSize = tankSize; }

    public Car() {
    }
}

