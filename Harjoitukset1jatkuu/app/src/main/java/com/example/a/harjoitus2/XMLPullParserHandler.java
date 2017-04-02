package com.example.a.harjoitus2;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 6.2.2017.
 */

public class XMLPullParserHandler {

    List<Employee> employees;
    private Employee employee;
    private String text;

    public XMLPullParserHandler() {
        employees = new ArrayList<Employee>();
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Employee> parse (InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();

                switch(eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("employee")) {
                            employee = new Employee();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("employee")) {
                            employees.add(employee);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            employee.id = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("name")) {
                            employee.name = text;
                        } else if (tagname.equalsIgnoreCase("department")) {
                            employee.department = text;
                        } else if (tagname.equalsIgnoreCase("email")) {
                            employee.email = text;
                        } else if (tagname.equalsIgnoreCase("type")) {
                            employee.type = text;
                        }

                        break;

                    default:
                        break;
                }

                eventType = parser.next();
            }

        } catch (Exception e) {
            Log.e("XMLPullParserHandler", e.getMessage());
        }

        return employees;
    }
}
