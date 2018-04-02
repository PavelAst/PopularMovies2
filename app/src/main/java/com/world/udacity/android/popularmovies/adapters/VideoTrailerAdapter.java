package com.world.udacity.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.world.udacity.android.popularmovies.R;
import com.world.udacity.android.popularmovies.model.VideoTrailer;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

import java.util.ArrayList;

public class VideoTrailerAdapter extends
        RecyclerView.Adapter<VideoTrailerAdapter.VideoTrailerViewHolder> {

    private final Context mContext;
    private ArrayList<VideoTrailer> mTrailers;

    private final VideoTrailerAdapterOnClickHandler mClickHandler;

    public interface VideoTrailerAdapterOnClickHandler {
        void onClick(String trailerKey);
    }

    public VideoTrailerAdapter(Context context, VideoTrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class VideoTrailerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final SimpleDraweeView mThumbnailYoutubeIV;
        public final TextView mTrailerNameTV;

        public VideoTrailerViewHolder(View trailerView) {
            super(trailerView);
            mThumbnailYoutubeIV = trailerView.findViewById(R.id.thumbnail_youtube_iv);
            mTrailerNameTV = trailerView.findViewById(R.id.trailer_name_tv);
            trailerView.setOnClickListener(this);
        }

        public void bindTo(VideoTrailer trailer) {
            String trailerThumbnailUrl = TheMoviedbConstants
                    .getYoutubeThumbnailUrl(trailer.getKey(), "mqdefault.jpg");
            mThumbnailYoutubeIV.setImageURI(trailerThumbnailUrl);
            mTrailerNameTV.setText(trailer.getName());
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String key = mTrailers.get(adapterPosition).getKey();
            if (mClickHandler != null) {
                mClickHandler.onClick(key);
            }
        }
    }

    @NonNull
    @Override
    public VideoTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutIdForRecyclerView = R.layout.youtube_trailer;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForRecyclerView, viewGroup, false);
        return new VideoTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoTrailerViewHolder videoTrailerViewHolder, int position) {
        VideoTrailer trailer = mTrailers.get(position);
        videoTrailerViewHolder.bindTo(trailer);
    }

    @Override
    public int getItemCount() {
        return (mTrailers == null) ? 0 : mTrailers.size();
    }

    public void setTrailers(ArrayList<VideoTrailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

}
