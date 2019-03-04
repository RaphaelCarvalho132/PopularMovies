package com.raphael.carvalho.android.popularmovies.detail.model;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;

    public String getContent() {
        return content;
    }
}