package com.raphael.carvalho.android.popularmovies.favorite.dao;

import android.app.Activity;

import java.util.Set;

public interface IFavoriteDAO {
    Set<String> loadAllMovieFavorite(Activity activity);

    void saveMovieFavorite(Activity activity, String movieId);

    void deleteMovieFavorite(Activity activity, String movieId);
}
