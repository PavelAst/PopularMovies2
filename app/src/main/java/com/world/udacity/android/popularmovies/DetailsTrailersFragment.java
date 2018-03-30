package com.world.udacity.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailsTrailersFragment extends Fragment {

    public DetailsTrailersFragment() {
        // Required empty public constructor
    }

    public static DetailsTrailersFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(MovieDetailsActivity.MOVIE_ID, id);
        DetailsTrailersFragment fragment = new DetailsTrailersFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int movieId = getArguments().getInt(MovieDetailsActivity.MOVIE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trailers, container, false);

        return v;
    }
}
