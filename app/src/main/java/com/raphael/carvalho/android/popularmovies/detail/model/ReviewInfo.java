package com.raphael.carvalho.android.popularmovies.detail.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewInfo {
    @SerializedName("id")
    private Integer id;
    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private ArrayList<Review> reviews;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_results")
    private Integer totalResults;

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
