package com.raphael.carvalho.android.popularmovies.favorite.dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class FavoriteDAO implements IFavoriteDAO {
    private static final String KEY_FAVORITES = "favorites";

    public void saveMovieFavorite(Activity activity, String movieId) {
        Set<String> allMovieFavorite = loadAllMovieFavorite(activity);
        allMovieFavorite.add(movieId);

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(KEY_FAVORITES, allMovieFavorite);
        editor.apply();
    }

    public void deleteMovieFavorite(Activity activity, String movieId) {
        Set<String> allMovieFavorite = loadAllMovieFavorite(activity);
        allMovieFavorite.remove(movieId);

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(KEY_FAVORITES, allMovieFavorite);
        editor.apply();
    }

    public Set<String> loadAllMovieFavorite(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getStringSet(KEY_FAVORITES, new HashSet<String>());
    }
}
