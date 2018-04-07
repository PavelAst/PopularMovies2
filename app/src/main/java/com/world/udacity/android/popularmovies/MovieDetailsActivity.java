package com.world.udacity.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.world.udacity.android.popularmovies.data.MovieContract;
import com.world.udacity.android.popularmovies.model.MovieItem;
import com.world.udacity.android.popularmovies.model.Review;
import com.world.udacity.android.popularmovies.model.VideoTrailer;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks {

    // Turn logging on or off
    private static final boolean L = true;

    private static final String TAG = "MovieDetailsActivity";
    public static final String MOVIE_ITEM = "movie_item_object";
    public static final String MOVIE_ID = "movie_id";
    private static final int YOUTUBE_LOADER_ID = 25;
    private static final int REVIEW_LOADER_ID = 20;
    public static final String GET_TRAILERS_LIST_EVENT = "GetTrailersListEvent";
    public static final String TRAILERS = "trailers";
    public static final String GET_REVIEWS_LIST_EVENT = "GetReviewsListEvent";
    public static final String REVIEWS = "reviews";

    private MovieItem mMovie;
    private boolean mIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ImageView backDropIV = findViewById(R.id.iv_backdropUp);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MOVIE_ITEM)) {
                mMovie = (MovieItem) intentThatStartedThisActivity.getParcelableExtra(MOVIE_ITEM);

                //Set toolbar title
                collapsingToolbar.setTitle(mMovie.getTitle());

                String backdropUrl = TheMoviedbConstants.getMoviePosterUrl("w780", mMovie.getBackdropPath());
                Picasso.with(this)
                        .load(backdropUrl)
                        .into(backDropIV);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(DetailsDescriptionFragment.newInstance(mMovie), "DESCRIPTION");
        adapter.addFragment(DetailsTrailersFragment.newInstance(mMovie), "TRAILERS");
        adapter.addFragment(DetailsReviewsFragment.newInstance(mMovie), "REVIEWS");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Something went wrong. Please try again.";
                mIsFavorite = !mIsFavorite;
                if (mIsFavorite) {
                    fab.setImageResource(R.drawable.ic_heart);
                    // Add to database
                    if (addTofavorites(mMovie)) {
                        message = "Movie added to favorites";
                    }
                } else {
                    fab.setImageResource(R.drawable.ic_heart_outline);
                    // Remove from database
                    if (removeFromFavorites(mMovie.getId()) > 0) {
                        message = "Movie removed from favorites";
                    }
                }
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        getSupportLoaderManager().initLoader(YOUTUBE_LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);

        loadAdditionalData();
        if (checkFavorite(mMovie.getId())) {
            mIsFavorite = true;
            fab.setImageResource(R.drawable.ic_heart);
            if (L) Log.i(TAG, "<<<< FAVORITE >>>>>");
        } else {
            mIsFavorite = false;
            fab.setImageResource(R.drawable.ic_heart_outline);
            if (L) Log.i(TAG, "---- SIMPLE -----");
        }
    }

    private void loadAdditionalData() {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(MOVIE_ID, mMovie.getId());
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(YOUTUBE_LOADER_ID, queryBundle, this);
        loaderManager.restartLoader(REVIEW_LOADER_ID, queryBundle, this);
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle loaderArgs) {
        switch (id) {
            case YOUTUBE_LOADER_ID:
                return new AppTrailersLoader(this, loaderArgs);
            case REVIEW_LOADER_ID:
                return new AppReviewsLoader(this, loaderArgs);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        if (null != data) {
            switch (loader.getId()) {
                case YOUTUBE_LOADER_ID:
                    mMovie.setTrailers((ArrayList<VideoTrailer>) data);
                    Intent intentTrailers = new Intent(GET_TRAILERS_LIST_EVENT);
                    intentTrailers.putParcelableArrayListExtra(TRAILERS, mMovie.getTrailers());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intentTrailers);
                    break;
                case REVIEW_LOADER_ID:
                    mMovie.setReviews((ArrayList<Review>) data);
                    Intent intentReviews = new Intent(GET_REVIEWS_LIST_EVENT);
                    intentReviews.putParcelableArrayListExtra(REVIEWS, mMovie.getReviews());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intentReviews);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /*
     * *** Work with ContentProvider ***
     */

    private boolean checkFavorite(int movieId) {
        // Build appropriate uri with String row id appended
        String stringId = Integer.toString(movieId);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        Cursor cursor = getContentResolver().query(uri, null,
                null, null, null);

        return (cursor != null && cursor.getCount() >= 1);
    }

    private boolean addTofavorites(MovieItem movie) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        // Insert the content values via a ContentResolver
        Uri newUri = getContentResolver().insert(uri, values);
        return (newUri != null);
    }

    private int removeFromFavorites(int movieId) {
        // Build appropriate uri with String row id appended
        String stringId = Integer.toString(movieId);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        return getContentResolver().delete(uri, null, null);
    }
}
