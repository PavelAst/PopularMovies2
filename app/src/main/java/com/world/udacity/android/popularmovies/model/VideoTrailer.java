package com.world.udacity.android.popularmovies.model;

public class VideoTrailer {

    private String mName;
    private String mKey;

    public VideoTrailer(String name, String key) {
        mName = name;
        mKey = key;
    }

    @Override
    public String toString() {
        return mName + " on Youtube";
    }
}