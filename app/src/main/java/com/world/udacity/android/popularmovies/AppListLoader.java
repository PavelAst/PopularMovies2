package com.world.udacity.android.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.world.udacity.android.popularmovies.model.MovieItem;
import com.world.udacity.android.popularmovies.utils.Most;

import java.util.List;

/**
 * A custom Loader that loads all movies.
 */
public class AppListLoader extends AsyncTaskLoader<List<MovieItem>> {

    public interface LoaderOnStartHandler {
        void onLoad(boolean start);
    }

    private Bundle mArgs;
    private final LoaderOnStartHandler mOnStartHandler;

    private List<MovieItem> mMovieItems = null;

    public AppListLoader(Context context, Bundle args, LoaderOnStartHandler onStartHandler) {
        super(context);
        mArgs = args;
        mOnStartHandler = onStartHandler;
    }

    @Override
    protected void onStartLoading() {
        if (mArgs == null) {
            return;
        }

        if (mMovieItems != null) {
            deliverResult(mMovieItems);
        } else {
            if (mOnStartHandler != null) {
                mOnStartHandler.onLoad(true);
            }
            forceLoad();
        }
    }

    @Override
    public List<MovieItem> loadInBackground() {
        if (!mArgs.containsKey(MainActivity.SEARCH_QUERY_PAGE) ||
                !mArgs.containsKey(MainActivity.SEARCH_QUERY_SORT)) {
            return null;
        }

        /* Extract the search query from the args using our constant */
        int searchQueryPage = mArgs.getInt(MainActivity.SEARCH_QUERY_PAGE);
        Most searchQuerySort = (Most) mArgs.getSerializable(MainActivity.SEARCH_QUERY_SORT);

        return new MovieFetchr().fetchItems(searchQuerySort, searchQueryPage);
    }

    /**
     * Sends the result of the load to the registered listener.
     *
     * @param data The result of the load
     */
    @Override
    public void deliverResult(List<MovieItem> data) {
        mMovieItems = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }
}
