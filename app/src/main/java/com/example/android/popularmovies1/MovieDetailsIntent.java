package com.example.android.popularmovies1;

import android.content.Intent;

import com.example.android.popularmovies1.activities.MovieDetailsActivity;
import com.example.android.popularmovies1.activities.MoviesListActivity;


public class MovieDetailsIntent extends Intent {

    public static final String ORIGINAL_TITLE = "com.example.android.popularmovies1.MovieDetailsIntent.originalTitle";
    public static final String OVERVIEW = "com.example.android.popularmovies1.MovieDetailsIntent.overview";
    public static final String POSTER_URL_PATH = "com.example.android.popularmovies1.MovieDetailsIntent.posterUrlPath";
    public static final String RELEASE_DATE = "com.example.android.popularmovies1.MovieDetailsIntent.releaseDate";
    public static final String VOTE_AVERAGE = "com.example.android.popularmovies1.MovieDetailsIntent.voteAverage";

    public MovieDetailsIntent(MoviesListActivity moviesListActivity, Class<MovieDetailsActivity> movieDetailsActivityClass) {
        super(moviesListActivity, movieDetailsActivityClass);
    }
}
