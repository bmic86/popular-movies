package com.example.android.popularmovies1.listeners;

public interface BaseDownloadTaskListener {

    void onDataProcessError();
    void onStartDownloadingData();
    void onStartProcessingData();

}
