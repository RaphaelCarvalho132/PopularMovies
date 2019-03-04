package com.raphael.carvalho.android.popularmovies.movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.raphael.carvalho.android.popularmovies.R;
import com.raphael.carvalho.android.popularmovies.detail.activity.MovieDetailActivity;
import com.raphael.carvalho.android.popularmovies.movies.adapter.MoviesAdapter;
import com.raphael.carvalho.android.popularmovies.movies.model.Movie;
import com.raphael.carvalho.android.popularmovies.movies.model.MovieInfo;
import com.raphael.carvalho.android.popularmovies.movies.task.SearchMoviesTask;
import com.raphael.carvalho.android.popularmovies.util.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.util.TaskListener;

public class MoviesActivity extends AppCompatActivity implements TaskListener<MovieInfo>, MoviesAdapter.MoviesListener {
    private View pbLoading;
    private View cgErrorLoading;

    private MoviesAdapter adapter;
    private RecyclerView rvMovies;

    private String sortBy;
    private MovieInfo movieInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        sortBy = MoviesUrl.SORT_BY_POPULARITY;
        initViews();
        loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_by_most_popular) {
            sortBy(MoviesUrl.SORT_BY_POPULARITY);
            return true;

        } else if (id == R.id.sort_by_top_rated) {
            sortBy(MoviesUrl.SORT_BY_VOTE_AVERAGE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortBy(@NonNull String sortBy) {
        if (sortBy.equals(this.sortBy)) return;

        this.sortBy = sortBy;
        movieInfo = null;
        adapter.clearMovies();
        loadMovies();
    }

    private void initViews() {
        pbLoading = findViewById(R.id.pb_loading);

        cgErrorLoading = findViewById(R.id.cg_error_loading);
        findViewById(R.id.bt_error_loading).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadMovies();
                    }
                }
        );

        adapter = new MoviesAdapter(this);
        rvMovies = findViewById(R.id.rv_movies);
        rvMovies.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        cgErrorLoading.setVisibility(View.GONE);
        rvMovies.setVisibility(View.GONE);

        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage() {
        pbLoading.setVisibility(View.GONE);
        rvMovies.setVisibility(View.GONE);

        cgErrorLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public synchronized void showResult(MovieInfo result) {
        pbLoading.setVisibility(View.GONE);
        cgErrorLoading.setVisibility(View.GONE);

        rvMovies.setVisibility(View.VISIBLE);

        movieInfo = result;
        adapter.addMovies(result.getMovies());
    }

    @Override
    public void onClickMovie(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        MovieDetailActivity.addExtras(intent, movie);

        startActivity(intent);
    }

    @Override
    public void onLoadLastItem() {
        loadMovies();
    }

    private void loadMovies() {
        String page = (movieInfo != null)
                ? Integer.toString(movieInfo.getPage() + 1) : "1";

        new SearchMoviesTask(this)
                .execute(sortBy, page);
    }
}
