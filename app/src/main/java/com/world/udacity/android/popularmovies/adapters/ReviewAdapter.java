package com.world.udacity.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.world.udacity.android.popularmovies.R;
import com.world.udacity.android.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final Context mContext;
    private ArrayList<Review> mReviews;

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public final TextView mAuthorTV;
        public final TextView mContentTV;

        public ReviewViewHolder(View reviewView) {
            super(reviewView);
            mAuthorTV = reviewView.findViewById(R.id.review_author_tv);
            mContentTV = reviewView.findViewById(R.id.review_tv);
        }

        public void bindTo(Review review) {
            mAuthorTV.setText(review.getAuthor());
            mContentTV.setText(review.getContent());
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutIdForRecyclerView = R.layout.movie_review;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForRecyclerView, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int position) {
        Review review = mReviews.get(position);
        reviewViewHolder.bindTo(review);
    }

    @Override
    public int getItemCount() {
        return (mReviews == null) ? 0 : mReviews.size();
    }

    public void setReviews(ArrayList<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

}
