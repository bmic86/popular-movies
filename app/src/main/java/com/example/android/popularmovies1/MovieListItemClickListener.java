package com.example.android.popularmovies1;

import com.example.android.popularmovies1.data.entities.MovieListItem;
import com.example.android.popularmovies1.activities.MovieDetailsActivity;
import com.example.android.popularmovies1.activities.MoviesListActivity;

public class MovieListItemClickListener {

    private MoviesListActivity moviesListActivity;

    public MovieListItemClickListener(MoviesListActivity moviesListActivity) {
        this.moviesListActivity = moviesListActivity;
    }

    public void onClick(MovieListItem movie) {
        MovieDetailsIntent intent = new MovieDetailsIntent(moviesListActivity, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsIntent.MOVIE_LIST_ITEM, movie);
        moviesListActivity.startActivity(intent);
    }

}
