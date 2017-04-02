package com.example.arto.googlemaps;

public class Route {

    public int id;
    public String name;
    public String filename;

    public float length;
    public String condition;

    public float rating;

    public int pointOfInterestCount;

    public Route(int id, String name, String filename, float length, String condition, float rating, int pointOfInterestCount) {
        this.id = id;
        this.name = name;
        this.filename = filename;
        this.length = length;
        this.condition = condition;
        this.rating = rating;
        this.pointOfInterestCount = pointOfInterestCount;
    }

    @Override
    public String toString() {
        return name + " | Length " + length + " | condition: " + condition + " | rating: " + rating + " | point of interests: " + pointOfInterestCount;
    }
}
