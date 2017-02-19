package com.example.android.popularmovies1.data;

public class PageInfo {

    private int pageNum;
    private int totalPagesNum;

    public PageInfo(int pageNum, int totalPagesNum) {
        this.pageNum = pageNum;
        this.totalPagesNum = totalPagesNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getTotalPagesNum() {
        return totalPagesNum;
    }

}
