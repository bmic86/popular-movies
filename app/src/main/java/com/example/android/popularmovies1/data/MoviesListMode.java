package com.example.android.popularmovies1.data;

public enum MoviesListMode {

    FAVORITES(0),
    BY_MOST_POPULAR(1),
    BY_HIGHEST_RATED(2);

    private int value;

    MoviesListMode(int value) {
        this.value = value;
    }

    public String toURLParam() {
        switch (this) {
            case BY_MOST_POPULAR:
                return "popular";

            case BY_HIGHEST_RATED:
                return "top_rated";

            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }
}
