package com.world.udacity.android.popularmovies.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class SortPreferences {

    private static final String PREF_SORT_CRITERIA = "searchQuery";

    public static Most getSortCriteria(Context context) {
        String criteria = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SORT_CRITERIA, "POPULAR");
        return Most.valueOf(criteria);
    }

    public static void setSortCriteria(Context context, Most criteria) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SORT_CRITERIA, criteria.toString())
                .apply();
    }
}
