package com.example.android.popularmovies1;

import android.content.Intent;

import com.example.android.popularmovies1.activities.MovieDetailsActivity;
import com.example.android.popularmovies1.activities.MoviesListActivity;


public class MovieDetailsIntent extends Intent {

    public static final String MOVIE_LIST_ITEM = "com.example.android.popularmovies1.MovieDetailsIntent.movieListItem";

    public MovieDetailsIntent(MoviesListActivity moviesListActivity, Class<MovieDetailsActivity> movieDetailsActivityClass) {
        super(moviesListActivity, movieDetailsActivityClass);
    }
}
