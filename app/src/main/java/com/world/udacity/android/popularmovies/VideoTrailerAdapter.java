package com.world.udacity.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.world.udacity.android.popularmovies.model.VideoTrailer;

import java.util.List;

public class VideoTrailerAdapter extends
        RecyclerView.Adapter<VideoTrailerAdapter.VideoTrailerViewHolder> {

    private List<VideoTrailer> mVideoTrailers;

    public VideoTrailerAdapter() {
    }

    @NonNull
    @Override
    public VideoTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForRecyclerView = R.layout.youtube_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForRecyclerView, viewGroup, false);
        return new VideoTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoTrailerViewHolder holder, int position) {
//        String url = "https://img.youtube.com/vi/6qTghUgMOeY/0.jpg";
        String url = "https://img.youtube.com/vi/6qTghUgMOeY/mqdefault.jpg";
        holder.mPosterYoutubeIV.setImageURI(url);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class VideoTrailerViewHolder extends RecyclerView.ViewHolder {

        public final SimpleDraweeView mPosterYoutubeIV;

        public VideoTrailerViewHolder(View view) {
            super(view);
            mPosterYoutubeIV = (SimpleDraweeView) view.findViewById(R.id.poster_youtube_iv);
        }

    }

}
