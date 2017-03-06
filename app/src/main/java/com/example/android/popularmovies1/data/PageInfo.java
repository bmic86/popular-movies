package com.example.android.popularmovies1.data;

import android.os.Parcel;
import android.os.Parcelable;

public class PageInfo implements Parcelable{

    private int pageNum;
    private int totalPagesNum;

    public static final Parcelable.Creator<PageInfo> CREATOR
            = new Parcelable.Creator<PageInfo>() {
        public PageInfo createFromParcel(Parcel in) {
            return new PageInfo(in);
        }

        public PageInfo[] newArray(int size) {
            return new PageInfo[size];
        }
    };

    public PageInfo(int pageNum, int totalPagesNum) {
        this.pageNum = pageNum;
        this.totalPagesNum = totalPagesNum;
    }

    public PageInfo(Parcel parcel) {
        pageNum = parcel.readInt();
        totalPagesNum = parcel.readInt();
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getTotalPagesNum() {
        return totalPagesNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(pageNum);
        parcel.writeInt(totalPagesNum);
    }
}
