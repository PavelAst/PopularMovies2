package com.world.udacity.android.popularmovies;

import android.util.Log;

import com.world.udacity.android.popularmovies.model.MovieItem;
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


    public List<MovieItem> fetchItems(Most mostFilm, int page) {

        List<MovieItem> items = new ArrayList<>();

        try {
            String url = TheMoviedbConstants.getMoviesUrl(mostFilm, "en", page);
            String jsonString = getUrlString(url);
            if (L) Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
            return items;
        } catch (IOException ioe) {
            if (L) Log.e(TAG, "Failed to fetch items", ioe);
            return null;
        } catch (JSONException je) {
            if (L) Log.e(TAG, "Failed to parse JSON", je);
            return null;
        }
    }

    private void parseItems(List<MovieItem> items, JSONObject jsonBody)
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

}
