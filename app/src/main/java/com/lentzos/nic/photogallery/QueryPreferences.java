package com.lentzos.nic.photogallery;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Nic on 29/11/2016.
 */

public class QueryPreferences {
    //Class to manage the writing of search strings to shared preferences, so that the last search query
    //is remembered by the app, even across device restarts.
    private static final String PREF_SEARCH_QUERY = "searchQuery";

    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_SEARCH_QUERY,query).apply();
    }
}
