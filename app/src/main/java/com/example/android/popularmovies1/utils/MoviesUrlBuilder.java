package com.example.android.popularmovies1.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public final class MoviesUrlBuilder {

    /* TODO: Put your API key string here. */
    private final static String API_KEY = "";
    private final static String API_KEY_QUERY = "api_key";
    private final static String PAGE_QUERY = "page";
    private final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String VIDEOS_QUERY = "videos";
    private final static String REVIEWS_QUERY = "reviews";

    private MoviesUrlBuilder() {
    }


    public static URL buildPopularMoviesURL(String sortOrder, int pageNum) {
        return buildUrl(BASE_URL + sortOrder, pageNum);
    }

    public static URL[] buildFavoriteMoviesUrls(long[] movieIds) {
        URL[] result = new URL[movieIds.length];
        for (int i=0; i<result.length; ++i) {
            result[i] = buildUrl(BASE_URL + String.valueOf(movieIds[i]));
        }
        return result;
    }

    public static URL buildMovieRelatedVideosUrl(long movieId) {
        return buildUrl(BASE_URL + String.valueOf(movieId) + '/' + VIDEOS_QUERY);
    }

    public static URL buildMovieReviewsUrl(long movieId, int pageNum ) {
        return buildUrl(BASE_URL + String.valueOf(movieId) + '/' + REVIEWS_QUERY, pageNum);
    }

    private static URL buildUrl(String baseUrl) {
        Uri uri = Uri.parse(baseUrl).buildUpon()
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

    private static URL buildUrl(String baseUrl, int pageNum) {
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(PAGE_QUERY, String.valueOf(pageNum))
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
