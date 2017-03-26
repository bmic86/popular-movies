package com.example.android.popularmovies1.data.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies1.R;
import com.example.android.popularmovies1.data.entities.MovieReviewListItem;
import com.example.android.popularmovies1.data.entities.PageInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private ArrayList<MovieReviewListItem> reviews;
    private PageInfo pageInfo;

    public ReviewsAdapter() {
        this.reviews = new ArrayList<>();
        this.pageInfo = new PageInfo();
    }

    public ReviewsAdapter(ArrayList<MovieReviewListItem> reviews, PageInfo pageInfo) {
        this.reviews = reviews;
        this.pageInfo = pageInfo;
    }

    public ReviewsAdapter(String data) throws JSONException {
        this.reviews = new ArrayList<>();
        this.pageInfo = new PageInfo();

        if(data != null && !data.isEmpty()) {
            JSONObject root = new JSONObject(data);

            pageInfo.setPageNum(root.getInt("page"));
            pageInfo.setTotalPagesNum(root.getInt("total_pages"));

            JSONArray results = root.getJSONArray("results");
            for (int i=0; i<results.length(); ++i) {
                reviews.add(new MovieReviewListItem(results.getJSONObject(i)));
            }
        }
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.reviews_list_item, parent, false);
        return new ReviewsAdapter.ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public ArrayList<MovieReviewListItem> getReviews() {
        return reviews;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView contentTextView;
        Context context;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nameTextView = (TextView)itemView.findViewById(R.id.tv_review_header);
            contentTextView = (TextView)itemView.findViewById(R.id.tv_review_content);
        }

        public void bind(MovieReviewListItem movieReviewListItem) {
            final String headerBaseText = context.getString(R.string.user_review_header);
            nameTextView.setText(String.format(headerBaseText, movieReviewListItem.getAuthor()));
            contentTextView.setText(movieReviewListItem.getContent());
        }
    }
}
