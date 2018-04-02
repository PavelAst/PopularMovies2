package com.world.udacity.android.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.world.udacity.android.popularmovies.adapters.VideoTrailerAdapter;
import com.world.udacity.android.popularmovies.model.MovieItem;
import com.world.udacity.android.popularmovies.model.VideoTrailer;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

import java.util.ArrayList;


public class DetailsTrailersFragment extends Fragment {

    // Turn logging on or off
    private static final boolean L = false;
    public static final String TAG = "MovieMethod";

    private ArrayList<VideoTrailer> mTrailers;

    private RecyclerView mYoutubeTrailersRV;
    private TextView mErrorMessageTV;

    private VideoTrailerAdapter mVideoTrailerAdapter;

    public DetailsTrailersFragment() {
        // Required empty public constructor
    }

    public static DetailsTrailersFragment newInstance(MovieItem movieItem) {
        Bundle args = new Bundle();
        args.putParcelable(MovieDetailsActivity.MOVIE_ITEM, movieItem);
        DetailsTrailersFragment fragment = new DetailsTrailersFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Get intent message - trailers ************");
            if (intent.getAction().equalsIgnoreCase(MovieDetailsActivity.GET_TRAILERS_LIST_EVENT)) {
                mTrailers = intent.getParcelableArrayListExtra(MovieDetailsActivity.TRAILERS);
                showYoutubeTrailersView();
                mVideoTrailerAdapter.setTrailers(mTrailers);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (L) Log.i(TAG, " <> DetailsTrailersFragment - onCreate");
        MovieItem movie = getArguments().getParcelable(MovieDetailsActivity.MOVIE_ITEM);
        mTrailers = movie.getTrailers();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MovieDetailsActivity.GET_TRAILERS_LIST_EVENT);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mUpdateUIReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (L) Log.i(TAG, " <> DetailsTrailersFragment - onCreateView");
        View v = inflater.inflate(R.layout.fragment_trailers, container, false);
        mYoutubeTrailersRV = v.findViewById(R.id.youtube_trailers_recycler_view);
        mErrorMessageTV = v.findViewById(R.id.tv_error_tr_message_display);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mYoutubeTrailersRV.setLayoutManager(layoutManager);
        mYoutubeTrailersRV.setHasFixedSize(true);

        mVideoTrailerAdapter = new VideoTrailerAdapter(getActivity(), new VideoTrailerAdapter.VideoTrailerAdapterOnClickHandler() {
            @Override
            public void onClick(String trailerKey) {
                String trailerUrl = TheMoviedbConstants.getYoutubeTrailerUrl(trailerKey);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivity(intent);
                }
            }
        });
        mYoutubeTrailersRV.setAdapter(mVideoTrailerAdapter);
        if (mTrailers != null && !mTrailers.isEmpty()) {
            showYoutubeTrailersView();
            mVideoTrailerAdapter.setTrailers(mTrailers);
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (L) Log.i(TAG, " <> DetailsTrailersFragment - onPause");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mUpdateUIReceiver);
    }

    private void showYoutubeTrailersView() {
        mYoutubeTrailersRV.setVisibility(View.VISIBLE);
        mErrorMessageTV.setVisibility(View.INVISIBLE);
    }

}
