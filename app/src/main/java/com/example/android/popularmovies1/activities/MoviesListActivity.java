package com.example.android.popularmovies1.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies1.FavoritesListStateManager;
import com.example.android.popularmovies1.PagedDataDownloader;
import com.example.android.popularmovies1.listeners.MovieListItemClickListener;
import com.example.android.popularmovies1.data.adapters.MoviesAdapter;
import com.example.android.popularmovies1.listeners.DownloadTaskForCustomListListener;
import com.example.android.popularmovies1.listeners.DownloadTaskListener;
import com.example.android.popularmovies1.data.FavoriteMoviesHelper;
import com.example.android.popularmovies1.data.entities.FavoriteMoviesList;
import com.example.android.popularmovies1.data.entities.MovieListItem;
import com.example.android.popularmovies1.data.entities.PageInfo;
import com.example.android.popularmovies1.listeners.PageChangeClickListener;
import com.example.android.popularmovies1.tasks.DownloadTask;
import com.example.android.popularmovies1.tasks.DownloadTaskForCustomList;
import com.example.android.popularmovies1.utils.AppSettings;
import com.example.android.popularmovies1.utils.MoviesUrlBuilder;
import com.example.android.popularmovies1.R;
import com.example.android.popularmovies1.data.MoviesListMode;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

public class MoviesListActivity extends SettingsMenuBaseActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        DownloadTaskListener,
        DownloadTaskForCustomListListener,
        PagedDataDownloader {

    private static final String STORAGE_KEY_MOVIES = "movies";
    private static final String STORAGE_KEY_PAGEINFO = "pageinfo";
    private static final String STORAGE_KEY_MODE = "mode";

    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private ProgressBar progressBar;
    private MovieListItemClickListener listener;
    private TextView errorTextView;
    private Menu menu;

    private TextView moviesListHeaderTextView;

    private Button prevPageButton;
    private Button nextPageButton;
    private TextView pageNumTextView;

    private MoviesListMode mode;

    @Override
    public void onDataProcess(String data) throws JSONException {
        hideError();
        moviesAdapter = new MoviesAdapter(data, listener);
        updatePageNumber(moviesAdapter.getPageInfo());
        recyclerView.setAdapter(moviesAdapter);

        moviesListHeaderTextView.setText(mode.toListDisplayHeader(getBaseContext()));
    }

    @Override
    public void onDataProcessError() {
        showError();
    }

    @Override
    public void onStartDownloadingData() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartProcessingData() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDataProcess(String[] data, PageInfo pageInfo) throws JSONException {
        moviesAdapter = new MoviesAdapter(pageInfo, listener);
        moviesAdapter.addMovies(data);
        updatePageNumber(pageInfo);
        recyclerView.setAdapter(moviesAdapter);

        moviesListHeaderTextView.setText(mode.toListDisplayHeader(getBaseContext()));
    }

    private void hideError() {
        errorTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        moviesListHeaderTextView.setVisibility(View.VISIBLE);
        setPagingToolsVisibility(View.VISIBLE);
    }

    private void showError() {
        errorTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        moviesListHeaderTextView.setVisibility(View.INVISIBLE);
        setPagingToolsVisibility(View.INVISIBLE);
    }

    private void setPagingToolsVisibility(int visibility) {
        pageNumTextView.setVisibility(visibility);
        prevPageButton.setVisibility(visibility);
        nextPageButton.setVisibility(visibility);
    }


    public void updatePageNumber(PageInfo pageNumber) {
        int pageNum = pageNumber.getPageNum();
        int totalPagesNum = pageNumber.getTotalPagesNum();

        pageNumTextView.setText(String.format(getString(R.string.pageNumberInfo), pageNum, totalPagesNum));
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

    @Override
    public void downloadData(int page) {
        if(mode != MoviesListMode.FAVORITES) {
            URL dataUrl = MoviesUrlBuilder.buildPopularMoviesURL(mode.toURLParam(), page);
            new DownloadTask(this).execute(dataUrl);
        } else {
            FavoriteMoviesList moviesList = FavoriteMoviesHelper.getFavoriteMoviesList(this, page);
            URL[] dataUrls = MoviesUrlBuilder.buildFavoriteMoviesUrls(moviesList.getMovieIds());
            new DownloadTaskForCustomList(this, moviesList.getPageInfo()).execute(dataUrls);
        }
    }
    @Override
    public PageInfo getPageInfo() {
        return moviesAdapter.getPageInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        progressBar = (ProgressBar) findViewById(R.id.pb_movies_loading_indicator);
        errorTextView = (TextView) findViewById(R.id.tv_movies_error);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        listener = new MovieListItemClickListener(this);
        moviesAdapter = new MoviesAdapter(listener);
        recyclerView.setAdapter(moviesAdapter);

        nextPageButton = (Button) findViewById(R.id.btn_page_next);
        nextPageButton.setOnClickListener(new PageChangeClickListener(1, this));

        prevPageButton = (Button) findViewById(R.id.btn_page_prev);
        prevPageButton.setOnClickListener(new PageChangeClickListener(-1, this));

        pageNumTextView = (TextView) findViewById(R.id.tv_page_num);

        moviesListHeaderTextView = (TextView) findViewById(R.id.tv_movies_list_header);

        setDefaultMode();
        loadData(savedInstanceState);
    }

    private void setDefaultMode() {
        if(AppSettings.isShowFavorites(this)) {
            mode = MoviesListMode.FAVORITES;
        } else {
            mode = MoviesListMode.BY_MOST_POPULAR;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(getString(R.string.show_favorites_key).equals(key)) {
            updateMenuOptions();
            FavoritesListStateManager.getInstance().setFavoritesEnabledChanged();
        }
    }

    private void updateMenuOptions() {
        boolean showFavorites = AppSettings.isShowFavorites(this);
        MenuItem item = menu.findItem(R.id.item_favorites);
        item.setVisible(showFavorites);
    }

    private void loadData(Bundle savedInstanceState) {
        ArrayList<MovieListItem> moviesRestored = null;
        PageInfo pageInfoRestored = null;
        Integer modeRestored = null;

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(STORAGE_KEY_MOVIES)) {
                moviesRestored = (ArrayList<MovieListItem>)(ArrayList<?>)(savedInstanceState.getParcelableArrayList(STORAGE_KEY_MOVIES));
            }
            if(savedInstanceState.containsKey(STORAGE_KEY_PAGEINFO)) {
                pageInfoRestored = savedInstanceState.getParcelable(STORAGE_KEY_PAGEINFO);
            }
            if(savedInstanceState.containsKey(STORAGE_KEY_MODE)) {
                modeRestored = savedInstanceState.getInt(STORAGE_KEY_MODE);
            }
        }

        if(moviesRestored != null && pageInfoRestored != null && modeRestored != null) {
            moviesAdapter = new MoviesAdapter(moviesRestored, pageInfoRestored, listener);
            updatePageNumber(moviesAdapter.getPageInfo());
            recyclerView.setAdapter(moviesAdapter);
            mode = MoviesListMode.values()[modeRestored];

            moviesListHeaderTextView.setText(mode.toListDisplayHeader(getBaseContext()));
        }
        else {
            downloadData( pageInfoRestored != null ? pageInfoRestored.getPageNum() : 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FavoritesListStateManager.getInstance().getAndResetFavoritesEnabledChanged()) {
            setDefaultMode();
            downloadData(1);
        } else if (FavoritesListStateManager.getInstance().getAndResetFavoritesListChanged()) {
            downloadData(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        updateMenuOptions();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.item_most_popular) {
            mode = MoviesListMode.BY_MOST_POPULAR;
            downloadData(1);
            return true;
        }
        else if (id == R.id.item_highest_rated) {
            mode = MoviesListMode.BY_HIGHEST_RATED;
            downloadData(1);
            return true;
        } else if (id == R.id.item_favorites) {
            mode = MoviesListMode.FAVORITES;
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

        if(movies != null && movies.size() > 0) {
            outState.putParcelableArrayList(STORAGE_KEY_MOVIES, movies);
            outState.putParcelable(STORAGE_KEY_PAGEINFO, pageInfo);
        }
        outState.putInt(STORAGE_KEY_MODE, mode.getValue());
    }
}
