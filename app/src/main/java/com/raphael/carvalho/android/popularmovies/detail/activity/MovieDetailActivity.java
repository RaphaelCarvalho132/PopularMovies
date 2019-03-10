package com.raphael.carvalho.android.popularmovies.detail.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raphael.carvalho.android.popularmovies.R;
import com.raphael.carvalho.android.popularmovies.movies.model.Movie;
import com.raphael.carvalho.android.popularmovies.util.MoviesUrl;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = "movie";

    private Movie movie;

    public static void addExtras(Intent intent, Movie movie) {
        intent.putExtra(EXTRA_MOVIE, movie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
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

        initMovieInformation();
    }

    private void initMovieInformation() {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                if (parentIntent != null) {
                    parentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    NavUtils.navigateUpTo(this, parentIntent);

                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
