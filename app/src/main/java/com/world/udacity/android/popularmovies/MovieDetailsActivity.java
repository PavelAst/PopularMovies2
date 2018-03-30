package com.world.udacity.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.world.udacity.android.popularmovies.model.MovieItem;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    public static final String MOVIE_ITEM = "movie_item_object";

    private TextView mMovieTitleTV;
    private SimpleDraweeView mPosterIV;
    private TextView mMovieReleleaseYearTV;
    private TextView mVoteAverageTV;
    private TextView mOverviewTV;
    private RecyclerView mYoutubeTrailersRV;

    private MovieItem mMovie;
    private VideoTrailerAdapter mVideoTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new DetailsDescriptionFragment(), "DESCRIPTION");
        adapter.addFragment(new DetailsTrailersFragment(), "TRAILERS");
        adapter.addFragment(new DetailsReviewsFragment(), "REVIEWS");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SimpleDraweeView backDropIV = findViewById(R.id.iv_backdropUp);
//        mMovieTitleTV = findViewById(R.id.tv_movie_title);
//        mPosterIV = findViewById(R.id.iv_poster);
//        mMovieReleleaseYearTV = findViewById(R.id.tv_movie_release_year);
//        mVoteAverageTV = findViewById(R.id.tv_vote_average);
//        mOverviewTV = findViewById(R.id.tv_overview);
//        mYoutubeTrailersRV = findViewById(R.id.youtube_trailers_recycler_view);
//
//        Intent intentThatStartedThisActivity = getIntent();
//
//        if (intentThatStartedThisActivity != null) {
//            if (intentThatStartedThisActivity.hasExtra(MOVIE_ITEM)) {
//                mMovie = (MovieItem) intentThatStartedThisActivity.getParcelableExtra(MOVIE_ITEM);
//
//                //Set toolbar title
//                CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);
//                collapsingToolbar.setTitle(mMovie.getTitle());
//
//                mMovieTitleTV.setText(mMovie.getTitle());
//
//                String backdropUrl = TheMoviedbConstants.getPosterUrl("w780", mMovie.getBackdropPath());
//                backDropIV.setImageURI(backdropUrl);
//
//                String posterUrl = TheMoviedbConstants.getPosterUrl("w342", mMovie.getPosterPath());
//                mPosterIV.setImageURI(posterUrl);
//
//                mMovieReleleaseYearTV.setText(mMovie.getReleaseYear());
//
//                String rating = getString(R.string.rating_string, mMovie.getVoteAverage());
//                mVoteAverageTV.setText(rating);
//
//                mOverviewTV.setText(mMovie.getOverview());
//
//                LinearLayoutManager layoutManager = new LinearLayoutManager(this,
//                        LinearLayoutManager.HORIZONTAL, false);
//                mYoutubeTrailersRV.setLayoutManager(layoutManager);
//                mYoutubeTrailersRV.setHasFixedSize(true);
//                mVideoTrailerAdapter = new VideoTrailerAdapter();
//                mYoutubeTrailersRV.setAdapter(mVideoTrailerAdapter);
//            }
//        }

    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
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


}
