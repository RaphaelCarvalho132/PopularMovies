package com.raphael.carvalho.android.popularmovies.detail.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raphael.carvalho.android.popularmovies.R;
import com.raphael.carvalho.android.popularmovies.detail.adapter.ReviewsAdapter.ReviewViewHolder;
import com.raphael.carvalho.android.popularmovies.detail.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private final List<Review> reviews;
    private ReviewListener listener;

    public ReviewsAdapter(@NonNull ReviewListener listener) {
        this.listener = listener;
        reviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReviewViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_review, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder movieViewHolder, int i) {
        if (i + 1 == getItemCount()) listener.onLoadLastItem();
        movieViewHolder.bind(reviews.get(i));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void addReviews(ArrayList<Review> reviews) {
        int size = this.reviews.size();
        this.reviews.addAll(reviews);

        notifyItemRangeInserted(size, reviews.size());
    }

    public interface ReviewListener {
        void onLoadLastItem();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvContent;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_review_content);
        }

        void bind(Review review) {
            tvContent.setText(review.getContent());
        }
    }
}
