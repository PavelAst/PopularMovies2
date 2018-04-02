package com.world.udacity.android.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.world.udacity.android.popularmovies.model.Review;

import java.util.List;

public class AppReviewsLoader extends AsyncTaskLoader<List<Review>> {

    private Bundle mArgs;
    private List<Review> mReviews = null;

    public AppReviewsLoader(@NonNull Context context, Bundle loaderArgs) {
        super(context);
        mArgs = loaderArgs;
    }

    @Override
    protected void onStartLoading() {
        if (null == mArgs) {
            return;
        }

        if (mReviews != null) {
            deliverResult(mReviews);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<Review> loadInBackground() {
        if (!mArgs.containsKey(MovieDetailsActivity.MOVIE_ID)) {
            return null;
        }

        int movieId = mArgs.getInt(MovieDetailsActivity.MOVIE_ID);

        return new MovieFetchr().fetchMovieReviews(movieId);
    }

    @Override
    public void deliverResult(@Nullable List<Review> data) {
        mReviews = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }
}
