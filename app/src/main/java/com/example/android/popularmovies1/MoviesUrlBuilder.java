package com.example.android.popularmovies1;


import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public final class MoviesUrlBuilder {

    /* TODO: Put your API key string here. */
    private final static String API_KEY = "";

    private final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String POPULAR = "popular";
    private final static String SORT_BY_QUERY = "sort_by";
    private final static String API_KEY_QUERY = "api_key";

    private MoviesUrlBuilder() {
    }


    public static URL buildPopularMoviesURL(String sortOrder) {
        Uri uri = Uri.parse(BASE_URL + POPULAR).buildUpon()
                .appendQueryParameter(SORT_BY_QUERY, sortOrder)
                .appendQueryParameter(API_KEY_QUERY, API_KEY)
                .build();
        return buildURL(uri);
    }

    public static URL buildMovieDetailsURL(int movieId) {
        Uri uri = Uri.parse(BASE_URL + String.valueOf(movieId)).buildUpon()
                .appendQueryParameter(API_KEY_QUERY, API_KEY)
                .build();
        return buildURL(uri);
    }

    private static URL buildURL(Uri uri) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
