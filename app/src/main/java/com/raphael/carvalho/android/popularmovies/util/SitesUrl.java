package com.raphael.carvalho.android.popularmovies.util;

import android.net.Uri;
import android.util.Log;

public class SitesUrl {
    private static final String TAG = SitesUrl.class.getSimpleName();

    private static final String YOU_TUBE_URL = "https://www.youtube.com/watch";
    private static final String YOU_TUBE_KEY_PARAM = "v";

    public static Uri buildYouTubeUri(String key) {
        Uri uri = Uri.parse(YOU_TUBE_URL).buildUpon()
                .appendQueryParameter(YOU_TUBE_KEY_PARAM, key)
                .build();

        Log.v(TAG, "Built URI " + uri);
        return uri;
    }
}
