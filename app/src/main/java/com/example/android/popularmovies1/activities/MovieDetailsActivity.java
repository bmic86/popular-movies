package com.example.android.popularmovies1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies1.FavoritesListStateManager;
import com.example.android.popularmovies1.MovieDetailsIntent;
import com.example.android.popularmovies1.R;
import com.example.android.popularmovies1.data.FavoriteMoviesHelper;
import com.example.android.popularmovies1.data.adapters.VideosAdapter;
import com.example.android.popularmovies1.data.entities.MovieListItem;
import com.example.android.popularmovies1.data.entities.MovieListItemDetails;
import com.example.android.popularmovies1.data.entities.MovieRelatedVideoListItem;
import com.example.android.popularmovies1.listeners.MovieRelatedVideoListItemClickListener;
import com.example.android.popularmovies1.tasks.DownloadTask;
import com.example.android.popularmovies1.tasks.DownloadTaskListener;
import com.example.android.popularmovies1.utils.MoviesUrlBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends SettingsMenuBaseActivity implements DownloadTaskListener {

    private static final String STORAGE_KEY_VIDEOS = "videos";

    private TextView originalTitleTextView;
    private TextView overviewTextView;
    private TextView voteAverageTextView;
    private TextView releaseDateTextView;
    private ImageView posterImageView;
    private Toast messageToast;
    private ImageButton addToFavoritesButton;

    private RecyclerView videosRecyclerView;
    private VideosAdapter videosAdapter;
    private MovieRelatedVideoListItemClickListener videoClickListener;

    private ProgressBar progressBar;
    private TextView videosErrorTextView;

    private MovieListItemDetails movieListItemDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        originalTitleTextView = (TextView)findViewById(R.id.tv_original_title_value);
        overviewTextView = (TextView)findViewById(R.id.tv_overview_value);
        voteAverageTextView = (TextView)findViewById(R.id.tv_vote_average_value);
        releaseDateTextView = (TextView)findViewById(R.id.tv_release_date_value);
        posterImageView = (ImageView)findViewById(R.id.iv_details_poster);
        addToFavoritesButton = (ImageButton)findViewById(R.id.ib_add_to_favorites);

        Intent intent = getIntent();

        MovieListItem listItem = intent.getParcelableExtra(MovieDetailsIntent.MOVIE_LIST_ITEM);
        originalTitleTextView.setText(listItem.getOriginalTitle());
        overviewTextView.setText(listItem.getOverview());
        voteAverageTextView.setText(listItem.getVoteAverage());
        releaseDateTextView.setText(listItem.getReleaseDate());

        String posterUrlPath = listItem.getPosterUrlPath();
        if(posterUrlPath != null && !posterUrlPath.isEmpty()) {
            Picasso.with(this).load(posterUrlPath).into(posterImageView);
            posterImageView.setVisibility(View.VISIBLE);
        }
        else {
            posterImageView.setVisibility(View.INVISIBLE);
        }

        setFavoriteButtonState(FavoriteMoviesHelper.isMovieFavorite(this, listItem.getId()));

        movieListItemDetails = new MovieListItemDetails(listItem);

        videosRecyclerView = (RecyclerView) findViewById(R.id.rv_videos_list);
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videosRecyclerView.setHasFixedSize(true);
        videoClickListener = new MovieRelatedVideoListItemClickListener(this);
        videosAdapter = new VideosAdapter(videoClickListener);
        videosRecyclerView.setAdapter(videosAdapter);

        progressBar = (ProgressBar) findViewById(R.id.pb_details_loading_indicator);
        videosErrorTextView = (TextView) findViewById(R.id.tv_videos_error);

        loadData(savedInstanceState, listItem.getId());
    }

    private void loadData(Bundle savedInstanceState, long movieId) {
        ArrayList<MovieRelatedVideoListItem> restoredVideosList = null;

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(STORAGE_KEY_VIDEOS)) {
                restoredVideosList = (ArrayList<MovieRelatedVideoListItem>)(ArrayList<?>)savedInstanceState.getParcelableArrayList(STORAGE_KEY_VIDEOS);
            }
        }

        if(restoredVideosList != null) {
            videosAdapter = new VideosAdapter(restoredVideosList, videoClickListener);
            videosRecyclerView.setAdapter(videosAdapter);
        } else {
            URL videosUrl = MoviesUrlBuilder.buildMovieRelatedVideosUrl(movieId);
            new DownloadTask(this).execute(videosUrl);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<MovieRelatedVideoListItem> videos = videosAdapter.getVideos();
        outState.putParcelableArrayList(STORAGE_KEY_VIDEOS, videos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    public void handleLikeButtonClick(View view) {
        boolean isFavorite = FavoriteMoviesHelper.changeMovieFavoritesState(this, movieListItemDetails.getMovieListItem());

        setFavoriteButtonState(isFavorite);
        FavoritesListStateManager.getInstance().setFavoritesListChanged();

        int messageId = (isFavorite) ? R.string.added_to_favorites : R.string.removed_from_favorites;
        ShowMessageToast(messageId);
    }

    private void setFavoriteButtonState(boolean isFavorite) {
        int imageId = (isFavorite) ? R.drawable.ic_favorite_48dp : R.drawable.ic_favorite_border_48dp;
        addToFavoritesButton.setImageResource(imageId);
    }

    private void ShowMessageToast(int messageId) {
        if(messageToast != null) {
            messageToast.cancel();
        }

        messageToast = Toast.makeText(this, messageId, Toast.LENGTH_SHORT);
        messageToast.show();
    }

    @Override
    public void onDataProcessError() {
        showVideosError();
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
    public void onDataProcess(String data) throws JSONException {
        hideVideosError();
        videosAdapter = new VideosAdapter(data, videoClickListener);
        videosRecyclerView.setAdapter(videosAdapter);
    }

    private void showVideosError() {
        videosRecyclerView.setVisibility(View.INVISIBLE);
        videosErrorTextView.setVisibility(View.VISIBLE);
    }

    private void hideVideosError() {
        videosRecyclerView.setVisibility(View.VISIBLE);
        videosErrorTextView.setVisibility(View.INVISIBLE);
    }
}
