package com.world.udacity.android.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.world.udacity.android.popularmovies.model.MovieItem;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

import org.w3c.dom.Text;

public class DetailsDescriptionFragment extends Fragment {

    String mTitle;
    String mPosterUrl;
    String mReleaseYear;
    String mVoteAverage;
    String mOverview;

    public DetailsDescriptionFragment() {
        // Required empty public constructor
    }

    public static DetailsDescriptionFragment newInstance(MovieItem movieItem) {
        Bundle args = new Bundle();
        args.putParcelable(MovieDetailsActivity.MOVIE_ITEM, movieItem);
        DetailsDescriptionFragment fragment = new DetailsDescriptionFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieItem movie = getArguments().getParcelable(MovieDetailsActivity.MOVIE_ITEM);
        mPosterUrl = TheMoviedbConstants.getPosterUrl("w342", movie.getPosterPath());
        mTitle = movie.getTitle();
        mReleaseYear = movie.getReleaseYear();
        mVoteAverage = getString(R.string.rating_string, movie.getVoteAverage());
        mOverview = movie.getOverview();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_description, container, false);
        SimpleDraweeView posterIV = v.findViewById(R.id.iv_poster);
        TextView movieTitleTV = v.findViewById(R.id.tv_movie_title);
        TextView releaseYearTV = v.findViewById(R.id.tv_movie_release_year);
        TextView voteAverageTV = v.findViewById(R.id.tv_vote_average);
        TextView overview = v.findViewById(R.id.tv_overview);

        posterIV.setImageURI(mPosterUrl);
        movieTitleTV.setText(mTitle);
        releaseYearTV.setText(mReleaseYear);
        voteAverageTV.setText(mVoteAverage);
        overview.setText(mOverview);

        return v;
    }

}
