package com.example.android.popularmovies1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies1.data.entities.FavoriteMoviesList;
import com.example.android.popularmovies1.data.entities.MovieListItem;

public class FavoriteMoviesHelper {

    private static final String CLASS_TAG = FavoriteMoviesHelper.class.getSimpleName();
    private static final int PAGE_SIZE = 20;

    private FavoriteMoviesHelper() {
    }

    public static boolean changeMovieFavoritesState(Context context, MovieListItem listItem) {
        final Uri uri = generateUri(listItem.getId());

        boolean isFavorite = isMovieFavorite(context, uri);
        if(isFavorite) {
            isFavorite = !deleteFromFavorites(context, uri);
        } else {
            isFavorite = addToFavorites(context, listItem);
        }
        Log.d(CLASS_TAG, "isFavorite = " + String.valueOf(isFavorite));
        return isFavorite;
    }

    public static boolean isMovieFavorite(Context context, long movieId) {
        return isMovieFavorite(context, generateUri(movieId));
    }

    public static FavoriteMoviesList getFavoriteMoviesList(Context context, int page) {
        FavoriteMoviesList result = null;
        Cursor data = null;
        try
        {
            data = context.getContentResolver().query(generateUri(), null, null, null, null);
            int allItems = data.getCount();
            int pagesNum = (int)Math.ceil((double)allItems / (double)PAGE_SIZE);
            if (page <= pagesNum && page >= 1) {
                int firstItemIndex = (page-1) * PAGE_SIZE;
                int lastItemIndex = firstItemIndex + PAGE_SIZE - 1;
                if(allItems - 1 < lastItemIndex) {
                    lastItemIndex = allItems - 1;
                }
                int itemsCount = lastItemIndex - firstItemIndex + 1;

                data.moveToPosition(firstItemIndex);
                long[] ids = new long[itemsCount];
                for(int i=0; i<ids.length; i++) {
                    ids[i] = data.getLong(data.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesContractEntry._ID));
                    data.moveToNext();
                }
                result = new FavoriteMoviesList(ids, page, pagesNum);
            }
        }
        catch (Exception ex)
        {
            Log.e(CLASS_TAG, ex.getMessage());
        }
        finally
        {
            if(data != null)
                data.close();
        }
        return result;
    }

    private static boolean isMovieFavorite(Context context, Uri uri) {
        Cursor data = context.getContentResolver().query(uri, null, null, null, null);
        boolean result = false;
        if (data != null) {
            result = (data.getCount() == 1);
            data.close();
        }
        return result;
    }

    private static boolean deleteFromFavorites(Context context, Uri uri) {
        return (context.getContentResolver().delete(uri, null, null) > 0);
    }

    private static boolean addToFavorites(Context context, MovieListItem listItem) {
        ContentValues values = new ContentValues();
        values.put(FavoriteMoviesContract.FavoriteMoviesContractEntry._ID, listItem.getId());
        values.put(FavoriteMoviesContract.FavoriteMoviesContractEntry.COLUMN_TITLE, listItem.getOriginalTitle());

        Uri newEntryUri = context.getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesContractEntry.CONTENT_URI, values);
        boolean result = (newEntryUri != null);

        if(result)
            Log.d(CLASS_TAG, "Added new Resource " + newEntryUri);

        return result;
    }

    private static Uri generateUri(long movieId) {
        final String id = String.valueOf(movieId);
        return FavoriteMoviesContract.FavoriteMoviesContractEntry.CONTENT_URI
                .buildUpon()
                .appendPath(id)
                .build();
    }

    private static Uri generateUri() {
        return FavoriteMoviesContract.FavoriteMoviesContractEntry.CONTENT_URI
                .buildUpon()
                .build();
    }

}
