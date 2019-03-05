package com.raphael.carvalho.android.popularmovies.detail.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;
import com.raphael.carvalho.android.popularmovies.util.SitesUrl;

public class Trailer {
    private static final String SITE_YOU_TUBE = "YouTube";

    @SerializedName("id")
    private String id;
    @SerializedName("iso_639_1")
    private String iso6391;
    @SerializedName("iso_3166_1")
    private String iso31661;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private Integer size;
    @SerializedName("type")
    private String type;

    public String getName() {
        return name;
    }

    public Uri getUri() {
        if (SITE_YOU_TUBE.equals(site)) {
            return SitesUrl.buildYouTubeUri(key);

        } else return null;
    }
}