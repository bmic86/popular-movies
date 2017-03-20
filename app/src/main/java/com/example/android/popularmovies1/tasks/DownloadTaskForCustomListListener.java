package com.example.android.popularmovies1.tasks;


import com.example.android.popularmovies1.data.entities.PageInfo;

import org.json.JSONException;

public interface DownloadTaskForCustomListListener extends BaseDownloadTaskListener{

    void onDataProcess(String[] data, PageInfo pageInfo) throws JSONException;

}
