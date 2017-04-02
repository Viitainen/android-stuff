package com.example.arto.yelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    public TextView errorMessages = null;
    public EditText searchTermEditText = null;
    public EditText searchLocationEditText = null;
    public ProgressBar progressBar = null;
    private RecyclerView mRecyclerView;
    private List<FeedItem> feedItemList = new ArrayList<FeedItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorMessages = (TextView) findViewById(R.id.errorMessages);
        searchTermEditText = (EditText) findViewById(R.id.searchTerm);
        searchLocationEditText = (EditText) findViewById(R.id.searchLocation);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(this, new
                    RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            MyRecyclerAdapter mra =
                                    (MyRecyclerAdapter)mRecyclerView.getAdapter();
                            FeedItem fi = mra.getSingleItem(position);
                            Intent intent = new Intent(MainActivity.this,
                                    DetailsActivity.class);
                            intent.putExtra("yelp_id", fi.getId());
                            startActivity(intent);
                        }
                    })
        );
    }

    public void performSearch(View view) {
        mRecyclerView.setVisibility(View.GONE);
        errorMessages.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Task()).start();
    }

    public class Task implements Runnable {
        // Modify AndroidManifest.xml so, that your application has access to internet
        // put all YELP code inside this run-block!
        @Override
        public void run() {
            {
                YelpAPIFactory apiFactory =
                        new YelpAPIFactory(YelpSettings.CONSUMER_KEY, YelpSettings.CONSUMER_SECRET,
                                YelpSettings.TOKEN, YelpSettings.TOKEN_SECRET);

                YelpAPI yelpAPI = apiFactory.createAPI();

                Map<String, String> params = new HashMap<>();

                // general params
                params.put("term", searchTermEditText.getText().toString());
                params.put("limit", "20");
                params.put("lang", "en");
                params.put("signature_method", "hmac-sha1");
                Call<SearchResponse> call = yelpAPI.search(searchLocationEditText.getText().toString(), params);

                try {
                    Response<SearchResponse> response = call.execute();

                    Callback<SearchResponse> callback = new Callback<SearchResponse>() {
                        @Override
                        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                            SearchResponse searchResponse = response.body();
                            // Update UI text with the searchResponse.

                            String results = "";

                            feedItemList = new ArrayList<FeedItem>();

                            for(int a = 0; a < searchResponse.businesses().size(); a++)
                            {
                                FeedItem item = new FeedItem();
                                item.setTitle(searchResponse.businesses().get(a).name());
                                item.setAddress("");

                                if(searchResponse.businesses().get(a).location().address().size() > 0) {
                                    item.setAddress(searchResponse.businesses().get(a).location().address().get(0));
                                }

                                item.setThumbnail(searchResponse.businesses().get(a).imageUrl());
                                item.setRating(searchResponse.businesses().get(a).rating());
                                item.setReviews(searchResponse.businesses().get(a).reviewCount());
                                item.setId(searchResponse.businesses().get(a).id());
                                item.setLatitude(searchResponse.businesses().get(a).location().coordinate().latitude());
                                item.setLongitude(searchResponse.businesses().get(a).location().coordinate().longitude());
                                String categories = "";

                                if(searchResponse.businesses().get(a).categories() != null) {
                                    for (int i = 0; i < searchResponse.businesses().get(a).categories().size(); i++) {
                                        categories += searchResponse.businesses().get(a).categories().get(i).name();
                                        if ((i + 1) != searchResponse.businesses().get(a).categories().size()) {
                                            categories += ", ";
                                        }
                                    }
                                }

                                item.setContentCategory(categories);

                                feedItemList.add(item);
                            }

                            Log.d("YELP", "Count: " + feedItemList.size());


                            // set adapter to recycler view
                            MyRecyclerAdapter adapter = new MyRecyclerAdapter(MainActivity.this, feedItemList);
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            adapter.notifyDataSetChanged();
                            mRecyclerView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            //tv.setText(results);
                        }

                        @Override
                        public void onFailure(Call<SearchResponse> call, Throwable t) {
                            errorMessages.setText("Something went wrong. Try again!");
                            mRecyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            errorMessages.setVisibility(View.VISIBLE);
                        }
                    };

                    call.clone().enqueue(callback);

                } catch (Exception e) {
                    Log.d("YELP", e.toString());

                    final String message = e.toString();

                    errorMessages.post(new Runnable() {
                        public void run() {
                            errorMessages.setText(message);
                            mRecyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            errorMessages.setVisibility(View.VISIBLE);
                        }
                    });

                }

            }
        }
    }
}
