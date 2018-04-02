package com.world.udacity.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoTrailer implements Parcelable {

    private String mName;
    private String mKey;

    public VideoTrailer(String name, String key) {
        mName = name;
        mKey = key;
    }

    protected VideoTrailer(Parcel in) {
        mName = in.readString();
        mKey = in.readString();
    }

    public static final Creator<VideoTrailer> CREATOR = new Creator<VideoTrailer>() {
        @Override
        public VideoTrailer createFromParcel(Parcel in) {
            return new VideoTrailer(in);
        }

        @Override
        public VideoTrailer[] newArray(int size) {
            return new VideoTrailer[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mKey);
    }
}