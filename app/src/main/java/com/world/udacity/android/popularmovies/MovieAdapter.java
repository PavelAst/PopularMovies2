package com.world.udacity.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieItem> mMovieItems;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieItem movie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mPosterImageView = itemView.findViewById(R.id.poster_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindTo(MovieItem item) {
            String posterUrl = TheMoviedbConstants.getPosterUrl("w185", item.getPosterPath());
            Picasso.with(itemView.getContext())
                    .load(posterUrl)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(mPosterImageView);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieItem movie = mMovieItems.get(adapterPosition);
            if (mClickHandler != null) {
                mClickHandler.onClick(movie);
            }
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_poster,
                viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int position) {
        MovieItem movieItem = mMovieItems.get(position);
        movieViewHolder.bindTo(movieItem);
    }

    @Override
    public int getItemCount() {
        return (mMovieItems == null) ? 0 : mMovieItems.size();
    }

    public void setMovieItems(List<MovieItem> movieItems) {
        if (mMovieItems == null || mMovieItems.isEmpty() || movieItems == null) {
            mMovieItems = movieItems;
        } else {
            mMovieItems.addAll(movieItems);
        }
        notifyDataSetChanged();
    }
}
