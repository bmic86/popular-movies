package com.example.android.popularmovies1.data;

import android.content.Context;

import com.example.android.popularmovies1.R;

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

    public String toListDisplayHeader(Context context) {
        switch (this) {
            case BY_MOST_POPULAR:
                return context.getString(R.string.most_popular_list_header);

            case BY_HIGHEST_RATED:
                return context.getString(R.string.top_rated_list_header);

            case FAVORITES:
                return context.getString(R.string.favorites_list_header);

            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }
}
