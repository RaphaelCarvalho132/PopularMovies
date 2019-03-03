package com.raphael.carvalho.android.popularmovies.movies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raphael.carvalho.android.popularmovies.R;
import com.raphael.carvalho.android.popularmovies.movies.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.movies.adapter.MoviesAdapter.MovieViewHolder;
import com.raphael.carvalho.android.popularmovies.movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private MoviesListener listener;
    private final List<Movie> movies;

    public MoviesAdapter(@NonNull MoviesListener listener) {
        this.listener = listener;
        movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MovieViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_movie, viewGroup, false),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        if (i + 1 == getItemCount()) listener.onLoadLastItem();
        movieViewHolder.bind(movies.get(i));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addMovies(ArrayList<Movie> movies) {
        int size = this.movies.size();
        this.movies.addAll(movies);

        notifyItemRangeInserted(size, movies.size());
    }

    public interface MoviesListener {
        void onClickMovie(Movie movie);

        void onLoadLastItem();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MoviesListener listener;
        private final ImageView ivPoster;

        private Movie movie;

        MovieViewHolder(@NonNull View itemView, MoviesListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.listener = listener;
            ivPoster = itemView.findViewById(R.id.iv_movie_poster);
        }

        void bind(Movie movie) {
            this.movie = movie;

            Picasso.get()
                    .load(MoviesUrl.buildPosterUri(movie.getPosterPath()).toString())
                    .into(ivPoster);
            ivPoster.setContentDescription(movie.getTitle());
        }

        @Override
        public void onClick(View v) {
            listener.onClickMovie(movie);
        }
    }
}
