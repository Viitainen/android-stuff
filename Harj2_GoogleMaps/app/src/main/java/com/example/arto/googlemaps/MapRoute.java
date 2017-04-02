package com.example.arto.googlemaps;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapRoute {

    public int id;
    public String name;
    public ArrayList<LatLng> coords;
    public float width;
    public String color;

    public MapRoute(int id, String name, ArrayList<LatLng> coords, float width, String color) {
        this.id = id;
        this.coords = coords;
        this.color = color;
        this.width = width;
    }

    public MapRoute(int id, String name, ArrayList<LatLng> coords, float width) {
        this.id = id;
        this.coords = coords;
        this.width = width;
        this.color = "#333333";
    }

    public MapRoute(int id, String name, float width, String color) {
        this.id = id;
        this.width = width;
        this.color = color;
        this.coords = new ArrayList<LatLng>();
    }

}
