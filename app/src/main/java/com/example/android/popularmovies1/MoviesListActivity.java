package com.example.android.popularmovies1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies1.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MoviesListActivity extends AppCompatActivity {

    private class DownloadTask extends AsyncTask<URL, Void, String> {

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
            Log.i(DownloadTask.class.getName(), data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        URL dataUrl = MoviesUrlBuilder.buildPopularMoviesURL(SortOrder.BY_MOST_POPULAR);
        new DownloadTask().execute(dataUrl);

        dataUrl = MoviesUrlBuilder.buildMovieDetailsURL(2);
        new DownloadTask().execute(dataUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_most_popular) {
            URL dataUrl = MoviesUrlBuilder.buildPopularMoviesURL(SortOrder.BY_MOST_POPULAR);
            new DownloadTask().execute(dataUrl);
            return true;
        } else if (id == R.id.item_highest_rated) {
            URL dataUrl = MoviesUrlBuilder.buildPopularMoviesURL(SortOrder.BY_HIGHEST_RATED);
            new DownloadTask().execute(dataUrl);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
