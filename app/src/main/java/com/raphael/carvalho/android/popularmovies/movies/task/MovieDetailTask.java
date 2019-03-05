package com.raphael.carvalho.android.popularmovies.movies.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.raphael.carvalho.android.popularmovies.movies.model.Movie;
import com.raphael.carvalho.android.popularmovies.util.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.util.NetworkUtils;
import com.raphael.carvalho.android.popularmovies.util.TaskListener;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetailTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private final TaskListener<ArrayList<Movie>> listener;

    public MovieDetailTask(@NonNull TaskListener<ArrayList<Movie>> listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.showLoading();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (String movieId : params) {
            URL url = MoviesUrl.buildMovieDetailUrl(movieId);

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                movies.add(new Gson().fromJson(json, Movie.class));

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);

        if (movies != null) {
            listener.showResult(movies);

        } else listener.showErrorMessage();
    }
}
