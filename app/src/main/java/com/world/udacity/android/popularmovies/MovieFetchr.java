package com.world.udacity.android.popularmovies;

import android.util.Log;

import com.world.udacity.android.popularmovies.model.MovieItem;
import com.world.udacity.android.popularmovies.model.Review;
import com.world.udacity.android.popularmovies.model.VideoTrailer;
import com.world.udacity.android.popularmovies.utils.Most;
import com.world.udacity.android.popularmovies.utils.TheMoviedbConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class MovieFetchr {

    // Turn logging on or off
    private static final boolean L = false;
    private static final String TAG = "MovieFetchr";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


    public List<MovieItem> fetchMovieItems(Most mostFilm, int page) {

        List<MovieItem> items = new ArrayList<>();

        try {
            String url = TheMoviedbConstants.getMoviesUrl(mostFilm, "en-US", page);
            String jsonString = getUrlString(url);
            if (L) Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseMovieItems(items, jsonBody);
            return items;
        } catch (IOException ioe) {
            if (L) Log.e(TAG, "Failed to fetch items", ioe);
            return null;
        } catch (JSONException je) {
            if (L) Log.e(TAG, "Failed to parse JSON", je);
            return null;
        }
    }

    private void parseMovieItems(List<MovieItem> items, JSONObject jsonBody)
            throws IOException, JSONException {
        JSONArray resultsJsonArray = jsonBody.getJSONArray("results");

        for (int i = 0; i < resultsJsonArray.length(); i++) {
            JSONObject movieJsonObject = resultsJsonArray.getJSONObject(i);

            MovieItem item = new MovieItem();
            item.setId(movieJsonObject.getInt("id"));
            item.setTitle(movieJsonObject.getString("title"));
            item.setVoteAverage(movieJsonObject.getDouble("vote_average"));
            item.setPosterPath(movieJsonObject.getString("poster_path"));
            item.setOverview(movieJsonObject.getString("overview"));
            item.setReleaseDate(movieJsonObject.getString("release_date"));
            item.setBackdropPath(movieJsonObject.getString("backdrop_path"));

            items.add(item);
        }
    }

    public List<VideoTrailer> fetchMovieTrailers(int movieId) {

        List<VideoTrailer> trailers = new ArrayList<>();

        try {
            String url = TheMoviedbConstants.getTrailersUrl(movieId, "en-US");
            String jsonString = getUrlString(url);
            if (L) Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseMovieTrailers(trailers, jsonBody);
            return trailers;
        } catch (IOException ioe) {
            if (L) Log.e(TAG, "Failed to fetch trailers data", ioe);
            return null;
        } catch (JSONException je) {
            if (L) Log.e(TAG, "Failed to parse JSON", je);
            return null;
        }
    }

    private void parseMovieTrailers(List<VideoTrailer> trailers, JSONObject jsonBody)
            throws IOException, JSONException {
        JSONArray resultsJsonArray = jsonBody.getJSONArray("results");

        for (int i = 0; i < resultsJsonArray.length(); i++) {
            JSONObject trailerJsonObject = resultsJsonArray.getJSONObject(i);
            String trailerName = trailerJsonObject.getString("name");
            String trailerKey = trailerJsonObject.getString("key");
            String trailerSite = trailerJsonObject.getString("site");

            if (trailerSite.toLowerCase().equals("youtube")) {
                VideoTrailer videoTrailer = new VideoTrailer(trailerName, trailerKey);
                trailers.add(videoTrailer);
            }
        }
    }

    public List<Review> fetchMovieReviews(int movieId) {

        List<Review> reviews = new ArrayList<>();

        try {
            String url = TheMoviedbConstants.getReviewsUrl(movieId, "en-US", 1);
            String jsonString = getUrlString(url);
            if (L) Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseMovieReviews(reviews, jsonBody);
            return reviews;
        } catch (IOException ioe) {
            if (L) Log.e(TAG, "Failed to fetch reviews data", ioe);
            return null;
        } catch (JSONException je) {
            if (L) Log.e(TAG, "Failed to parse JSON", je);
            return null;
        }
    }

    private void parseMovieReviews(List<Review> reviews, JSONObject jsonBody)
            throws IOException, JSONException {
        JSONArray resultsJsonArray = jsonBody.getJSONArray("results");

        for (int i = 0; i < resultsJsonArray.length(); i++) {
            JSONObject reviewJsonObject = resultsJsonArray.getJSONObject(i);
            String reviewAuthor = reviewJsonObject.getString("author");
            String reviewContent = reviewJsonObject.getString("content");

            Review review = new Review(reviewAuthor, reviewContent);
            reviews.add(review);
        }
    }

}
