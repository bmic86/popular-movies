package com.example.android.popularmovies1.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieReviewListItem implements Parcelable {

    private String author;
    private String content;

    public MovieReviewListItem(JSONObject json) throws JSONException {
        if(json != null) {
            author = json.getString("author");
            content = json.getString("content");
        }
    }

    public MovieReviewListItem(Parcel parcel) {
        author = parcel.readString();
        content = parcel.readString();
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public static final Parcelable.Creator<MovieReviewListItem> CREATOR
            = new Parcelable.Creator<MovieReviewListItem>() {
        public MovieReviewListItem createFromParcel(Parcel in) {
            return new MovieReviewListItem(in);
        }

        public MovieReviewListItem[] newArray(int size) {
            return new MovieReviewListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }
}
