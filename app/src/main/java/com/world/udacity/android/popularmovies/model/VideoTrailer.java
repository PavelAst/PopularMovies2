package com.world.udacity.android.popularmovies.model;

public class VideoTrailer {

    private String mName;
    private String mKey;

    public VideoTrailer(String name, String key) {
        mName = name;
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    @Override
    public String toString() {
        return mName + " on Youtube";
    }
}