package com.world.udacity.android.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.world.udacity.android.popularmovies.adapters.ReviewAdapter;
import com.world.udacity.android.popularmovies.model.MovieItem;
import com.world.udacity.android.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

public class DetailsReviewsFragment extends Fragment {

    // Turn logging on or off
    private static final boolean L = true;
    public static final String TAG = "MovieMethod";

    private ArrayList<Review> mReviews;

    private RecyclerView mReviesRV;
    private ReviewAdapter mReviewAdapter;

    public DetailsReviewsFragment() {
        // Required empty public constructor
    }

    public static DetailsReviewsFragment newInstance(MovieItem movieItem) {
        Bundle args = new Bundle();
        args.putParcelable(MovieDetailsActivity.MOVIE_ITEM, movieItem);
        DetailsReviewsFragment fragment = new DetailsReviewsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(MovieDetailsActivity.GET_REVIEWS_LIST_EVENT)) {
                Log.d(TAG, "Get intent message - reviews ************");
                mReviews = intent.getParcelableArrayListExtra(MovieDetailsActivity.REVIEWS);
                mReviewAdapter.setReviews(mReviews);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (L) Log.i(TAG, " <> DetailsReviewsFragment - onCreate");
        MovieItem movie = getArguments().getParcelable(MovieDetailsActivity.MOVIE_ITEM);
        mReviews = movie.getReviews();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MovieDetailsActivity.GET_REVIEWS_LIST_EVENT);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mUpdateUIReceiver, intentFilter);
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
        if (mReviews != null) {
            mReviewAdapter.setReviews(mReviews);
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (L) Log.i(TAG, " <> DetailsReviewsFragment - onPause");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mUpdateUIReceiver);
    }

}













