package com.example.android.popularmovies1.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieListItem implements Parcelable {

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

    public MovieListItem(Parcel parcel) {
        id = parcel.readLong();
        posterUrlPath = parcel.readString();
        originalTitle = parcel.readString();
        releaseDate = parcel.readString();
        overview = parcel.readString();
        voteAverage = parcel.readString();
    }

    public static final Parcelable.Creator<MovieListItem> CREATOR
            = new Parcelable.Creator<MovieListItem>() {
        public MovieListItem createFromParcel(Parcel in) {
            return new MovieListItem(in);
        }

        public MovieListItem[] newArray(int size) {
            return new MovieListItem[size];
        }
    };


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(posterUrlPath);
        parcel.writeString(originalTitle);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeString(voteAverage);
    }
}
