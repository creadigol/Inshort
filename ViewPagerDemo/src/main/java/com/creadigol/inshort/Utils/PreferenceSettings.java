package com.creadigol.inshort.Utils;

/**
 * Created by ravi on 26-10-2016.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceSettings {
    private String NEWSAPP = "NewsApp";
    private static final String KEY_NOTIFICATION_LAST_TIME = "last_sync_time";


    private String IsFirst = "true";
    private String Bookmark = "false";
    private String IsNeeded = "false";

    Context _context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    public PreferenceSettings(Context context) {
        this._context = context;
        sp = _context.getSharedPreferences(NEWSAPP, context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public boolean getBookmark() {
        return sp.getBoolean(Bookmark, false);
    }

    public void setBookmark(boolean flag) {
        editor.putBoolean(Bookmark, flag).commit();
    }

    public boolean getIsNeeded() {
        return sp.getBoolean(IsNeeded, true);
    }

    public void setIsNeeded(boolean flag) {
        editor.putBoolean(IsNeeded, flag).commit();
    }

    public boolean getIsFirst() {
        return sp.getBoolean(IsFirst, true);
    }

    public void setIsFirst(boolean flag) {
        editor.putBoolean(IsFirst, flag).commit();
    }

    public void setLastSyncTime(long id) {
        editor.putLong(KEY_NOTIFICATION_LAST_TIME, id).commit();
    }

    public long getLastSyncTime() {
        return sp.getLong(KEY_NOTIFICATION_LAST_TIME, 0);
    }

}