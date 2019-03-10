package com.raphael.carvalho.android.popularmovies.util;

import android.net.Uri;
import android.support.annotation.StringDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Pattern;

import static com.raphael.carvalho.android.popularmovies.BuildConfig.API_KEY_THE_MOVIE_DB;

public class MoviesUrl {
    public static final String SORT_BY_POPULAR = "popular";
    public static final String SORT_BY_TOP_RATED = "top_rated";

    private static final String TAG = MoviesUrl.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static final String MOVIE_PATH_URL = "movie";
    private static final String POPULAR_PATH_URL = "popular";
    private static final String TOP_RATED_PATH_URL = "top_rated";

    private static final String API_KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";
    private static final String LANGUAGE_PARAM = "language";

    private static final String IMG_URL = "http://image.tmdb.org/t/p/";
    private static final String IMG_QUALITY = "w185";

    public static Uri buildPosterUri(String posterPath) {
        return Uri.parse(IMG_URL).buildUpon()
                .appendEncodedPath(IMG_QUALITY)
                .appendEncodedPath(posterPath)
                .build();
    }

    public static URL buildDiscoverUrl(@SortBy String sortBy, String page) {
        if (SORT_BY_POPULAR.equals(sortBy)) {
            return buildUrl(
                    getConfiguredBuilder()
                            .appendEncodedPath(MOVIE_PATH_URL)
                            .appendEncodedPath(POPULAR_PATH_URL)
                            .appendQueryParameter(PAGE_PARAM, page)
                            .build()
            );

        } else if (SORT_BY_TOP_RATED.equals(sortBy)) {
            return buildUrl(
                    getConfiguredBuilder()
                            .appendEncodedPath(MOVIE_PATH_URL)
                            .appendEncodedPath(TOP_RATED_PATH_URL)
                            .appendQueryParameter(PAGE_PARAM, page)
                            .build()
            );

        } else return null;
    }

    private static URL buildUrl(Uri uri) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    private static Uri.Builder getConfiguredBuilder() {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_THE_MOVIE_DB);

        String language = getCurrentLanguage();
        if (Pattern.matches("([a-z]{2})-([A-Z]{2})", language))
            builder.appendQueryParameter(LANGUAGE_PARAM, language);

        return builder;
    }

    private static String getCurrentLanguage() {
        return Locale.getDefault().getDisplayLanguage();
    }

    @StringDef({SORT_BY_POPULAR, SORT_BY_TOP_RATED})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SortBy {
    }
}
