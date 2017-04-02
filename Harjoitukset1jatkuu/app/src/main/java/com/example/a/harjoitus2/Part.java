package com.example.a.harjoitus2;

import java.util.Locale;

public class Part {

    public String item;
    public String manufacturer;
    public String model;
    public float cost;

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s %s by %s%n$%.2f", model, item, manufacturer, cost);
    }

}
