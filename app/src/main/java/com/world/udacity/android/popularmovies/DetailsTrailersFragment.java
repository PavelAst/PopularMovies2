package com.world.udacity.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.world.udacity.android.popularmovies.adapters.VideoTrailerAdapter;
import com.world.udacity.android.popularmovies.model.VideoTrailer;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

import java.util.List;

public class DetailsTrailersFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<VideoTrailer>> {

    // Turn logging on or off
    private static final boolean L = false;

    private static final int YOUTUBE_LOADER_ID = 25;
    public static final String MOVIE_ID = "movie_id";

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
        loadYoutubeTrailersData(movieId);
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

        return v;
    }

    private void loadYoutubeTrailersData(int id) {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(MOVIE_ID, id);
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(YOUTUBE_LOADER_ID, queryBundle, this);
    }

    @NonNull
    @Override
    public Loader<List<VideoTrailer>> onCreateLoader(int id, @Nullable Bundle loaderArgs) {
        return new AppTrailersLoader(getActivity(), loaderArgs);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<VideoTrailer>> loader, List<VideoTrailer> data) {
        if (null == data) {
            Toast.makeText(getActivity(),
                    R.string.error_message_all,
                    Toast.LENGTH_SHORT).show();
        } else {
            mVideoTrailerAdapter.setTrailersKeys(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<VideoTrailer>> loader) {

    }
    
}
