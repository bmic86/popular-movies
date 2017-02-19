package com.example.android.popularmovies1.utils;


import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public final class MoviesUrlBuilder {

    /* TODO: Put your API key string here. */
    private final static String API_KEY = "";
    private final static String API_KEY_QUERY = "api_key";

    private final static String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private MoviesUrlBuilder() {
    }


    public static URL buildPopularMoviesURL(String sortOrder) {
        return getUrl(sortOrder);
    }

    public static URL buildMovieDetailsURL(int movieId) {
        return getUrl(String.valueOf(movieId));
    }

    private static URL getUrl(String key) {
        Uri uri = Uri.parse(BASE_URL + key).buildUpon()
                .appendQueryParameter(API_KEY_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
