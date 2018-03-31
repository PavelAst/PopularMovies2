package com.world.udacity.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DetailsTrailersFragment extends Fragment {

    private RecyclerView mYoutubeTrailersRV;
    private VideoTrailerAdapter mVideoTrailerAdapter;

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
        mYoutubeTrailersRV = v.findViewById(R.id.youtube_trailers_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mYoutubeTrailersRV.setLayoutManager(layoutManager);
        mYoutubeTrailersRV.setHasFixedSize(true);
        mVideoTrailerAdapter = new VideoTrailerAdapter();
        mYoutubeTrailersRV.setAdapter(mVideoTrailerAdapter);

        return v;
    }

}
