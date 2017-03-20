package com.example.android.popularmovies1.tasks;


import org.json.JSONException;

public interface DownloadTaskListener extends BaseDownloadTaskListener{

    void onDataProcess(String data) throws JSONException;

}
