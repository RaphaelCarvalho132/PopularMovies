package com.raphael.carvalho.android.popularmovies.util;

public interface TaskListener<T> {
    void showLoading();

    void showErrorMessage();

    void showResult(T result);
}
