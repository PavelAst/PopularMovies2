package com.world.udacity.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_ITEM = "movie_item_object";

    private TextView mMovieTitleTV;
    private SimpleDraweeView mPosterIV;
    private TextView mMovieReleleaseYearTV;
    private TextView mVoteAverageTV;
    private TextView mOverviewTV;

    private MovieItem mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mMovieTitleTV = findViewById(R.id.tv_movie_title);
        mPosterIV = findViewById(R.id.iv_poster);
        mMovieReleleaseYearTV = findViewById(R.id.tv_movie_release_year);
        mVoteAverageTV = findViewById(R.id.tv_vote_average);
        mOverviewTV = findViewById(R.id.tv_overview);
        SimpleDraweeView backDropIV = findViewById(R.id.iv_backdropUp);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MOVIE_ITEM)) {
                mMovie = (MovieItem) intentThatStartedThisActivity.getParcelableExtra(MOVIE_ITEM);

                //Set toolbar title
                CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);
                collapsingToolbar.setTitle(mMovie.getTitle());

                mMovieTitleTV.setText(mMovie.getTitle());

                String backdropUrl = TheMoviedbConstants.getPosterUrl("w780", mMovie.getBackdropPath());
                backDropIV.setImageURI(backdropUrl);

                String posterUrl = TheMoviedbConstants.getPosterUrl("w342", mMovie.getPosterPath());
                mPosterIV.setImageURI(posterUrl);

                mMovieReleleaseYearTV.setText(mMovie.getReleaseYear());

                String rating = getString(R.string.rating_string, mMovie.getVoteAverage());
                mVoteAverageTV.setText(rating);

                mOverviewTV.setText(mMovie.getOverview());
            }
        }

    }
}
