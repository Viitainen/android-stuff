package com.example.arto.yelp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.arto.yelp.FeedItem;
import com.example.arto.yelp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

// RecyclerView requires a customized adapter before the data can be used in it
public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {

    // data to  be used
    private List<FeedItem> feedItemList;

    public FeedItem getSingleItem(int position)
    {
        return feedItemList.get(position);
    }

    private Context mContext;

    public MyRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        FeedItem feedItem = feedItemList.get(i);

        // we're using Picasso to handle picture management.
        // if thumbnail not found, use placeholder image instead
        Picasso.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.thumbnail);

        feedListRowHolder.title.setText(Html.fromHtml(feedItem.getTitle()));
        feedListRowHolder.address.setText(Html.fromHtml(feedItem.getAddress()));
        feedListRowHolder.categories.setText(Html.fromHtml(feedItem.getContentCategory()));
        feedListRowHolder.rating.setRating(feedItem.getRating().floatValue());



        String reviews = feedItem.getReviews() + "";
        reviews += " review";

        if(feedItem.getReviews() != 1)
        {
            reviews += "s";
        }

        feedListRowHolder.reviews.setText(reviews);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


}

