package com.world.udacity.android.popularmovies;

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

import com.world.udacity.android.popularmovies.adapters.ReviewAdapter;
import com.world.udacity.android.popularmovies.model.Review;

import java.util.List;

public class DetailsReviewsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Review>> {

    // Turn logging on or off
    private static final boolean L = true;
    public static final String TAG = "MovieMethod";

    private static final int REVIEW_LOADER_ID = 20;
    public static final String MOVIE_ID = "movie_id";

    private RecyclerView mReviesRV;
    private ReviewAdapter mReviewAdapter;

    public DetailsReviewsFragment() {
        // Required empty public constructor
    }

    public static DetailsReviewsFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(DetailsReviewsFragment.MOVIE_ID, id);
        DetailsReviewsFragment fragment = new DetailsReviewsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (L) Log.i(TAG, " <> DetailsReviewsFragment - onCreate");
        int movieId = getArguments().getInt(DetailsReviewsFragment.MOVIE_ID);
        loadReviewsData(movieId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (L) Log.i(TAG, " <> DetailsReviewsFragment - onCreateView");
        View v = inflater.inflate(R.layout.fragment_reviews, container, false);
        mReviesRV = v.findViewById(R.id.reviews_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mReviesRV.setLayoutManager(layoutManager);
        mReviesRV.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter(getActivity());
        mReviesRV.setAdapter(mReviewAdapter);

        return v;
    }

    private void loadReviewsData(int id) {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(MOVIE_ID, id);
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(REVIEW_LOADER_ID, queryBundle, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (L) Log.i(TAG, " <> DetailsReviewsFragment - onPause");
    }

    @NonNull
    @Override
    public Loader<List<Review>> onCreateLoader(int id, @Nullable Bundle loaderArgs) {
        return new AppReviewsLoader(getActivity(), loaderArgs);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> data) {
        if (null == data) {
            Toast.makeText(getActivity(),
                    R.string.error_message_all,
                    Toast.LENGTH_SHORT).show();
        } else {
            mReviewAdapter.setReviews(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Review>> loader) {

    }
}
