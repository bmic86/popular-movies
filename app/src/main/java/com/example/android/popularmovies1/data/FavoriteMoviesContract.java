package com.example.android.popularmovies1.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class FavoriteMoviesContract {

    public static final String AUTHORITY = "com.example.android.popularmovies1";

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteMoviesContractEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY).buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "FAVORITE_MOVIES";

        public static final String COLUMN_TITLE = "TITLE";
    }

}
