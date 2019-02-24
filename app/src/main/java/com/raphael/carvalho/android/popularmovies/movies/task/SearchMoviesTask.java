package com.raphael.carvalho.android.popularmovies.movies.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.raphael.carvalho.android.popularmovies.movies.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.movies.model.MovieInfo;
import com.raphael.carvalho.android.popularmovies.util.NetworkUtils;
import com.raphael.carvalho.android.popularmovies.util.TaskListener;

import java.net.URL;

public class SearchMoviesTask extends AsyncTask<String, Void, MovieInfo> {
    private final TaskListener<MovieInfo> listener;

    public SearchMoviesTask(@NonNull TaskListener<MovieInfo> listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.showLoading();
    }

    @Override
    protected MovieInfo doInBackground(String... params) {
        URL url = null;
        if (params.length >= 2) {
            url = MoviesUrl.buildUrl(params[0], params[1]);
        }

        if (url == null) return null;
        try {
            String json = NetworkUtils.getResponseFromHttpUrl(url);

            return new Gson().fromJson(json, MovieInfo.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieInfo movieInfo) {
        super.onPostExecute(movieInfo);

        if (movieInfo != null) {
            listener.showResult(movieInfo);

        } else listener.showErrorMessage(this);
    }
}
