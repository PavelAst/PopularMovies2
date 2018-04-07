package com.world.udacity.android.popularmovies.data;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.world.udacity.android.popularmovies.model.MovieItem;

import java.util.ArrayList;
import java.util.List;

public class MovieDBUtils {

    public static List<MovieItem> getMoviesFromCursor(Cursor cursor) {
        List<MovieItem> movies = new ArrayList<>();
        if (cursor.getCount() == 0) {
            return null;
        }
        if (cursor.moveToFirst()) {
            do {
                MovieItem movie = new MovieItem();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));

                movies.add(movie);

            } while (cursor.moveToNext());
        }

        return movies;
    }

}
