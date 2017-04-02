package com.example.arto.mapboxtest;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsRoute;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.models.DirectionsResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mMapView;
    MapboxMap mMapboxMap;

    final String TAG = MainActivity.class.getSimpleName();

    final Position origin = Position.fromCoordinates(-3.588098, 37.176164);

    final Position destination = Position.fromCoordinates(-3.601845, 37.184080);

    private DirectionsRoute currentRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapboxAccountManager.start(this, "pk.eyJ1IjoidmlpdGFuZW4iLCJhIjoiY2l6bnptZnRsMDAxYTJ3cGphcXlhYWplOSJ9.HTxJSzVovQIrTf3QA1n8RQ");

        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mMapboxMap = mapboxMap;

        mMapboxMap.addMarker(new MarkerOptions()
            .position(new LatLng(origin.getLatitude(), origin.getLongitude()))
            .title("Origin")
            .snippet("Alhambra")
        );

        mMapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(destination.getLatitude(), destination.getLongitude()))
                .title("Destination")
                .snippet("Plaza del Triunfo")
        );

        /*
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(origin.getLatitude(), origin.getLongitude()))
                .include(new LatLng(destination.getLatitude(), destination.getLongitude()))
                .build();

        mMapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 5000);
        */

        //new DrawGeoJson().execute();
    }

    public void getRouteBtnOnClick(View view) {

        try {
            RadioButton bicycle = (RadioButton) findViewById(R.id.travelmethod_bicycle);

            String driveMethod = (bicycle.isChecked()) ? DirectionsCriteria.PROFILE_CYCLING : DirectionsCriteria.PROFILE_DRIVING;
            getRoute(origin, destination, driveMethod);

        } catch (ServicesException servicesException) {
            servicesException.printStackTrace();
        }

    }

    private void getRoute(Position origin, Position destination, String driveMethod) throws ServicesException {

        MapboxDirections client = new MapboxDirections.Builder()
                .setOrigin(origin)
                .setDestination(destination)
                .setProfile(driveMethod)
                .setAccessToken(MapboxAccountManager.getInstance().getAccessToken())
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get the generic HTTP info about the response
                Log.d(TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().getRoutes().size() < 1) {
                    Log.e(TAG, "No routes found");
                    return;
                }

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                Log.d(TAG, "Distance: " + currentRoute.getDistance());
                Toast.makeText(
                        MainActivity.this,
                        "Route is " + currentRoute.getDistance() + " meters long. It will take you appromaximetely " + (int) currentRoute.getDuration() / 60 + " minutes to get there.",
                        Toast.LENGTH_LONG).show();

                // Draw the route on the map
                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void drawRoute(DirectionsRoute route) {
        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }

        // Draw Points on MapView
        mMapboxMap.addPolyline(new PolylineOptions()
                .add(points)
                .color(Color.parseColor("#009688"))
                .width(5));

    /*
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(origin.getLatitude(), origin.getLongitude()))
                .include(new LatLng(destination.getLatitude(), destination.getLongitude()))
                .build();

        mMapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 5000);
        */

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



    private class DrawGeoJson extends AsyncTask<Void, Void, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(Void... params) {
            List<LatLng> points = new ArrayList<>();

            try {
                InputStream inputStream = getAssets().open("map.geojson");
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();

                int cp;

                while((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }

                inputStream.close();

                JSONObject json = new JSONObject(sb.toString());
                JSONArray features = json.getJSONArray("features");
                JSONObject feature = features.getJSONObject(0);
                JSONObject geometry = feature.getJSONObject("geometry");

                if (geometry != null) {
                    String type = geometry.getString("type");

                    if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {
                        JSONArray coords = geometry.getJSONArray("coordinates");

                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            points.add(latLng);
                        }
                    }

                }
            } catch(Exception e) {
                Log.e("ERROR", e.getMessage());
            }

            return points;
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            super.onPostExecute(points);
            Log.d("HELLO", "Points size: " + points.size());

            if (points.size() > 0) {
                mMapboxMap.addPolyline(new PolylineOptions().addAll(points).color(Color.parseColor("#3bb2d0")).width(2));
            }
        }
    }
}
