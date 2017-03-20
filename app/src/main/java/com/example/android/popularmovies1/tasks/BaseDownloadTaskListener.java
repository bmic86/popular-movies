package com.example.android.popularmovies1.tasks;

public interface BaseDownloadTaskListener {

    void onDataProcessError();
    void onStartDownloadingData();
    void onStartProcessingData();

}
