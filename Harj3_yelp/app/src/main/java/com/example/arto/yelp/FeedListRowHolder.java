package com.example.arto.yelp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class FeedListRowHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;
    protected TextView address;
    protected TextView reviews;
    protected TextView categories;
    protected RatingBar rating;

    public FeedListRowHolder(View view) {
        super(view);
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.title = (TextView) view.findViewById(R.id.title);
        this.address = (TextView) view.findViewById(R.id.address);
        this.reviews = (TextView) view.findViewById(R.id.reviews);
        this.categories = (TextView) view.findViewById(R.id.categories);
        this.rating = (RatingBar) view.findViewById(R.id.rating);

    }

}