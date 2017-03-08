package com.example.android.popularmovies1.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies1.MovieListItemClickListener;
import com.example.android.popularmovies1.MoviesAdapter;
import com.example.android.popularmovies1.data.entities.MovieListItem;
import com.example.android.popularmovies1.data.entities.PageInfo;
import com.example.android.popularmovies1.utils.MoviesUrlBuilder;
import com.example.android.popularmovies1.R;
import com.example.android.popularmovies1.data.SortOrder;
import com.example.android.popularmovies1.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MoviesListActivity extends SettingsMenuBaseActivity {

    private static final String STORAGE_KEY_MOVIES = "movies";
    private static final String STORAGE_KEY_PAGEINFO = "pageinfo";

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
                    hideError();
                    moviesAdapter = new MoviesAdapter(data, listener);
                    updatePageNumber(moviesAdapter.getPageInfo());
                    recyclerView.setAdapter(moviesAdapter);
                } catch (JSONException e) {
                    updateViewOnError();
                    Log.e(DownloadTask.class.getName(), "Unable to parse downloaded result.");
                }
            } else {
                updateViewOnError();
                Log.e(DownloadTask.class.getName(), "Downloaded data is empty.");
            }
        }
    }

    public class PageChangeClickListener implements View.OnClickListener {
        private int stepSize;


        public PageChangeClickListener(int stepSize) {
            this.stepSize = stepSize;
        }

        @Override
        public void onClick(View view) {
            PageInfo pageInfo = moviesAdapter.getPageInfo();
            int nextPage = pageInfo.getPageNum() + stepSize;
            if( nextPage > 0 && nextPage <= pageInfo.getTotalPagesNum()) {
                downloadData(nextPage);
            }
        }
    }

    RecyclerView recyclerView;
    MoviesAdapter moviesAdapter;
    ProgressBar progressBar;
    MovieListItemClickListener listener;
    TextView errorTextView;

    Button prevPageButton;
    Button nextPageButton;
    TextView pageNumTextView;

    String sortOrder;

    private void hideError() {
        errorTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void updateViewOnError() {
        hidePagingTools();
        showError();
    }

    private void hidePagingTools() {
        pageNumTextView.setVisibility(View.INVISIBLE);
        prevPageButton.setVisibility(View.INVISIBLE);
        nextPageButton.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        errorTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void updatePageNumber(PageInfo pageNumber) {
        int pageNum = pageNumber.getPageNum();
        int totalPagesNum = pageNumber.getTotalPagesNum();

        pageNumTextView.setText(String.format( getString(R.string.pageNumberInfo), pageNum, totalPagesNum));
        pageNumTextView.setVisibility(View.VISIBLE);

        if(pageNum > 1) {
            prevPageButton.setVisibility(View.VISIBLE);
        } else {
            prevPageButton.setVisibility(View.INVISIBLE);
        }

        if(pageNum < totalPagesNum) {
            nextPageButton.setVisibility(View.VISIBLE);
        } else {
            nextPageButton.setVisibility(View.INVISIBLE);
        }
    }

    private void downloadData(int nextPage) {
        URL dataUrl = MoviesUrlBuilder.buildPopularMoviesURL(sortOrder, nextPage);
        new DownloadTask().execute(dataUrl);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        errorTextView = (TextView) findViewById(R.id.tv_error);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        listener = new MovieListItemClickListener(this);
        recyclerView.setAdapter(new MoviesAdapter(listener));

        nextPageButton = (Button) findViewById(R.id.btn_page_next);
        nextPageButton.setOnClickListener(new PageChangeClickListener(1));

        prevPageButton = (Button) findViewById(R.id.btn_page_prev);
        prevPageButton.setOnClickListener(new PageChangeClickListener(-1));

        pageNumTextView = (TextView) findViewById(R.id.tv_page_num);

        sortOrder = SortOrder.BY_MOST_POPULAR;

        loadData(savedInstanceState);
    }

    private void loadData(Bundle savedInstanceState) {
        ArrayList<MovieListItem> moviesRestored = null;
        PageInfo pageInfoRestored = null;

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(STORAGE_KEY_MOVIES)) {
                moviesRestored = (ArrayList<MovieListItem>)(ArrayList<?>)(savedInstanceState.getParcelableArrayList(STORAGE_KEY_MOVIES));
            }
            if(savedInstanceState.containsKey(STORAGE_KEY_PAGEINFO)) {
                pageInfoRestored = savedInstanceState.getParcelable(STORAGE_KEY_PAGEINFO);
            }
        }

        if(moviesRestored != null && pageInfoRestored != null) {
            moviesAdapter = new MoviesAdapter(moviesRestored, pageInfoRestored, listener);
            updatePageNumber(moviesAdapter.getPageInfo());
            recyclerView.setAdapter(moviesAdapter);
        }
        else {
            downloadData( pageInfoRestored != null ? pageInfoRestored.getPageNum() : 1);
        }
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
            sortOrder = SortOrder.BY_MOST_POPULAR;
            downloadData(1);
            return true;
        }
        else if (id == R.id.item_highest_rated) {
            sortOrder = SortOrder.BY_HIGHEST_RATED;
            downloadData(1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<MovieListItem> movies = moviesAdapter.getMovies();
        PageInfo pageInfo = moviesAdapter.getPageInfo();

        outState.putParcelableArrayList(STORAGE_KEY_MOVIES, movies);
        outState.putParcelable(STORAGE_KEY_PAGEINFO, pageInfo);
    }
}
