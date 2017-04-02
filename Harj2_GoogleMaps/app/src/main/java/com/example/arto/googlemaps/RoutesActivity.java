package com.example.arto.googlemaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity implements OnItemClickListener {

    ArrayList<Route> routesList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        routesList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.routesList);

        String jsonString = new JSONParser().jsonFromFile("routes.json", getApplicationContext());


        try {
            JSONObject routesObject = new JSONObject(jsonString);
            JSONArray routes = routesObject.getJSONArray("routes");

            for(int i = 0; i < routes.length(); i++) {
                JSONObject routeObj = routes.getJSONObject(i);

                int id = routeObj.getInt("id");
                String name = routeObj.getString("name");
                String filename = routeObj.getString("filename");
                float length = (float) routeObj.getDouble("length");
                String condition = routeObj.getString("condition");
                float rating = (float) routeObj.getDouble("rating");
                int poiCount = routeObj.getInt("pointOfInterestCount");

                Route route = new Route(id, name, filename, length, condition, rating, poiCount);

                routesList.add(route);
                ArrayAdapter adapter = new ArrayAdapter<Route>(this, R.layout.route_listview_item, routesList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);

            }

        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage());
        }



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Route route = routesList.get(position);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("filename", route.filename);
        startActivity(intent);
    }
}
