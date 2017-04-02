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

public class XMLPartsParser {

    List<Part> parts;
    private Part part;
    private String text;

    public XMLPartsParser() {
        parts = new ArrayList<Part>();
    }

    public List<Part> getParts() {
        return parts;
    }

    public List<Part> parse (InputStream is) {
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
                        if (tagname.equalsIgnoreCase("part")) {
                            part = new Part();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    /**
                     *
                     public String item;
                     public String manufacturer;
                     public String model;
                     public float cost;

                     */
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("part")) {
                            parts.add(part);
                        } else if (tagname.equalsIgnoreCase("item")) {
                            part.item = text;
                        } else if (tagname.equalsIgnoreCase("manufacturer")) {
                            part.manufacturer = text;
                        } else if (tagname.equalsIgnoreCase("model")) {
                            part.model = text;
                        } else if (tagname.equalsIgnoreCase("cost")) {
                            part.cost = Float.parseFloat(text);
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

        return parts;
    }
}
