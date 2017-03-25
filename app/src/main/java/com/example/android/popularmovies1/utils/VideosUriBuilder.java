package com.example.android.popularmovies1.utils;

import android.net.Uri;

import com.example.android.popularmovies1.data.entities.MovieRelatedVideoListItem;
import com.example.android.popularmovies1.exceptions.UnsupportedVideoSiteException;

import java.util.HashMap;

public class VideosUriBuilder {

    public static final HashMap<String, String> videoSiteBaseUrls = new HashMap<String, String>();
    static {
        videoSiteBaseUrls.put("YouTube", "https://www.youtube.com/embed/%s");
    }

    private VideosUriBuilder() {
    }

    public static Uri buildVideoUri(MovieRelatedVideoListItem video) throws UnsupportedVideoSiteException {
        if(video == null || video.getKey() == null || video.getKey().isEmpty())
            return null;

        String site = video.getSite();
        if(!videoSiteBaseUrls.containsKey(site))
            throw new UnsupportedVideoSiteException();

        String baseUri = videoSiteBaseUrls.get(site);
        String uri = String.format(baseUri, video.getKey());
        return Uri.parse(uri).buildUpon().build();
    }
}
