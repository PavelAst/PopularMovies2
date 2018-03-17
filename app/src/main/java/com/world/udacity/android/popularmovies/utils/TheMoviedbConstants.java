package com.world.udacity.android.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.world.udacity.android.popularmovies.BuildConfig;

public class TheMoviedbConstants {

    // Turn logging on or off
    private static final boolean L = true;

    private static final String TAG = "MovieConstants";

    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String API_KEY = BuildConfig.MDB_API_KEY;

    private static final String MOVIE_SEGMENT = "movie";
    private static final String POPULAR_SEGMENT = "popular";
    private static final String TOP_RATED_SEGMENT = "top_rated";
    private static final String API_KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";
    private static final String LANGUAGE_PARAM = "language";

    public static String getMoviesUrl(Most mostFilm, String lang, int page) {
        String mostSegment = (mostFilm == Most.POPULAR) ? POPULAR_SEGMENT : TOP_RATED_SEGMENT;
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(MOVIE_SEGMENT)
                .appendEncodedPath(mostSegment)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(PAGE_PARAM, Integer.toString(page))
                .appendQueryParameter(LANGUAGE_PARAM, lang)
                .build();
        if (L) Log.i(TAG, "getMoviesUrl - " + builtUri.toString());
        return builtUri.toString();
    }

    //    https://image.tmdb.org/t/p/w185//qpdF5bhIYFguucIcCKaXXo202ny.jpg
    public static String getPosterUrl(String size, String path) {
        Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendEncodedPath(size)
                .appendEncodedPath(path)
                .build();
        if (L) Log.i(TAG, "getPosterUrl - " + builtUri.toString());
        return builtUri.toString();
    }

}