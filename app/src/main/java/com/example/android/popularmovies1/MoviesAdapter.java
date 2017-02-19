package com.example.android.popularmovies1;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private static int viewHoldersCount = 0;

    private ArrayList<MovieListItem> movies;
    private Context context;

    public MoviesAdapter() {
        movies = new ArrayList<>();
        viewHoldersCount = 0;
    }

    public MoviesAdapter(String data) throws JSONException {
        JSONObject root = new JSONObject(data);
        movies = new ArrayList<>();
        viewHoldersCount = 0;

        JSONArray results = root.getJSONArray("results");
        for(int i=0; i<results.length(); ++i) {
            JSONObject obj = (JSONObject)results.get(i);
            movies.add( new MovieListItem(obj) );
        }
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movies_list_item, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        MovieListItem movie = movies.get(viewHoldersCount++);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + movie.getPosterUrlPath()).into(viewHolder.posterImageView);

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

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        ImageView posterImageView;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
        }

        void bind(MovieListItem movie) {
            Picasso.with(context).load(movie.getPosterUrlPath()).into(posterImageView);
        }
    }

}
