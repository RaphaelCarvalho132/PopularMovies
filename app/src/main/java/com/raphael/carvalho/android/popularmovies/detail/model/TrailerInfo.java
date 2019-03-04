package com.raphael.carvalho.android.popularmovies.detail.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrailerInfo {
    @SerializedName("id")
    private Integer id;
    @SerializedName("results")
    private ArrayList<Trailer> trailers;

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }
}
