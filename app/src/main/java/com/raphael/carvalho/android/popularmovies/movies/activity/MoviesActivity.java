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
import com.raphael.carvalho.android.popularmovies.favorite.dao.FavoriteDAO;
import com.raphael.carvalho.android.popularmovies.favorite.dao.IFavoriteDAO;
import com.raphael.carvalho.android.popularmovies.movies.adapter.MoviesAdapter;
import com.raphael.carvalho.android.popularmovies.movies.model.Movie;
import com.raphael.carvalho.android.popularmovies.movies.model.MovieInfo;
import com.raphael.carvalho.android.popularmovies.movies.task.MovieDetailTask;
import com.raphael.carvalho.android.popularmovies.movies.task.SearchMoviesTask;
import com.raphael.carvalho.android.popularmovies.util.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.util.TaskListener;

import java.util.ArrayList;
import java.util.Set;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.MoviesListener {
    private IFavoriteDAO favoriteDAO;

    private MoviesAdapter adapter;

    private TaskListener<MovieInfo> movieInfoListener;
    private TaskListener<ArrayList<Movie>> favoriteMoviesListener;

    private String sortBy;
    private int page;

    private boolean showFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        favoriteDAO = new FavoriteDAO();
        initViews();
        sortBy(MoviesUrl.SORT_BY_POPULARITY);
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

        } else if (id == R.id.sort_by_favorite) {
            this.sortBy = null;
            showFavorite = true;
            adapter.clearMovies();
            loadFavoriteMovies();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortBy(@NonNull String sortBy) {
        showFavorite = false;
        if (sortBy.equals(this.sortBy)) return;

        this.sortBy = sortBy;
        page = 1;
        adapter.clearMovies();
        loadMovies();
    }

    private void initViews() {
        View pbLoading = findViewById(R.id.pb_loading);

        View cgErrorLoading = findViewById(R.id.cg_error_loading);
        findViewById(R.id.bt_error_loading).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showFavorite) {
                            loadFavoriteMovies();

                        } else loadMovies();
                    }
                }
        );

        adapter = new MoviesAdapter(this);
        RecyclerView rvMovies = findViewById(R.id.rv_movies);
        rvMovies.setAdapter(adapter);

        movieInfoListener = initMovieInfoListener(adapter, rvMovies, pbLoading, cgErrorLoading);
        favoriteMoviesListener = initFavoriteMoviesListener(adapter, rvMovies, pbLoading, cgErrorLoading);
    }

    private TaskListener<MovieInfo> initMovieInfoListener(
            final MoviesAdapter adapter, final RecyclerView recyclerView,
            final View pbLoading, final View vErrorLoading) {
        return new TaskListener<MovieInfo>() {

            @Override
            public void showLoading() {
                vErrorLoading.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void showErrorMessage() {
                pbLoading.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                vErrorLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public synchronized void showResult(MovieInfo result) {
                pbLoading.setVisibility(View.GONE);
                vErrorLoading.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);

                page = result.getPage();
                adapter.addMovies(result.getMovies());
            }
        };
    }

    private TaskListener<ArrayList<Movie>> initFavoriteMoviesListener(
            final MoviesAdapter adapter, final RecyclerView recyclerView,
            final View pbLoading, final View vErrorLoading) {
        return new TaskListener<ArrayList<Movie>>() {

            @Override
            public void showLoading() {
                vErrorLoading.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void showErrorMessage() {
                pbLoading.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                vErrorLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public synchronized void showResult(ArrayList<Movie> result) {
                pbLoading.setVisibility(View.GONE);
                vErrorLoading.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);

                adapter.addMovies(result);
            }
        };
    }

    @Override
    public void onClickMovie(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        MovieDetailActivity.addExtras(intent, movie);

        startActivity(intent);
    }

    @Override
    public void onLoadLastItem() {
        if (!showFavorite) {
            ++page;
            loadMovies();
        }
    }

    private void loadFavoriteMovies() {
        Set<String> allMovieFavorite = favoriteDAO.loadAllMovieFavorite(this);
        new MovieDetailTask(favoriteMoviesListener)
                .execute(allMovieFavorite.toArray(new String[0]));
    }

    private void loadMovies() {
        new SearchMoviesTask(movieInfoListener)
                .execute(sortBy, page + "");
    }
}
