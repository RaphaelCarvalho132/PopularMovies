package com.raphael.carvalho.android.popularmovies.util;

import android.os.AsyncTask;

public interface TaskListener<T> {
    void showLoading();

    void showErrorMessage(AsyncTask task);

    void showResult(T result);
}
