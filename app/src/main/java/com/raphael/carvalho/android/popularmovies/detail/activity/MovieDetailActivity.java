package com.raphael.carvalho.android.popularmovies.detail.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raphael.carvalho.android.popularmovies.R;
import com.raphael.carvalho.android.popularmovies.detail.adapter.ReviewsAdapter;
import com.raphael.carvalho.android.popularmovies.detail.adapter.TrailerAdapter;
import com.raphael.carvalho.android.popularmovies.detail.model.Review;
import com.raphael.carvalho.android.popularmovies.detail.model.ReviewInfo;
import com.raphael.carvalho.android.popularmovies.detail.model.Trailer;
import com.raphael.carvalho.android.popularmovies.detail.model.TrailerInfo;
import com.raphael.carvalho.android.popularmovies.detail.task.SearchMovieReviewsTask;
import com.raphael.carvalho.android.popularmovies.detail.task.SearchMovieTrailersTask;
import com.raphael.carvalho.android.popularmovies.favorite.dao.FavoriteDAO;
import com.raphael.carvalho.android.popularmovies.favorite.dao.IFavoriteDAO;
import com.raphael.carvalho.android.popularmovies.movies.model.Movie;
import com.raphael.carvalho.android.popularmovies.util.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.util.TaskListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Set;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerListener, ReviewsAdapter.ReviewListener {
    private static final String EXTRA_MOVIE = "movie";

    private static final String REVIEW_PAGE_STATE_KEY = "page";
    private static final String TRAILERS_STATE_KEY = "trailers";
    private static final String REVIEWS_STATE_KEY = "reviews";

    private IFavoriteDAO favoriteDAO;
    private Movie movie;
    private int reviewPage;

    private TaskListener<TrailerInfo> trailerListener;
    private TaskListener<ReviewInfo> reviewsListener;

    private TrailerAdapter adapterTrailer;
    private ReviewsAdapter adapterReviews;

    public static void addExtras(Intent intent, Movie movie) {
        intent.putExtra(EXTRA_MOVIE, movie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movie != null) {
            reviewPage = 1;
            favoriteDAO = new FavoriteDAO();

            initViews(movie);

            if (savedInstanceState == null) {
                loadTrailers();
                loadReviews(reviewPage);

            } else restoreActivity(savedInstanceState);

        } else {
            Toast.makeText(this, R.string.movie_detail_error_open, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void restoreActivity(Bundle savedInstanceState) {
        reviewPage = savedInstanceState.getInt(REVIEW_PAGE_STATE_KEY, 1);

        ArrayList<Trailer> trailers = savedInstanceState.getParcelableArrayList(TRAILERS_STATE_KEY);
        if (trailers != null) adapterTrailer.addTrailers(trailers);

        ArrayList<Review> reviews = savedInstanceState.getParcelableArrayList(REVIEWS_STATE_KEY);
        if (reviews != null) adapterReviews.addReviews(reviews);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(REVIEW_PAGE_STATE_KEY, reviewPage);

        outState.putParcelableArrayList(
                TRAILERS_STATE_KEY, adapterTrailer.getTrailers());

        outState.putParcelableArrayList(
                REVIEWS_STATE_KEY, adapterReviews.getReviews());

        super.onSaveInstanceState(outState);
    }

    private void initViews(Movie movie) {
        ImageView ivPoster = findViewById(R.id.iv_movie_detail_poster);
        Picasso.get()
                .load(MoviesUrl.buildPosterUri(movie.getPosterPath()).toString())
                .into(ivPoster);

        initMovieInfos();
        initFavoriteAction();
        initViewTrailers();
        initViewReviews();
    }

    private void initMovieInfos() {
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

    private void initFavoriteAction() {
        Set<String> allMovieFavorite = favoriteDAO.loadAllMovieFavorite(this);
        final boolean favorite = allMovieFavorite.contains(movie.getId() + "");

        final Button btMarkFavorite = findViewById(R.id.bt_movie_detail_mark_favorite);
        btMarkFavorite.setText(favorite ? R.string.movie_detail_favorite : R.string.movie_detail_mark_as_favorite);
        btMarkFavorite.setSelected(favorite);
        btMarkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavoriteState(btMarkFavorite);
            }
        });
    }

    private void changeFavoriteState(Button btMarkFavorite) {
        if (btMarkFavorite.isSelected()) {
            btMarkFavorite.setText(R.string.movie_detail_mark_as_favorite);
            favoriteDAO.deleteMovieFavorite(this, movie.getId() + "");

        } else {
            btMarkFavorite.setText(R.string.movie_detail_favorite);
            favoriteDAO.saveMovieFavorite(this, movie.getId() + "");
        }

        btMarkFavorite.setSelected(!btMarkFavorite.isSelected());
    }

    private void initViewTrailers() {
        adapterTrailer = new TrailerAdapter(this);
        RecyclerView rvTrailers = findViewById(R.id.rv_movie_detail_trailers);
        rvTrailers.setAdapter(adapterTrailer);

        View pbLoadingTrailers = findViewById(R.id.pb_movie_detail_loading_trailers);
        View cgErrorLoadingTrailers = findViewById(R.id.cg_movie_detail_error_loading_trailers);
        findViewById(R.id.bt_movie_detail_error_loading_trailers).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadTrailers();
                    }
                }
        );

        trailerListener = initTrailerListener(
                adapterTrailer, rvTrailers, pbLoadingTrailers, cgErrorLoadingTrailers);
    }

    private TaskListener<TrailerInfo> initTrailerListener(
            final TrailerAdapter adapter, final RecyclerView recyclerView,
            final View pbLoading, final View cgErrorLoading) {
        return new TaskListener<TrailerInfo>() {
            @Override
            public void showLoading() {
                recyclerView.setVisibility(View.GONE);
                cgErrorLoading.setVisibility(View.GONE);

                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void showErrorMessage() {
                recyclerView.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);

                cgErrorLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void showResult(TrailerInfo result) {
                pbLoading.setVisibility(View.GONE);
                cgErrorLoading.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);
                adapter.addTrailers(result.getTrailers());
            }
        };
    }

    private void loadTrailers() {
        new SearchMovieTrailersTask(trailerListener).execute(movie.getId() + "");
    }

    private void initViewReviews() {
        adapterReviews = new ReviewsAdapter(this);
        RecyclerView rvReviews = findViewById(R.id.rv_movie_detail_reviews);
        rvReviews.setAdapter(adapterReviews);

        View pbLoadingReviews = findViewById(R.id.pb_movie_detail_loading_reviews);
        View cgErrorLoadingReviews = findViewById(R.id.cg_movie_detail_error_loading_reviews);
        findViewById(R.id.bt_movie_detail_error_loading_reviews).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadReviews(reviewPage);
                    }
                }
        );

        reviewsListener = initReviewListener(
                adapterReviews, rvReviews, pbLoadingReviews, cgErrorLoadingReviews);
    }

    private TaskListener<ReviewInfo> initReviewListener(
            final ReviewsAdapter adapter, final RecyclerView recyclerView,
            final View pbLoading, final View cgErrorLoading) {
        return new TaskListener<ReviewInfo>() {
            @Override
            public void showLoading() {
                recyclerView.setVisibility(View.GONE);
                cgErrorLoading.setVisibility(View.GONE);

                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void showErrorMessage() {
                recyclerView.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);

                cgErrorLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void showResult(ReviewInfo result) {
                pbLoading.setVisibility(View.GONE);
                cgErrorLoading.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);
                adapter.addReviews(result.getReviews());
            }
        };
    }

    private void loadReviews(int reviewPage) {
        new SearchMovieReviewsTask(reviewsListener).execute(movie.getId() + "", reviewPage + "");
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

    @Override
    public void onClickTrailer(Trailer trailer) {
        Uri uri = trailer.getUri();

        if (uri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_VIEW);
            shareIntent.setData(uri);

            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent);
            } else
                Toast.makeText(this, R.string.movie_detail_error_no_app_share, Toast.LENGTH_LONG).show();

        } else Toast.makeText(this, R.string.movie_detail_error_sharing, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadLastItem() {
        loadReviews(++reviewPage);
    }
}
