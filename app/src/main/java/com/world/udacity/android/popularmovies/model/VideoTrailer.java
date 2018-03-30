package com.world.udacity.android.popularmovies.model;

public class VideoTrailer {

    private String mName;
    private String mKey;
    private String mSite;

    public VideoTrailer(String name, String key, String site) {
        mName = name;
        mKey = key;
        mSite = site;
    }

    @Override
    public String toString() {
        return mName + " on " + mSite;
    }
}