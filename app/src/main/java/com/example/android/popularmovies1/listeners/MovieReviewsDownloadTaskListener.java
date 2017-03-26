package com.example.android.popularmovies1.listeners;


import com.example.android.popularmovies1.tasks.MovieReviewsDownloadTaskHandler;

import org.json.JSONException;

public class MovieReviewsDownloadTaskListener implements DownloadTaskListener {

    private MovieReviewsDownloadTaskHandler handler;

    public MovieReviewsDownloadTaskListener(MovieReviewsDownloadTaskHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onDataProcessError() {
        handler.onMovieReviewsProcessError();
    }

    @Override
    public void onStartDownloadingData() {
        handler.onMovieReviewsStartDownloading();
    }

    @Override
    public void onStartProcessingData() {
        handler.onMovieReviewsStartProcessing();
    }

    @Override
    public void onDataProcess(String data) throws JSONException {
        handler.onMovieReviewsProcess(data);
    }
}
