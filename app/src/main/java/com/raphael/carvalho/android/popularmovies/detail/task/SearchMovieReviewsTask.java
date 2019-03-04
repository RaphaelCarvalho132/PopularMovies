package com.raphael.carvalho.android.popularmovies.detail.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.raphael.carvalho.android.popularmovies.detail.model.ReviewInfo;
import com.raphael.carvalho.android.popularmovies.util.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.util.NetworkUtils;
import com.raphael.carvalho.android.popularmovies.util.TaskListener;

import java.net.URL;

public class SearchMovieReviewsTask extends AsyncTask<String, Void, ReviewInfo> {
    private final TaskListener<ReviewInfo> listener;

    public SearchMovieReviewsTask(@NonNull TaskListener<ReviewInfo> listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.showLoading();
    }

    @Override
    protected ReviewInfo doInBackground(String... params) {
        URL url = null;
        if (params.length >= 2) {
            url = MoviesUrl.buildReviewsUrl(params[0], params[1]);
        }

        if (url == null) return null;
        try {
            String json = NetworkUtils.getResponseFromHttpUrl(url);

            return new Gson().fromJson(json, ReviewInfo.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ReviewInfo reviewInfo) {
        super.onPostExecute(reviewInfo);

        if (reviewInfo != null) {
            listener.showResult(reviewInfo);

        } else listener.showErrorMessage();
    }
}
