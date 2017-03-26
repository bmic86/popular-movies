package com.example.android.popularmovies1.tasks;

import org.json.JSONException;

public interface MovieRelatedVideosDownloadTaskHandler {

    void onMovieRelatedVideosProcessError();

    void onMovieRelatedVideosStartDownloading();

    void onMovieRelatedVideosStartProcessing();

    void onMovieRelatedVideosProcess(String data) throws JSONException;

}
