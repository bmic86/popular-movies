package com.example.android.popularmovies1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE = "CREATE TABLE "  + FavoriteMoviesContract.FavoriteMoviesContractEntry.TABLE_NAME + " (" +
                FavoriteMoviesContract.FavoriteMoviesContractEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMoviesContract.FavoriteMoviesContractEntry.COLUMN_TITLE + " TEXT NOT NULL); ";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
