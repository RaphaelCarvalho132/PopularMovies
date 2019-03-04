package com.raphael.carvalho.android.popularmovies.detail.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raphael.carvalho.android.popularmovies.R;
import com.raphael.carvalho.android.popularmovies.detail.adapter.TrailerAdapter;
import com.raphael.carvalho.android.popularmovies.detail.model.Trailer;
import com.raphael.carvalho.android.popularmovies.detail.model.TrailerInfo;
import com.raphael.carvalho.android.popularmovies.detail.task.SearchMovieTrailersTask;
import com.raphael.carvalho.android.popularmovies.movies.model.Movie;
import com.raphael.carvalho.android.popularmovies.util.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.util.TaskListener;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerListener, TaskListener<TrailerInfo> {
    private static final String EXTRA_MOVIE = "movie";

    private Movie movie;

    private RecyclerView rvTrailers;
    private TrailerAdapter adapterTrailer;
    private View pbLoadingTrailers;
    private View cgErrorLoadingTrailers;

    public static void addExtras(Intent intent, Movie movie) {
        intent.putExtra(EXTRA_MOVIE, movie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(EXTRA_MOVIE);
        if (movie != null) {
            initViews(movie);

        } else {
            Toast.makeText(this, R.string.movie_detail_error_open, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews(Movie movie) {
        ImageView ivPoster = findViewById(R.id.iv_movie_detail_poster);
        Picasso.get()
                .load(MoviesUrl.buildPosterUri(movie.getPosterPath()).toString())
                .into(ivPoster);

        TextView tvTitle = findViewById(R.id.tv_movie_detail_title);
        tvTitle.setText(movie.getTitle());

        TextView tvSynopsis = findViewById(R.id.tv_movie_detail_synopsis);
        tvSynopsis.setText(movie.getOverview());

        TextView tvReleaseDate = findViewById(R.id.tv_movie_detail_release_date);
        tvReleaseDate.setText(movie.getReleaseDate());

        TextView tvRating = findViewById(R.id.tv_movie_detail_rating);
        tvRating.setText(getString(
                R.string.movie_detail_rating_arg,
                movie.getVoteAverage()
        ));

        initViewTrailers();
    }

    private void initViewTrailers() {
        adapterTrailer = new TrailerAdapter(this);
        rvTrailers = findViewById(R.id.rv_movie_detail_trailers);
        rvTrailers.setAdapter(adapterTrailer);

        pbLoadingTrailers = findViewById(R.id.pb_movie_detail_loading_trailers);
        cgErrorLoadingTrailers = findViewById(R.id.cg_movie_detail_error_loading);
        findViewById(R.id.bt_movie_detail_error_loading_trailers).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadTrailers();
                    }
                }
        );

        loadTrailers();
    }

    private void loadTrailers() {
        new SearchMovieTrailersTask(this).execute(movie.getId() + "");
    }

    @Override
    public void onClickTrailer(Trailer trailer) {

    }

    @Override
    public void showLoading() {
        rvTrailers.setVisibility(View.GONE);
        cgErrorLoadingTrailers.setVisibility(View.GONE);

        pbLoadingTrailers.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage(AsyncTask task) {
        rvTrailers.setVisibility(View.GONE);
        pbLoadingTrailers.setVisibility(View.GONE);

        cgErrorLoadingTrailers.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResult(TrailerInfo result) {
        pbLoadingTrailers.setVisibility(View.GONE);
        cgErrorLoadingTrailers.setVisibility(View.GONE);

        rvTrailers.setVisibility(View.VISIBLE);
        adapterTrailer.addTrailers(result.getTrailers());
    }
}
