package com.world.udacity.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_ITEM = "movie_item_object";

    private TextView mMovieTitleTV;
    private SimpleDraweeView mBackdropIV;
    private SimpleDraweeView mPosterIV;
    private TextView mMovieReleleaseYearTV;
    private TextView mVoteAverageTV;
    private TextView mOverviewTV;

    private MovieItem mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mMovieTitleTV = findViewById(R.id.tv_movie_title);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MOVIE_ITEM)) {
                mMovie = (MovieItem) intentThatStartedThisActivity.getParcelableExtra(MOVIE_ITEM);
                mMovieTitleTV.setText(mMovie.getTitle());
            }
        }

        mBackdropIV = findViewById(R.id.iv_backdrop);
        String backdropUrl = TheMoviedbConstants.getPosterUrl("w780", mMovie.getBackdropPath());
        mBackdropIV.setImageURI(backdropUrl);

        mPosterIV = findViewById(R.id.iv_poster);
        String posterUrl = TheMoviedbConstants.getPosterUrl("w342", mMovie.getPosterPath());
        mPosterIV.setImageURI(posterUrl);

        mMovieReleleaseYearTV = findViewById(R.id.tv_movie_release_year);
        mMovieReleleaseYearTV.setText(mMovie.getReleaseYear());

        mVoteAverageTV = findViewById(R.id.tv_vote_average);
        String rating = getString(R.string.rating_string, mMovie.getVoteAverage());
        mVoteAverageTV.setText(rating);

        mOverviewTV = findViewById(R.id.tv_overview);
        mOverviewTV.setText(mMovie.getOverview());
    }
}
