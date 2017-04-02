package com.example.arto.yelp;

import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Review;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailsActivity extends AppCompatActivity {

    String id, name;

    TextView yelpDetailName = null;
    TextView closedTextView = null;
    RatingBar ratingBar;
    TextView phone;
    ListView reviewsListView;

    Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            id = extras.getString("yelp_id");
        } else {
            startActivity(new Intent(DetailsActivity.this, MainActivity.class));
        }

        yelpDetailName = (TextView) findViewById(R.id.nameTextView);
        closedTextView = (TextView) findViewById(R.id.closedTextView);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        phone = (TextView) findViewById(R.id.phoneTextView);
        reviewsListView = (ListView) findViewById(R.id.reviewsListView);
        new Thread(new DetailTask()).start();

    }

    public void showOnMap(View view) {
        Intent intent = new Intent(DetailsActivity.this, MapsActivity.class);

        intent.putExtra("yelp_lat", latitude);
        intent.putExtra("yelp_lng", longitude);
        intent.putExtra("yelp_name", name);
        startActivity(intent);
    }

    public class DetailTask implements Runnable {
        // Modify AndroidManifest.xml so, that your application has access to internet
        // put all YELP code inside this run-block!
        @Override
        public void run() {
            {
                YelpAPIFactory apiFactory = new YelpAPIFactory(YelpSettings.CONSUMER_KEY, YelpSettings.CONSUMER_SECRET, YelpSettings.TOKEN, YelpSettings.TOKEN_SECRET);

                YelpAPI yelpAPI = apiFactory.createAPI();


                Call call = yelpAPI.getBusiness(id);


                try {
                    Response<Business> response = call.execute();

                    Callback<Business> callback = new Callback<Business>() {
                        @Override
                        public void onResponse(Call<Business> call, Response<Business> response) {
                            Business searchResponse = response.body();

                            ArrayList<Review> reviews = searchResponse.reviews();
                            Log.d("DetailsActivity", "Length bfore: " + searchResponse.reviewCount());
                            ArrayList<String> reviewStrings = new ArrayList<>();

                            for(Review r : reviews) {
                                reviewStrings.add(r.user().name() + " gave " + r.rating().toString() + "/5.0\n" + r.excerpt());
                            }

                            Log.d("DetailsActivity", "Lenghts.<: " + reviewStrings.size());

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, reviewStrings);
                            reviewsListView.setAdapter(adapter);
                            // Update UI text with the searchResponse.

                            //Log.d("YELP", "Number of results" + searchResponse.name() + "");
                            yelpDetailName.setText(searchResponse.name());
                            ratingBar.setRating(searchResponse.rating().floatValue());
                            phone.setText(searchResponse.phone());

                            if (searchResponse.isClosed()) {
                                closedTextView.setText("Closed");
                            } else {
                                closedTextView.setText("OPEN");
                            }

                            longitude = searchResponse.location().coordinate().longitude();
                            latitude = searchResponse.location().coordinate().latitude();
                            name = searchResponse.name();
                        }

                        @Override
                        public void onFailure(Call<Business> call, Throwable t) {
                            Log.e("YELP", "Something went wrong");
                        }

                    };

                    call.clone().enqueue(callback);

                } catch (Exception e) {
                    Log.d("YELP", e.toString());

                   /* final String message = e.toString();

                    errorMessages.post(new Runnable() {
                        public void run() {
                            errorMessages.setText(message);
                            mRecyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            errorMessages.setVisibility(View.VISIBLE);
                        }
                    });*/

                }

            }
        }
    }
}