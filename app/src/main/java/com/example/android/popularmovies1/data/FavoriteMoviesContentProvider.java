package com.example.android.popularmovies1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.example.android.popularmovies1.data.FavoriteMoviesContract.FavoriteMoviesContractEntry.TABLE_NAME;
import static com.example.android.popularmovies1.data.FavoriteMoviesContract.FavoriteMoviesContractEntry.CONTENT_URI;

public class FavoriteMoviesContentProvider extends ContentProvider {

    private static final int FAVORITES = 100;
    private static final int FAVORITE_BY_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    FavoriteMoviesDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITES + "/#", FAVORITE_BY_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new FavoriteMoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int code = uriMatcher.match(uri);

        Cursor result = null;

        switch (code) {
            case FAVORITES:
                result = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAVORITE_BY_ID:
                final String id = uri.getPathSegments().get(1);
                result = db.query(TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[] { id },
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Malformed URI: " + uri);
        }

        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int code = uriMatcher.match(uri);
        Uri result;

        switch (code) {
            case FAVORITES:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    result = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Error while inserting row via URI: " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Malformed URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int result;

        switch (match) {
            case FAVORITE_BY_ID:
                String id = uri.getPathSegments().get(1);
                result = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Malformed URI: " + uri);
        }

        if (result != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return result;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

}
