package com.raphael.carvalho.android.popularmovies.detail.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.raphael.carvalho.android.popularmovies.R;
import com.raphael.carvalho.android.popularmovies.detail.adapter.TrailerAdapter.TrailerViewHolder;
import com.raphael.carvalho.android.popularmovies.detail.model.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {
    private final ArrayList<Trailer> trailers;
    private final TrailerListener listener;

    public TrailerAdapter(@NonNull TrailerListener listener) {
        this.listener = listener;
        trailers = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TrailerViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_trailer, viewGroup, false),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {
        trailerViewHolder.bind(trailers.get(i));
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void addTrailers(ArrayList<Trailer> trailers) {
        int size = this.trailers.size();
        this.trailers.addAll(trailers);

        notifyItemRangeInserted(size, trailers.size());
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public interface TrailerListener {
        void onClickTrailer(Trailer trailer);
    }

    static class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TrailerListener listener;
        private final Button btPlayTrailer;

        private Trailer trailer;

        TrailerViewHolder(@NonNull View itemView, TrailerListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.listener = listener;
            btPlayTrailer = itemView.findViewById(R.id.bt_trailer_play_trailer);
        }

        void bind(Trailer trailer) {
            this.trailer = trailer;
            btPlayTrailer.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            listener.onClickTrailer(trailer);
        }
    }
}
