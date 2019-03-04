package com.raphael.carvalho.android.popularmovies.detail.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.raphael.carvalho.android.popularmovies.detail.model.TrailerInfo;
import com.raphael.carvalho.android.popularmovies.util.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.util.NetworkUtils;
import com.raphael.carvalho.android.popularmovies.util.TaskListener;

import java.net.URL;

public class SearchMovieTrailersTask extends AsyncTask<String, Void, TrailerInfo> {
    private final TaskListener<TrailerInfo> listener;

    public SearchMovieTrailersTask(@NonNull TaskListener<TrailerInfo> listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.showLoading();
    }

    @Override
    protected TrailerInfo doInBackground(String... params) {
        URL url = null;
        if (params.length >= 1) {
            url = MoviesUrl.buildTrailersUrl(params[0]);
        }

        if (url == null) return null;
        try {
            String json = NetworkUtils.getResponseFromHttpUrl(url);

            return new Gson().fromJson(json, TrailerInfo.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(TrailerInfo trailerInfo) {
        super.onPostExecute(trailerInfo);

        if (trailerInfo != null) {
            listener.showResult(trailerInfo);

        } else listener.showErrorMessage(this);
    }
}
