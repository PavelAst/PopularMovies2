package com.world.udacity.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
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

import com.world.udacity.android.popularmovies.adapters.MovieAdapter;
import com.world.udacity.android.popularmovies.adapters.MovieAdapter.MovieAdapterOnClickHandler;
import com.world.udacity.android.popularmovies.data.MovieContract;
import com.world.udacity.android.popularmovies.model.MovieItem;
import com.world.udacity.android.popularmovies.utils.Most;
import com.world.udacity.android.popularmovies.utils.SortPreferences;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieAdapterOnClickHandler,
        LoaderCallbacks {

    // Turn logging on or off
    private static final boolean L = true;

    public static final String TAG = "MovieMainActivity";
    /* A constant to save and restore the page  */
    public static final String SEARCH_QUERY_PAGE = "query_page";
    public static final String SEARCH_QUERY_SORT = "query_sort";
    private static final int COLUMN_WIDTH = 180;

    private static final int NETWORK_LOADER_ID = 61;
    private static final int CURSOR_LOADER_ID = 62;
    int mLoaderId = NETWORK_LOADER_ID;

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
        Log.i(TAG, "*** MainActivity - onCreate");

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

                if (!recyclerView.canScrollVertically(1) && itemsCount > 0 && mLoaderId == NETWORK_LOADER_ID) {
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
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        Most sortOption = SortPreferences.getSortCriteria(this);
        mLoaderId = (sortOption == Most.FAVORITES) ? CURSOR_LOADER_ID : NETWORK_LOADER_ID;
        getSupportLoaderManager().initLoader(mLoaderId, null, this);

        loadMovieItemsData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (L) Log.i(TAG, "*** MainActivity - onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (L) Log.i(TAG, "*** MainActivity - onResume");
        getSupportLoaderManager().restartLoader(mLoaderId, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (L) Log.i(TAG, "*** MainActivity - onPause");
    }

    private void loadMovieItemsData() {
        Most sortOption = SortPreferences.getSortCriteria(this);

        if (!isOnline() && sortOption != Most.FAVORITES) {
            showErrorMessage(R.string.error_message_network);
            return;
        }
        Log.i(TAG, "*** MainActivity - In loadMovieItemsData, mLastPage = " + mLastPage);

        showMovieItemsView();

        Bundle queryBundle = new Bundle();
        if (sortOption != Most.FAVORITES) {
            queryBundle.putInt(SEARCH_QUERY_PAGE, mLastPage);
            queryBundle.putSerializable(SEARCH_QUERY_SORT, sortOption);
        }

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(mLoaderId, queryBundle, this);
    }

    private void setupAdapter(List<MovieItem> items) {
        if (L) Log.i(TAG, "*** MainActivity - setupAdapter");
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
    public Loader onCreateLoader(int id, final Bundle loaderArgs) {
        if (L) Log.i(TAG, "*** MainActivity - onCreateLoader");

        switch (id) {
            case NETWORK_LOADER_ID:
                return new AppMovieNetworkLoader(this, loaderArgs, new AppMovieNetworkLoader.LoaderOnStartHandler() {
                    @Override
                    public void onLoad(boolean start) {
                        mLoadingIndicator.setVisibility(View.VISIBLE);
                    }
                });
            case CURSOR_LOADER_ID:
                Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                return new CursorLoader(this, uri, null,
                        null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (L) Log.i(TAG, "*** MainActivity - onLoadFinished");

        if (null == data) {
            if (L) Log.i(TAG, "### Error in Loader , id = " + loader.getId());
            showErrorMessage(R.string.error_message_all);
        } else {
            showMovieItemsView();
            List<MovieItem> movies = null;

            switch (loader.getId()) {
                case NETWORK_LOADER_ID:
                    movies = (List<MovieItem>) data;
                    break;
                case CURSOR_LOADER_ID:
                    movies = getMoviesFromCursor((Cursor) data);
                    break;
            }
            setupAdapter(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        loader = null;
        if (L) Log.i(TAG, "*** MainActivity - onLoaderReset");
    }

    private List<MovieItem> getMoviesFromCursor(Cursor cursor) {
        if (L) Log.i(TAG, "*** MainActivity - getMoviesFromCursor");
        List<MovieItem> movies = new ArrayList<>();
        if (cursor.getCount() == 0) {
            return null;
        }
        if (cursor.moveToFirst()) {
            do {
                MovieItem movie = new MovieItem();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));

                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_IMAGE));
                movie.setPosterImage(getBitmap(imageBytes));

                movies.add(movie);

            } while (cursor.moveToNext());
        }

        return movies;
    }

    private Bitmap getBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /*
        void setPosterFromCursor(Cursor cursor) {
    }
     */

    /**
     * This method is used when we are resetting data, so that at one point in time during a
     * refresh of our data, you can see that there is no data showing.
     */
    private void invalidateData() {
        if (L) Log.i(TAG, "*** MainActivity - invalidateData");
//        setupAdapter(null);
        mMovieAdapter.clearMovieItems();
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

        Most showOption = SortPreferences.getSortCriteria(this);

        switch (showOption) {
            case POPULAR:
                menu.findItem(R.id.action_most_popular).setChecked(true);
                break;
            case TOP_RATED:
                menu.findItem(R.id.action_most_top_rated).setChecked(true);
                break;
            case FAVORITES:
                menu.findItem(R.id.action_my_favorites).setChecked(true);
                break;
        }

        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_most_popular ||
                id == R.id.action_most_top_rated ||
                id == R.id.action_my_favorites) {
            Most sortOption = SortPreferences.getSortCriteria(this);

            // The same option was chosen
            if ((sortOption == Most.POPULAR && id == R.id.action_most_popular) ||
                    (sortOption == Most.TOP_RATED && id == R.id.action_most_top_rated) ||
                    (sortOption == Most.FAVORITES && id == R.id.action_my_favorites)) {
                return true;
            }

            getSupportLoaderManager().destroyLoader(mLoaderId);
            if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }
            switch (id) {
                case R.id.action_most_popular:
                    sortOption = Most.POPULAR;
                    mLoaderId = NETWORK_LOADER_ID;
                    break;
                case R.id.action_most_top_rated:
                    mLoaderId = NETWORK_LOADER_ID;
                    sortOption = Most.TOP_RATED;
                    break;
                case R.id.action_my_favorites:
                    mLoaderId = CURSOR_LOADER_ID;
                    sortOption = Most.FAVORITES;
                    break;
            }
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
