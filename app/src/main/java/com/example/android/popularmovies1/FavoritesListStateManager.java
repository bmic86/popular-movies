package com.example.android.popularmovies1;


public class FavoritesListStateManager {

    private boolean favoritesEnabledChanged = false;
    private boolean favoritesListChanged = false;

    private static FavoritesListStateManager instance = new FavoritesListStateManager();

    private FavoritesListStateManager(){
    }

    public static FavoritesListStateManager getInstance() {
        return instance;
    }


    public boolean getAndResetFavoritesEnabledChanged() {
        boolean lastState = favoritesEnabledChanged;
        favoritesEnabledChanged = false;
        return lastState;
    }

    public void setFavoritesEnabledChanged() {
        this.favoritesEnabledChanged = true;
    }

    public boolean getAndResetFavoritesListChanged() {
        boolean lastState = favoritesListChanged;
        favoritesListChanged = false;
        return lastState;
    }

    public void setFavoritesListChanged() {
        this.favoritesListChanged = true;
    }

}
