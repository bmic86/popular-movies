package com.example.android.popularmovies1.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies1.MovieDetailsIntent;
import com.example.android.popularmovies1.R;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView originalTitleTextView;
    private TextView overviewTextView;
    private TextView voteAverageTextView;
    private TextView releaseDateTextView;
    private ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        originalTitleTextView = (TextView)findViewById(R.id.tv_original_title_value);
        overviewTextView = (TextView)findViewById(R.id.tv_overview_value);
        voteAverageTextView = (TextView)findViewById(R.id.tv_vote_average_value);
        releaseDateTextView = (TextView)findViewById(R.id.tv_release_date_value);
        posterImageView = (ImageView)findViewById(R.id.iv_details_poster);

        Intent intent = getIntent();

        String originalTitle = "";
        if( intent.hasExtra(MovieDetailsIntent.ORIGINAL_TITLE) ) {
            originalTitle = intent.getStringExtra(MovieDetailsIntent.ORIGINAL_TITLE);
        }
        originalTitleTextView.setText(originalTitle);

        if( intent.hasExtra(MovieDetailsIntent.POSTER_URL_PATH) ) {
            String posterUrlPath = intent.getStringExtra(MovieDetailsIntent.POSTER_URL_PATH);
            Picasso.with(this).load(posterUrlPath).into(posterImageView);
            posterImageView.setVisibility(View.VISIBLE);
        } else {
            posterImageView.setVisibility(View.INVISIBLE);
        }

        String overview = "";
        if( intent.hasExtra(MovieDetailsIntent.OVERVIEW) ) {
            overview = intent.getStringExtra(MovieDetailsIntent.OVERVIEW);
        }
        overviewTextView.setText(overview);

        String voteAverage = "";
        if( intent.hasExtra(MovieDetailsIntent.VOTE_AVERAGE) ) {
            voteAverage = intent.getStringExtra(MovieDetailsIntent.VOTE_AVERAGE);
        }
        voteAverageTextView.setText(voteAverage);

        String releaseDate = "";
        if( intent.hasExtra(MovieDetailsIntent.RELEASE_DATE) ) {
            releaseDate = intent.getStringExtra(MovieDetailsIntent.RELEASE_DATE);
        }
        releaseDateTextView.setText(releaseDate);
    }
}
