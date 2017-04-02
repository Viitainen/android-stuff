package com.example.arto.googlemaps;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class PointOfInterest {

    public int id;
    public String name;
    public LatLng coords;
    public float rating;
    public int visitCount;
    public String description;
    public String details;

    public int color;

    public PointOfInterest(int id, String name, LatLng coords, float rating, int visitCount, String description, String details, int color) {
        this.id = id;
        this.name = name;
        this.coords = coords;
        this.rating = rating;
        this.visitCount = visitCount;
        this.description = description;
        this.details = details;
        this.color = color;
    }

    public PointOfInterest(int id, String name, LatLng coords, float rating, int visitCount, String description, String details) {
        this.id = id;
        this.name = name;
        this.coords = coords;
        this.rating = rating;
        this.visitCount = visitCount;
        this.description = description;
        this.details = details;
        this.color = Color.CYAN;

    }

    @Override
    public String toString() {
        return name + " " + color;
    }

}
