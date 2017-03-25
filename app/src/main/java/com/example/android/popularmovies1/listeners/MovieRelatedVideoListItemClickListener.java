package com.example.android.popularmovies1.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.example.android.popularmovies1.R;
import com.example.android.popularmovies1.activities.MovieDetailsActivity;
import com.example.android.popularmovies1.data.entities.MovieRelatedVideoListItem;
import com.example.android.popularmovies1.exceptions.UnsupportedVideoSiteException;
import com.example.android.popularmovies1.utils.VideosUriBuilder;

public class MovieRelatedVideoListItemClickListener {

    private MovieDetailsActivity detailsActivity;
    private Toast errorMsgToast = null;

    public MovieRelatedVideoListItemClickListener(MovieDetailsActivity detailsActivity) {
        this.detailsActivity = detailsActivity;
    }

    public void onClick(MovieRelatedVideoListItem video) {
        Context context = detailsActivity.getBaseContext();
        Uri videoUri = null;

        try
        {
            videoUri = VideosUriBuilder.buildVideoUri(video);
        }
        catch (UnsupportedVideoSiteException ex)
        {
            String baseMsg = context.getString(R.string.unsupported_video_site_error);
            showError(context, String.format(baseMsg, video.getSite()));
        }

        if(videoUri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
            if( intent.resolveActivity(context.getPackageManager()) != null ) {
                detailsActivity.startActivity(intent);
            } else {
                showError(context, context.getString(R.string.video_open_error));
            }
        } else {
            showError(context, context.getString(R.string.video_open_error));
        }
    }

    private void showError(Context context, String msg) {
        if(errorMsgToast != null)
            errorMsgToast.cancel();

        errorMsgToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        errorMsgToast.show();
    }

}
