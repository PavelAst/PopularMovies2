package com.world.udacity.android.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.world.udacity.android.popularmovies.model.VideoTrailer;

import java.util.List;

class AppTrailersLoader extends AsyncTaskLoader<List<VideoTrailer>> {

    private Bundle mArgs;
    private List<VideoTrailer> mTrailers = null;

    public AppTrailersLoader(Context context, Bundle loaderArgs) {
        super(context);
        mArgs = loaderArgs;
    }

    @Override
    protected void onStartLoading() {
        if (null == mArgs) {
            return;
        }

        if (mTrailers != null) {
            deliverResult(mTrailers);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<VideoTrailer> loadInBackground() {
        if (!mArgs.containsKey(DetailsTrailersFragment.MOVIE_ID)) {
            return null;
        }

        int movieId = mArgs.getInt(DetailsTrailersFragment.MOVIE_ID);

        return new MovieFetchr().fetchMovieTrailers(movieId);
    }

    @Override
    public void deliverResult(@Nullable List<VideoTrailer> data) {
        mTrailers = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }
}
