package com.world.udacity.android.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.world.udacity.android.popularmovies.BuildConfig;

public class TheMoviedbConstants {

    // Turn logging on or off
    private static final boolean L = true;

    private static final String TAG = "MovieConstants";

    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String MOVIE_POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String YOUTUBE_POSTER_BASE_URL = "https://img.youtube.com/vi/";
    private static final String YOUTUBE_TRAILER_BASE_URL = "https://www.youtube.com/";

    private static final String API_KEY = BuildConfig.MDB_API_KEY;

    private static final String MOVIE_SEGMENT = "movie";
    private static final String POPULAR_SEGMENT = "popular";
    private static final String TOP_RATED_SEGMENT = "top_rated";
    private static final String VIDEOS_SEGMENT = "videos";
    private static final String REVIEWS_SEGMENT = "reviews";
    private static final String API_KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";
    private static final String LANGUAGE_PARAM = "language";
    private static final String YOUTUBE_WATCH_SEGMENT = "watch";
    private static final String YOUTUBE_V_PARAM = "v";

    //  https://api.themoviedb.org/3/movie/popular?api_key=API_KEY&page=1&language=en
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

    //  https://image.tmdb.org/t/p/w185//qpdF5bhIYFguucIcCKaXXo202ny.jpg
    public static String getMoviePosterUrl(String size, String path) {
        Uri builtUri = Uri.parse(MOVIE_POSTER_BASE_URL).buildUpon()
                .appendEncodedPath(size)
                .appendEncodedPath(path)
                .build();
//        if (L) Log.i(TAG, "getMoviePosterUrl - " + builtUri.toString());
        return builtUri.toString();
    }

    //  https://img.youtube.com/vi/6qTghUgMOeY/mqdefault.jpg
    public static String getYoutubeThumbnailUrl(String trailerKey, String qualityVersion) {
        Uri builtUri = Uri.parse(YOUTUBE_POSTER_BASE_URL).buildUpon()
                .appendEncodedPath(trailerKey)
                .appendEncodedPath(qualityVersion)
                .build();
        if (L) Log.i(TAG, "getYoutubeThumbnailUrl - " + builtUri.toString());
        return builtUri.toString();
    }

    //  https://api.themoviedb.org/3/movie/181808?api_key=API_KEY
    public static String getMovieInfoUrl(int id) {
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(MOVIE_SEGMENT)
                .appendEncodedPath(Integer.toString(id))
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        if (L) Log.i(TAG, "getMovieInfoUrl - " + builtUri.toString());
        return builtUri.toString();
    }

    //  https://api.themoviedb.org/3/movie/181808/videos?api_key=API_KEY&language=en-US
    public static String getTrailersUrl(int id, String lang) {
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(MOVIE_SEGMENT)
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(VIDEOS_SEGMENT)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, lang)
                .build();
        if (L) Log.i(TAG, "getTrailersUrl - " + builtUri.toString());
        return builtUri.toString();
    }

    //  https://www.youtube.com/watch?v=ue80QwXMRHg
    public static String getYoutubeTrailerUrl(String trailerKey) {
        Uri builtUri = Uri.parse(YOUTUBE_TRAILER_BASE_URL).buildUpon()
                .appendEncodedPath(YOUTUBE_WATCH_SEGMENT)
                .appendQueryParameter(YOUTUBE_V_PARAM, trailerKey)
                .build();
        if (L) Log.i(TAG, "getTrailersUrl - " + builtUri.toString());
        return builtUri.toString();
    }

    //  https://api.themoviedb.org/3/movie/181808/reviews?api_key=API_KEY&page=1&language=en-US
    public static String getReviewsUrl(int id, String lang, int page) {
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(MOVIE_SEGMENT)
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(REVIEWS_SEGMENT)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(PAGE_PARAM, Integer.toString(page))
                .appendQueryParameter(LANGUAGE_PARAM, lang)
                .build();
        if (L) Log.i(TAG, "getReviewsUrl - " + builtUri.toString());
        return builtUri.toString();
    }

}