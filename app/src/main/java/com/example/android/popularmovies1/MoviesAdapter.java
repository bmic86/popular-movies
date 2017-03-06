package com.example.android.popularmovies1;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies1.data.MovieListItem;
import com.example.android.popularmovies1.data.PageInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private ArrayList<MovieListItem> movies;
    private Context context;
    private final MovieListItemClickListener clickListener;

    private final PageInfo pageInfo;

    public MoviesAdapter(MovieListItemClickListener clickListener) {
        movies = new ArrayList<>();
        this.clickListener = clickListener;
        pageInfo = new PageInfo(0, 0);
    }

    public MoviesAdapter(String data, MovieListItemClickListener clickListener) throws JSONException {
        movies = new ArrayList<>();
        this.clickListener = clickListener;

        JSONObject root = new JSONObject(data);
        pageInfo = new PageInfo(root.getInt("page"), root.getInt("total_pages"));

        JSONArray results = root.getJSONArray("results");
        for(int i=0; i<results.length(); ++i) {
            JSONObject obj = (JSONObject)results.get(i);
            movies.add( new MovieListItem(obj) );
        }
    }

    public MoviesAdapter(ArrayList<MovieListItem> movies, PageInfo pageInfo, MovieListItemClickListener clickListener) {
        this.movies = movies;
        this.pageInfo = pageInfo;
        this.clickListener = clickListener;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public ArrayList<MovieListItem> getMovies() {
        return movies;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movies_list_item, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView posterImageView;
        MovieListItem listItem;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        void bind(MovieListItem movie) {
            listItem = movie;
            Picasso.with(context).load(movie.getPosterUrlPath()).into(posterImageView);
        }

        @Override
        public void onClick(View view) {
            Log.d(this.getClass().getName(), "Clicked on:" + listItem.getId() );
            clickListener.onClick(listItem);
        }
    }

}
