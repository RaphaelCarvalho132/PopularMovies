package com.raphael.carvalho.android.popularmovies.movies;

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
    public static final String SORT_BY_POPULARITY = "popularity.desc";
    public static final String SORT_BY_VOTE_AVERAGE = "vote_average.desc";

    private static final String TAG = MoviesUrl.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    private static final String API_KEY_PARAM = "api_key";
    private static final String SORT_BY_PARAM = "sort_by";
    private static final String PAGE_PARAM = "page";
    private static final String LANGUAGE_PARAM = "language";

    public static URL buildUrl(@SortBy String sortBy, String page) {
        return buildUrl(
                getConfiguredBuilder()
                        .appendQueryParameter(SORT_BY_PARAM, sortBy)
                        .appendQueryParameter(PAGE_PARAM, page)
                        .build()
        );
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

    @StringDef({SORT_BY_POPULARITY, SORT_BY_VOTE_AVERAGE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SortBy {
    }
}
