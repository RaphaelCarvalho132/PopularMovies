package com.raphael.carvalho.android.popularmovies.movies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raphael.carvalho.android.popularmovies.R;
import com.raphael.carvalho.android.popularmovies.movies.adapter.MoviesAdapter.MovieViewHolder;
import com.raphael.carvalho.android.popularmovies.movies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private final List<Movie> movies;

    public MoviesAdapter() {
        movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MovieViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_movie, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
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

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMovieTitle;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
        }

        void bind(Movie movie) {
            tvMovieTitle.setText(movie.getTitle());
        }
    }
}
