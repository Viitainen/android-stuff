package com.example.a.harjoitus2;

import java.util.Locale;

/**
 * Created by a on 6.2.2017.
 */

public class Employee {
    public String name;
    public int id;
    public String department;
    public String type;
    public String email;

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH ,"%d: %s%n%s - %s%n%s", id, name, department, type, email);
    }

}
