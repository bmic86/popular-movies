package com.example.android.popularmovies1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies1.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MoviesListActivity extends AppCompatActivity {

    private class DownloadTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setVisibility(View.INVISIBLE);

            if(data != null && !data.isEmpty()) {
                Log.i(DownloadTask.class.getName(), data);
                try {
                    moviesAdapter = new MoviesAdapter(data);
                    recyclerView.setAdapter(moviesAdapter);
                } catch (JSONException e) {
                    Log.e(DownloadTask.class.getName(), "Unable to parse downloaded result.");
                }
            }
        }
    }

    RecyclerView recyclerView;
    MoviesAdapter moviesAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MoviesAdapter());

        URL dataUrl = MoviesUrlBuilder.buildPopularMoviesURL(SortOrder.BY_MOST_POPULAR);
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
