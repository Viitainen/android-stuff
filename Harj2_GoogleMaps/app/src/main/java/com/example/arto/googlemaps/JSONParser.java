package com.example.arto.googlemaps;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class JSONParser {


    public String jsonFromFile(String filename, Context context) {

        String json = "";

        try {

            InputStream inputStream = context.getAssets().open(filename);
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();

            int cp;

            while((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            json = sb.toString();
            inputStream.close();
        }
        catch(Exception e) {
            Log.e(JSONParser.class.getSimpleName(), e.getMessage());
        }

        return json;

    }
}
