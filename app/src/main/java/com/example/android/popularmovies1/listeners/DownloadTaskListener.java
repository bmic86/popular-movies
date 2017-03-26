package com.example.android.popularmovies1.listeners;


import com.example.android.popularmovies1.listeners.BaseDownloadTaskListener;

import org.json.JSONException;

public interface DownloadTaskListener extends BaseDownloadTaskListener {

    void onDataProcess(String data) throws JSONException;

}
