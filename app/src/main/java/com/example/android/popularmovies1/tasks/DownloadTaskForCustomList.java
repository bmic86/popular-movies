package com.example.android.popularmovies1.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies1.data.entities.PageInfo;
import com.example.android.popularmovies1.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;


public class DownloadTaskForCustomList extends AsyncTask<URL, Void, String[]> {

    private DownloadTaskForCustomListListener listener;

    private PageInfo pageInfo;

    public DownloadTaskForCustomList(DownloadTaskForCustomListListener listener, PageInfo pageInfo) {
        this.listener = listener;
        this.pageInfo = pageInfo;
    }

    @Override
    protected void onPreExecute() {
        listener.onStartDownloadingData();
    }

    @Override
    protected String[] doInBackground(URL... urls) {
        String[] results = new String[urls.length];
        for(int i=0; i<urls.length; ++i) {
            try {
                results[i] = NetworkUtils.getResponseFromHttpUrl(urls[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    protected void onPostExecute(String[] data) {
        listener.onStartProcessingData();

        if(data != null) {
            for (String s : data) {
                if(s == null || s.isEmpty()) {
                    Log.e(DownloadTask.class.getName(), "Downloaded data is incomplete.");
                }
            }

            try {
                listener.onDataProcess(data, pageInfo);
            } catch (JSONException e) {
                listener.onDataProcessError();
                Log.e(DownloadTask.class.getName(), "Unable to parse downloaded result.");
            }
        } else {
            listener.onDataProcessError();
            Log.e(DownloadTask.class.getName(), "Downloaded data is empty.");
        }
    }
}
