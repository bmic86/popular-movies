package com.example.android.popularmovies1;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieListItem {

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

    private String posterUrlPath;
    private long id;

    public MovieListItem(JSONObject json) throws JSONException {
        if(json != null) {
            id = json.getLong("id");
            posterUrlPath = BASE_POSTER_URL + json.getString("poster_path");
        }
    }

    public long getId() {
        return id;
    }

    public String getPosterUrlPath() {
        return posterUrlPath;
    }
}
