package com.world.udacity.android.popularmovies;

import java.io.Serializable;

public class MovieItem implements Serializable {

    private int mId;
    private String mTitle;
    private double mVoteAverage;
    private String mPosterPath;
    private String mBackdropPath;
    private String mOverview;
    private String mReleaseDate;
    private int mRuntime;

    public MovieItem() {
        mId = 0;
        mTitle = "No title";
        mVoteAverage = 0;
        mPosterPath = "";
        mBackdropPath = "";
        mOverview = "No overview";
        mReleaseDate = "1900";
        mRuntime = 0;
    }

    public MovieItem(int id, String title, String posterPath, double voteAverage,
                     String backdropPath, int runtime, String overview, String releaseDate) {
        mId = id;
        mTitle = title;
        mVoteAverage = voteAverage;
        mPosterPath = posterPath;
        mBackdropPath = backdropPath;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mRuntime = runtime;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public int getRuntime() {
        return mRuntime;
    }

    public void setRuntime(int runtime) {
        mRuntime = runtime;
    }

    public String getReleaseYear() {
        return mReleaseDate.substring(0, 4);
    }

    @Override
    public String toString() {
        return "Movie: " + mTitle;
    }

}
