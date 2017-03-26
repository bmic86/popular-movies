package com.example.android.popularmovies1.data.entities;

public class FavoriteMoviesList {

    private PageInfo pageInfo;
    private long[] movieIds;

    public FavoriteMoviesList() {
        this.movieIds = new long[] {};
        pageInfo = new PageInfo();
    }

    public FavoriteMoviesList(long[] movieIds, int page, int totalPages) {
        this.movieIds = movieIds;
        pageInfo = new PageInfo(page, totalPages);
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public long[] getMovieIds() {
        return movieIds;
    }

    public void setMovieIds(long[] movieIds) {
        this.movieIds = movieIds;
    }

}
