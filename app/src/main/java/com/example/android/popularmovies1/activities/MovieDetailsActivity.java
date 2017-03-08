package com.example.android.popularmovies1.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies1.MovieDetailsIntent;
import com.example.android.popularmovies1.R;
import com.example.android.popularmovies1.data.FavoriteMoviesContract;
import com.example.android.popularmovies1.data.FavoriteMoviesHelper;
import com.example.android.popularmovies1.data.entities.MovieListItem;
import com.example.android.popularmovies1.data.entities.MovieListItemDetails;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView originalTitleTextView;
    private TextView overviewTextView;
    private TextView voteAverageTextView;
    private TextView releaseDateTextView;
    private ImageView posterImageView;
    private Toast messageToast;
    private ImageButton addToFavoritesButton;

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
        MovieListItem listItem = null;

        if(intent.hasExtra(MovieDetailsIntent.MOVIE_LIST_ITEM)) {
            listItem = intent.getParcelableExtra(MovieDetailsIntent.MOVIE_LIST_ITEM);
        }

        if(listItem != null) {
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
        }

        setFavoriteButtonState(FavoriteMoviesHelper.isMovieFavorite(this, listItem.getId()));

        movieListItemDetails = new MovieListItemDetails(listItem);
    }

    public void handleLikeButtonClick(View view) {
        boolean isFavorite = FavoriteMoviesHelper.changeMovieFavoritesState(this, movieListItemDetails.getMovieListItem());

        setFavoriteButtonState(isFavorite);

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

}
