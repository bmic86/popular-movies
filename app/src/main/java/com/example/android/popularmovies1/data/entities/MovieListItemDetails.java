package com.example.android.popularmovies1.data.entities;

public class MovieListItemDetails {

    private MovieListItem movieListItem;

    public MovieListItemDetails(MovieListItem movieListItem) {
        this.movieListItem = movieListItem;
    }

    public MovieListItem getMovieListItem() {
        return movieListItem;
    }

    public void setMovieListItem(MovieListItem movieListItem) {
        this.movieListItem = movieListItem;
    }

}
