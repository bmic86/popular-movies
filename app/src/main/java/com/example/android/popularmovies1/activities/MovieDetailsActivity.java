package com.example.android.popularmovies1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies1.FavoritesListStateManager;
import com.example.android.popularmovies1.MovieDetailsIntent;
import com.example.android.popularmovies1.PagedDataDownloader;
import com.example.android.popularmovies1.R;
import com.example.android.popularmovies1.data.FavoriteMoviesHelper;
import com.example.android.popularmovies1.data.adapters.ReviewsAdapter;
import com.example.android.popularmovies1.data.adapters.VideosAdapter;
import com.example.android.popularmovies1.data.entities.MovieListItem;
import com.example.android.popularmovies1.data.entities.MovieRelatedVideoListItem;
import com.example.android.popularmovies1.data.entities.MovieReviewListItem;
import com.example.android.popularmovies1.data.entities.PageInfo;
import com.example.android.popularmovies1.listeners.MovieRelatedVideoListItemClickListener;
import com.example.android.popularmovies1.listeners.MovieRelatedVideosDownloadTaskListener;
import com.example.android.popularmovies1.listeners.MovieReviewsDownloadTaskListener;
import com.example.android.popularmovies1.listeners.PageChangeClickListener;
import com.example.android.popularmovies1.tasks.DownloadTask;
import com.example.android.popularmovies1.tasks.MovieRelatedVideosDownloadTaskHandler;
import com.example.android.popularmovies1.tasks.MovieReviewsDownloadTaskHandler;
import com.example.android.popularmovies1.utils.MoviesUrlBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

public class MovieDetailsActivity extends SettingsMenuBaseActivity
        implements MovieRelatedVideosDownloadTaskHandler,
        MovieReviewsDownloadTaskHandler,
        PagedDataDownloader {

    private static final String STORAGE_KEY_VIDEOS = "videos";
    private static final String STORAGE_KEY_REVIEWS_PAGEINFO = "reviews_pageinfo";
    private static final String STORAGE_KEY_REVIEWS = "reviews";

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
    private ProgressBar videosProgressBar;
    private TextView videosErrorTextView;

    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private ProgressBar reviewsProgressBar;
    private TextView reviewsErrorTextView;
    private Button prevPageButton;
    private Button nextPageButton;
    private TextView pageNumTextView;

    private MovieListItem movieListItem;

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

        movieListItem = listItem;
        initializeVideosList();
        initializeReviewsList();

        loadData(savedInstanceState, listItem.getId());
    }

    private void initializeVideosList() {
        videosRecyclerView = (RecyclerView) findViewById(R.id.rv_videos_list);
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videosRecyclerView.setHasFixedSize(true);
        videoClickListener = new MovieRelatedVideoListItemClickListener(this);
        videosAdapter = new VideosAdapter(videoClickListener);
        videosRecyclerView.setAdapter(videosAdapter);

        videosProgressBar = (ProgressBar) findViewById(R.id.pb_videos_loading_indicator);
        videosErrorTextView = (TextView) findViewById(R.id.tv_videos_error);
    }

    private void initializeReviewsList() {
        pageNumTextView = (TextView) findViewById(R.id.tv_reviews_page_num);

        nextPageButton = (Button) findViewById(R.id.btn_reviews_page_next);
        nextPageButton.setOnClickListener(new PageChangeClickListener(1, this));

        prevPageButton = (Button) findViewById(R.id.btn_reviews_page_prev);
        prevPageButton.setOnClickListener(new PageChangeClickListener(-1, this));

        reviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews_list);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setHasFixedSize(true);
        reviewsAdapter = new ReviewsAdapter();
        reviewsRecyclerView.setAdapter(videosAdapter);

        reviewsProgressBar = (ProgressBar) findViewById(R.id.pb_reviews_loading_indicator);
        reviewsErrorTextView = (TextView) findViewById(R.id.tv_reviews_error);
    }

    private void loadData(Bundle savedInstanceState, long movieId) {
        ArrayList<MovieRelatedVideoListItem> restoredVideosList = null;
        PageInfo restoredPageInfo = null;
        ArrayList<MovieReviewListItem> restoredReviews = null;

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(STORAGE_KEY_VIDEOS)) {
                restoredVideosList = (ArrayList<MovieRelatedVideoListItem>)(ArrayList<?>)savedInstanceState.getParcelableArrayList(STORAGE_KEY_VIDEOS);
            }
            if(savedInstanceState.containsKey(STORAGE_KEY_REVIEWS_PAGEINFO)) {
                restoredPageInfo = savedInstanceState.getParcelable(STORAGE_KEY_REVIEWS_PAGEINFO);
            }
            if(savedInstanceState.containsKey(STORAGE_KEY_REVIEWS)) {
                restoredReviews = (ArrayList<MovieReviewListItem>)(ArrayList<?>)savedInstanceState.getParcelableArrayList(STORAGE_KEY_REVIEWS);
            }
        }

        if(restoredVideosList != null) {
            videosAdapter = new VideosAdapter(restoredVideosList, videoClickListener);
            videosRecyclerView.setAdapter(videosAdapter);
        } else {
            new DownloadTask(new MovieRelatedVideosDownloadTaskListener(this))
                    .execute(MoviesUrlBuilder.buildMovieRelatedVideosUrl(movieId));
        }

        if(restoredPageInfo != null && restoredReviews != null) {
            reviewsAdapter = new ReviewsAdapter(restoredReviews, restoredPageInfo);
            updatePageNumber(restoredPageInfo);
            reviewsRecyclerView.setAdapter(reviewsAdapter);
        } else {
            downloadData(1);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<MovieRelatedVideoListItem> videos = videosAdapter.getVideos();
        if(videos != null && videos.size() > 0) {
            outState.putParcelableArrayList(STORAGE_KEY_VIDEOS, videos);
        }

        ArrayList<MovieReviewListItem> reviews = reviewsAdapter.getReviews();
        if(reviews != null && reviews.size() > 0) {
            PageInfo pageInfo = getPageInfo();
            outState.putParcelable(STORAGE_KEY_REVIEWS_PAGEINFO, pageInfo);
            outState.putParcelableArrayList(STORAGE_KEY_REVIEWS, reviews);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    public void handleLikeButtonClick(View view) {
        boolean isFavorite = FavoriteMoviesHelper.changeMovieFavoritesState(this, movieListItem);

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

    private void showVideosError() {
        videosRecyclerView.setVisibility(View.INVISIBLE);
        videosErrorTextView.setVisibility(View.VISIBLE);
    }

    private void hideVideosError() {
        videosRecyclerView.setVisibility(View.VISIBLE);
        videosErrorTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMovieRelatedVideosProcessError() {
        showVideosError();
    }

    @Override
    public void onMovieRelatedVideosStartDownloading() {
        videosProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieRelatedVideosStartProcessing() {
        videosProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMovieRelatedVideosProcess(String data) throws JSONException {
        hideVideosError();
        videosAdapter = new VideosAdapter(data, videoClickListener);
        videosRecyclerView.setAdapter(videosAdapter);
    }

    private void showReviewsError() {
        reviewsRecyclerView.setVisibility(View.INVISIBLE);
        reviewsErrorTextView.setVisibility(View.VISIBLE);
    }

    private void hideReviewsError() {
        reviewsRecyclerView.setVisibility(View.VISIBLE);
        reviewsErrorTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMovieReviewsProcessError() {
        showReviewsError();
    }

    @Override
    public void onMovieReviewsStartDownloading() {
        reviewsProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieReviewsStartProcessing() {
        reviewsProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMovieReviewsProcess(String data) throws JSONException {
        hideReviewsError();
        reviewsAdapter = new ReviewsAdapter(data);
        updatePageNumber(reviewsAdapter.getPageInfo());
        reviewsRecyclerView.setAdapter(reviewsAdapter);
    }

    @Override
    public void downloadData(int page) {
        new DownloadTask(new MovieReviewsDownloadTaskListener(this))
                .execute(MoviesUrlBuilder.buildMovieReviewsUrl(movieListItem.getId(), page));
    }

    public void updatePageNumber(PageInfo pageNumber) {
        int pageNum = pageNumber.getPageNum();
        int totalPagesNum = pageNumber.getTotalPagesNum();

        if(totalPagesNum == 0) {
            pageNumTextView.setVisibility(View.INVISIBLE);
            prevPageButton.setVisibility(View.INVISIBLE);
            prevPageButton.setVisibility(View.INVISIBLE);
            return;
        }

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
    public PageInfo getPageInfo() {
        return reviewsAdapter.getPageInfo();
    }
}
