package com.example.android.popularmovies1.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies1.listeners.DownloadTaskListener;
import com.example.android.popularmovies1.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;


public class DownloadTask extends AsyncTask<URL, Void, String> {

    private DownloadTaskListener listener;


    public DownloadTask(DownloadTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onStartDownloadingData();
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL searchUrl = urls[0];
        String results = null;
        try {
            results = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(String data) {
        listener.onStartProcessingData();

        if(data != null && !data.isEmpty()) {
            Log.i(DownloadTask.class.getName(), data);
            try {
                listener.onDataProcess(data);
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
