package com.world.udacity.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.world.udacity.android.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;
import com.world.udacity.android.popularmovies.utils.Most;
import com.world.udacity.android.popularmovies.utils.SortPreferences;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieAdapterOnClickHandler,
        LoaderCallbacks<List<MovieItem>> {

    // Turn logging on or off
    private static final boolean L = false;

    private static final String TAG = "MovieMainActivity";
    /* A constant to save and restore the page  */
    public static final String SEARCH_QUERY_PAGE = "query_page";
    public static final String SEARCH_QUERY_SORT = "query_sort";
    private static final int COLUMN_WIDTH = 180;

    private static final int MOVIE_LOADER_ID = 64;

    private RecyclerView mMoviesRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private MovieAdapter mMovieAdapter;
    private int mLastPage = 1;
    private int mFirstVisibleItemPosition = 0;
    private int mX = 0;
    private int mY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        setContentView(R.layout.activity_main);

        mMoviesRecyclerView = findViewById(R.id.movies_recycler_view);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        int numberOfColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mMoviesRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this, this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        mMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mX = dx;
                mY = dy;

                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                mFirstVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                int itemsCount = gridLayoutManager.getItemCount();

                if (!recyclerView.canScrollVertically(1) && itemsCount > 0) {
                    mLastPage += 1;
                    if (L)
                        Log.i(TAG, "** " + itemsCount + " In onScrolled, mLastPage = " + mLastPage);
                    loadMovieItemsData();
                }
            }
        });

        mMoviesRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        float columnWidthInPixels = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                COLUMN_WIDTH,
                                MainActivity.this.getResources().getDisplayMetrics());
                        int width = mMoviesRecyclerView.getWidth();
                        int columnNumber = Math.round(width / columnWidthInPixels);
                        mMoviesRecyclerView.setLayoutManager(
                                new GridLayoutManager(MainActivity.this, columnNumber));
                        mMoviesRecyclerView.scrollToPosition(mFirstVisibleItemPosition);
                        mMoviesRecyclerView
                                .getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                    }
                });

        /*
         * From MainActivity, we have implemented the LoaderCallbacks interface with the type of
         * String array. (implements LoaderCallbacks<String[]>) The variable callback is passed
         * to the call to initLoader below. This means that whenever the loaderManager has
         * something to notify us of, it will do so through this callback.
         */
        LoaderCallbacks<List<MovieItem>> callback = MainActivity.this;

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, callback);

        loadMovieItemsData();
    }


    private void loadMovieItemsData() {
        if (!isOnline()) {
            showErrorMessage(R.string.error_message_network);
            return;
        }
        if (L) Log.i(TAG, "** In loadMovieItemsData, mLastPage = " + mLastPage);

        showMovieItemsView();

        Most sortOption = SortPreferences.getSortCriteria(this);
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(SEARCH_QUERY_PAGE, mLastPage);
        queryBundle.putSerializable(SEARCH_QUERY_SORT, sortOption);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(MOVIE_LOADER_ID, queryBundle, this);
    }

    private void setupAdapter(List<MovieItem> items) {
        mMovieAdapter.setMovieItems(items);
        mMoviesRecyclerView.scrollToPosition(mFirstVisibleItemPosition);
        mMoviesRecyclerView.scrollBy(mX, mY);
    }

    @Override
    public void onClick(MovieItem movie) {
        Context context = this;
        Intent intentMovieDetails = new Intent(context, MovieDetailsActivity.class);
        intentMovieDetails.putExtra(MovieDetailsActivity.MOVIE_ITEM, movie);
        startActivity(intentMovieDetails);
    }

    private void showMovieItemsView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(int resid) {
        /* First, hide the currently visible data */
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setText(resid);
    }

    @Override
    public Loader<List<MovieItem>> onCreateLoader(int id, final Bundle loaderArgs) {
        Log.i(TAG, "<> In onCreateLoader");
        return new AppListLoader(this, loaderArgs, new AppListLoader.LoaderOnStartHandler() {
            @Override
            public void onLoad(boolean start) {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<List<MovieItem>> loader, List<MovieItem> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (null == data) {
            showErrorMessage(R.string.error_message_all);
        } else {
            showMovieItemsView();
            setupAdapter(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieItem>> loader) {

    }

    /**
     * This method is used when we are resetting data, so that at one point in time during a
     * refresh of our data, you can see that there is no data showing.
     */
    private void invalidateData() {
        setupAdapter(null);
    }

    protected boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu);

        Most sortOption = SortPreferences.getSortCriteria(this);
        MenuItem menuItem = (sortOption == Most.POPULAR) ?
                menu.findItem(R.id.action_most_popular) : menu.findItem(R.id.action_most_top_rated);
        menuItem.setChecked(true);

        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_most_popular || id == R.id.action_most_top_rated) {
            Most sortOption = SortPreferences.getSortCriteria(this);

            // The same option was chosen
            if ((sortOption == Most.POPULAR && id == R.id.action_most_popular) ||
                    (sortOption == Most.TOP_RATED && id == R.id.action_most_top_rated)) {
                return true;
            }

            if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }
            sortOption = (id == R.id.action_most_popular) ? Most.POPULAR : Most.TOP_RATED;
            SortPreferences.setSortCriteria(this, sortOption);
            mLastPage = 1;
            mFirstVisibleItemPosition = 0;
            mX = 0;
            mY = 0;
            invalidateData();
            loadMovieItemsData();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
