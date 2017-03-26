package com.example.android.popularmovies1;

import com.example.android.popularmovies1.data.entities.PageInfo;

public interface PagedDataDownloader {

    void downloadData(int page);
    PageInfo getPageInfo();

}
