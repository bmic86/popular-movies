package com.example.android.popularmovies1.listeners;


import com.example.android.popularmovies1.tasks.MovieRelatedVideosDownloadTaskHandler;

import org.json.JSONException;

public class MovieRelatedVideosDownloadTaskListener implements DownloadTaskListener {

    private MovieRelatedVideosDownloadTaskHandler handler;

    public MovieRelatedVideosDownloadTaskListener(MovieRelatedVideosDownloadTaskHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onDataProcessError() {
        handler.onMovieRelatedVideosProcessError();
    }

    @Override
    public void onStartDownloadingData() {
        handler.onMovieRelatedVideosStartDownloading();
    }

    @Override
    public void onStartProcessingData() {
        handler.onMovieRelatedVideosStartProcessing();
    }

    @Override
    public void onDataProcess(String data) throws JSONException {
        handler.onMovieRelatedVideosProcess(data);
    }
}
