package com.world.udacity.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.world.udacity.android.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;
import com.world.udacity.android.popularmovies.utils.Most;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {

    // Turn logging on or off
    private static final boolean L = false;

    private static final String TAG = "MovieMainActivity";

    private RecyclerView mMoviesRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private MovieAdapter mMovieAdapter;
    private int mLastPage = 1;
    private int mFirstVisibleItemPosition = 0;
    private int mX = 0;
    private int mY = 0;
    private Most mSortOption = Most.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRecyclerView = findViewById(R.id.movies_recycler_view);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        int numberOfColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mMoviesRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        if (isOnline()) {
            loadMovieItemsData();
        } else {
            showErrorMessage(R.string.error_message_network);
        }

        mMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mX = dx;
                mY = dy;

                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                mFirstVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();

                // if (L) Log.i(TAG, "=== onScrolled ===" + mLastPage + " dx=" + dx + ", dy=" + dy);
                if (!recyclerView.canScrollVertically(1)) {
                    if (L) Log.i(TAG, "~~~ I can't !!! ~~~");
                    mLastPage += 1;
                    new FetchMoviesTask().execute(mLastPage);
                }
            }
        });
    }

    private void loadMovieItemsData() {
        showMovieItemsView();
        new FetchMoviesTask().execute(mLastPage);
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

    private class FetchMoviesTask extends AsyncTask<Integer, Void, List<MovieItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieItem> doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }
            return new MovieFetchr().fetchItems(mSortOption, params[0]);
        }

        @Override
        protected void onPostExecute(List<MovieItem> items) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (items != null) {
                showMovieItemsView();
                setupAdapter(items);
            } else {
                showErrorMessage(R.string.error_message_all);
            }
        }
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
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_most_popular || id == R.id.action_most_top_rated) {
            mSortOption = (id == R.id.action_most_popular) ? Most.POPULAR : Most.TOP_RATED;
            mLastPage = 1;
            mFirstVisibleItemPosition = 0;
            mX = 0;
            mY = 0;
            setupAdapter(null);
            loadMovieItemsData();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
