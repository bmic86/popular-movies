package com.example.android.popularmovies1.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.android.popularmovies1.R;

public class AppSettings {

    private AppSettings() {
    }

    public static boolean isShowFavorites(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(context.getString(R.string.show_favorites_key), false);
    }
}
