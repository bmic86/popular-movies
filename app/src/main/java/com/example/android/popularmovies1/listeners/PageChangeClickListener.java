package com.example.android.popularmovies1.listeners;

import android.view.View;

import com.example.android.popularmovies1.PagedDataDownloader;
import com.example.android.popularmovies1.data.entities.PageInfo;

public class PageChangeClickListener implements View.OnClickListener {

    private int stepSize;
    private PagedDataDownloader downloader;

    public PageChangeClickListener(int stepSize, PagedDataDownloader downloader) {
        this.stepSize = stepSize;
        this.downloader = downloader;
    }

    @Override
    public void onClick(View view) {
        PageInfo pageInfo = downloader.getPageInfo();
        int nextPage = pageInfo.getPageNum() + stepSize;
        if( nextPage > 0 && nextPage <= pageInfo.getTotalPagesNum()) {
            downloader.downloadData(nextPage);
        }
    }
}
