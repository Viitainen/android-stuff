package com.example.a.harjoitus2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by a on 6.2.2017.
 */

public class PostAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Post> mDataSource;

    public PostAdapter(Context context, ArrayList<Post> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Post post = (Post) getItem(position);

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_post, parent, false);

            holder = new ViewHolder();
            holder.authorTextView  = (TextView) convertView.findViewById(R.id.post_author_tv);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.post_title_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        TextView authorTextView = holder.authorTextView;

        titleTextView.setText(post.title);
        authorTextView.setText(post.author);

        return convertView;
    }

    private static class ViewHolder {
        public TextView authorTextView;
        public TextView titleTextView;
    }
}
