package com.example.arto.googlemaps;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.Rating;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GeoJsonLayer.GeoJsonOnFeatureClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private boolean track;
    public final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private String routeFilename;
    private View bottomSheet;
    private BottomSheetBehavior mBottomSheetBehaviour;
    private List<PointOfInterest> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        routeFilename = intent.getStringExtra("filename");

        bottomSheet = findViewById(R.id.bottom_sheet);

        mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);

        mBottomSheetBehaviour.setPeekHeight(0);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerClickListener(this);
        new DrawRouteWithPointsOfInterest().execute();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.d("hadsf", "Hello form maker");

        PointOfInterest poi = GetPointData((int) marker.getTag());

        if(poi != null) {
            Log.d("MAPS", poi.toString());

            TextView name = (TextView) bottomSheet.findViewById(R.id.point_details_name);
            TextView description = (TextView) bottomSheet.findViewById(R.id.point_details_description);
            TextView details = (TextView) bottomSheet.findViewById(R.id.point_details_details);
            TextView visitCount = (TextView) bottomSheet.findViewById(R.id.point_details_visitcount);
            RatingBar rating = (RatingBar) bottomSheet.findViewById(R.id.point_details_rating);

            name.setText(poi.name);
            description.setText(poi.description);
            details.setText(poi.details);
            visitCount.setText(String.format(Locale.ENGLISH, "%d", poi.visitCount));
            rating.setRating(poi.rating);

            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

        }

        return false;
    }

    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        createLocationRequest();
    }

    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onFeatureClick(Feature feature) {
        if (feature.hasProperty("title")) {
            Log.d("GEOJSONCLICK", "Feature clicked: " + feature.getProperty("title"));

        } else {
            Log.d("GEOJSONCLICK", "Feature clicked");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        track = sharedPref.getBoolean(SettingsFragment.PREF_TRACK, false);
        Log.d("TRACK", track + "");

        if(track) {
            checkPermissions();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void startLocationUpdates() {

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Log.d("TRACK", "TRACKING");

        } catch(SecurityException e) {
            Log.e("ERROR", e.getMessage());
        }

    }

    private void checkPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            startLocationUpdates();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void createLocationRequest() {
        final int REQUEST_CHECK_SETTINGS = 0;
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MapsActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        mMap.addMarker(new MarkerOptions().title("YOU ARE HERE").position(new LatLng(location.getLatitude(), location.getLongitude())));
        Log.d("LOCATION", location.toString());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(15)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private class DrawRouteWithPointsOfInterest extends AsyncTask<Void, Void, Void> {

        MapRoute mapRoute;
        JSONObject json;

        @Override
        protected Void doInBackground(Void... params) {


            try {
                String jsonString = new JSONParser().jsonFromFile(routeFilename, getApplicationContext());
                //json = new JSONObject(jsonString);

                JSONObject json = new JSONObject(jsonString);

                //Features
                JSONArray features = json.getJSONArray("features");
                points.clear();

                for (int i = 0; i < features.length(); i++) {

                    JSONObject feature = features.getJSONObject(i);
                    Log.d("asdfa", feature.toString());
                    JSONObject properties = feature.getJSONObject("properties");
                    Log.d("asdfjk", properties.toString());
                    JSONObject geometry = feature.getJSONObject("geometry");
                    int id = properties.getInt("id");

                    String type = geometry.getString("type");
                    if (type.equalsIgnoreCase("Point")) {

                        String name = properties.getString("name");
                        String description = properties.getString("description");
                        String details = properties.getString("details");
                        int visitCount = properties.getInt("visitCount");
                        float rating = (float) properties.getDouble("rating");

                        JSONArray coords = geometry.getJSONArray("coordinates");

                        LatLng latLng = new LatLng(coords.getDouble(1), coords.getDouble(0));

                        PointOfInterest point = new PointOfInterest(id, name, latLng, rating, visitCount, description, details);
                        points.add(point);

                    } else if (type.equalsIgnoreCase("LineString")) {
                        String name = properties.getString("name");
                        float width = (float) properties.getDouble("stroke-width");
                        String color = properties.getString("stroke");


                        if (width == 0) {
                            width = 3;
                        }

                        JSONArray coords = geometry.getJSONArray("coordinates");
                        mapRoute = new MapRoute(id, name, width, color);
                        ArrayList<LatLng> routeCoords = new ArrayList<>();

                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            routeCoords.add(latLng);
                        }
                        mapRoute.coords = routeCoords;
                    }
                }


            } catch(Exception e) {
                Log.e(DrawRouteWithPointsOfInterest.class.getSimpleName(), e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            //GeoJsonLayer layer = new GeoJsonLayer(mMap, json);
            //layer.addLayerToMap();

            mMap.addPolyline(new PolylineOptions().addAll(mapRoute.coords).color(Color.CYAN).width(mapRoute.width));
            for(PointOfInterest poi : points) {
                mMap.addMarker(new MarkerOptions().title(poi.name).position(poi.coords)).setTag(poi.id);
            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(points.get(0).coords)
                    .zoom(15)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    private PointOfInterest GetPointData(int id) {
        for(PointOfInterest poi : points) {
            if (poi.id == id) {
                return poi;
            }
        }

        return null;
    }

}
