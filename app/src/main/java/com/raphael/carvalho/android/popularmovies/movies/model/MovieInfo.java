package com.raphael.carvalho.android.popularmovies.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieInfo {
    @SerializedName("page")
    private Integer page;
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("results")
    private ArrayList<Movie> movies;

    public Integer getPage() {
        return page;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}
