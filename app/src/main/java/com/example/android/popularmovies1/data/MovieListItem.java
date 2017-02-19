package com.example.android.popularmovies1.data;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieListItem {

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

    private String posterUrlPath;
    private long id;
    private String originalTitle;
    private String overview;
    private String voteAverage;
    private String releaseDate;

    public MovieListItem(JSONObject json) throws JSONException {
        if(json != null) {
            id = json.getLong("id");
            posterUrlPath = BASE_POSTER_URL + json.getString("poster_path");
            originalTitle =  json.getString("original_title");
            overview = json.getString("overview");
            voteAverage = json.getString("vote_average");
            releaseDate = json.getString("release_date");
        }
    }

    public long getId() {
        return id;
    }

    public String getPosterUrlPath() {
        return posterUrlPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

}
