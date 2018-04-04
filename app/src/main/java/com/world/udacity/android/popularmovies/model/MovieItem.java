package com.world.udacity.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MovieItem implements Parcelable {

    private int mId;
    private String mTitle;
    private double mVoteAverage;
    private String mPosterPath;
    private String mBackdropPath;
    private String mOverview;
    private String mReleaseDate;

    private ArrayList<VideoTrailer> mTrailers;
    private ArrayList<Review> mReviews;

    public MovieItem() {
        mId = 0;
        mTitle = "No title";
        mVoteAverage = 0;
        mPosterPath = "";
        mBackdropPath = "";
        mOverview = "No overview";
        mReleaseDate = "1900";
    }

    public MovieItem(int id, String title, String posterPath, double voteAverage,
                     String backdropPath, String overview, String releaseDate,
                     ArrayList<VideoTrailer> trailers, ArrayList<Review> reviews) {
        mId = id;
        mTitle = title;
        mVoteAverage = voteAverage;
        mPosterPath = posterPath;
        mBackdropPath = backdropPath;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mTrailers = trailers;
        mReviews = reviews;
    }

    protected MovieItem(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mVoteAverage = in.readDouble();
        mPosterPath = in.readString();
        mBackdropPath = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        in.readList(new ArrayList<VideoTrailer>(), null);
        in.readList(new ArrayList<Review>(), null);
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mId);
        out.writeString(mTitle);
        out.writeDouble(mVoteAverage);
        out.writeString(mPosterPath);
        out.writeString(mBackdropPath);
        out.writeString(mOverview);
        out.writeString(mReleaseDate);
        out.writeList(mTrailers);
        out.writeList(mReviews);
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

    public ArrayList<VideoTrailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(ArrayList<VideoTrailer> trailers) {
        mTrailers = trailers;
    }

    public ArrayList<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        mReviews = reviews;
    }

    public String getReleaseYear() {
        return mReleaseDate.substring(0, 4);
    }

    @Override
    public String toString() {
        return "Movie: " + mTitle;
    }

}
