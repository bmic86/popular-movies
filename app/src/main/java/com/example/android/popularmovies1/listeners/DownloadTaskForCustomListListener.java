package com.example.android.popularmovies1.listeners;


import com.example.android.popularmovies1.data.entities.PageInfo;
import com.example.android.popularmovies1.listeners.BaseDownloadTaskListener;

import org.json.JSONException;

public interface DownloadTaskForCustomListListener extends BaseDownloadTaskListener {

    void onDataProcess(String[] data, PageInfo pageInfo) throws JSONException;

}
