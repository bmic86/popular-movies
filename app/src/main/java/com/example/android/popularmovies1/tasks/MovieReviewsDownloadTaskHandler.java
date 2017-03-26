package com.example.android.popularmovies1.tasks;

import org.json.JSONException;

public interface MovieReviewsDownloadTaskHandler {

    void onMovieReviewsProcessError();

    void onMovieReviewsStartDownloading();

    void onMovieReviewsStartProcessing();

    void onMovieReviewsProcess(String data) throws JSONException;

}
