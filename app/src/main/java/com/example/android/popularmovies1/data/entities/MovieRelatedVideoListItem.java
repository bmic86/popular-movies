package com.example.android.popularmovies1.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieRelatedVideoListItem implements Parcelable {

    private String name;
    private String site;
    private String key;

    public MovieRelatedVideoListItem(JSONObject json) throws JSONException {
        if(json != null) {
            name = json.getString("name");
            site = json.getString("site");
            key = json.getString("key");
        }
    }

    public MovieRelatedVideoListItem(Parcel parcel) {
        name = parcel.readString();
        site = parcel.readString();
        key = parcel.readString();
    }

    public static final Parcelable.Creator<MovieRelatedVideoListItem> CREATOR
            = new Parcelable.Creator<MovieRelatedVideoListItem>() {
        public MovieRelatedVideoListItem createFromParcel(Parcel in) {
            return new MovieRelatedVideoListItem(in);
        }

        public MovieRelatedVideoListItem[] newArray(int size) {
            return new MovieRelatedVideoListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeString(key);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public String getKey() {
        return key;
    }

}
