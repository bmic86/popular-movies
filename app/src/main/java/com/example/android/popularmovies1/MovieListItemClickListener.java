package com.example.android.popularmovies1;

import android.content.Intent;

import com.example.android.popularmovies1.data.MovieListItem;
import com.example.android.popularmovies1.activities.MovieDetailsActivity;
import com.example.android.popularmovies1.activities.MoviesListActivity;

public class MovieListItemClickListener {

    private MoviesListActivity moviesListActivity;

    public MovieListItemClickListener(MoviesListActivity moviesListActivity) {
        this.moviesListActivity = moviesListActivity;
    }

    public void onClick(MovieListItem movie) {
        MovieDetailsIntent intent = new MovieDetailsIntent(moviesListActivity, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsIntent.ORIGINAL_TITLE, movie.getOriginalTitle());
        intent.putExtra(MovieDetailsIntent.OVERVIEW, movie.getOverview());
        intent.putExtra(MovieDetailsIntent.POSTER_URL_PATH, movie.getPosterUrlPath());
        intent.putExtra(MovieDetailsIntent.RELEASE_DATE, movie.getReleaseDate());
        intent.putExtra(MovieDetailsIntent.VOTE_AVERAGE, movie.getVoteAverage());
        moviesListActivity.startActivity(intent);
    }

}
