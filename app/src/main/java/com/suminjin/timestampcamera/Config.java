package com.suminjin.timestampcamera;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by parkjisun on 2017. 3. 9..
 */

public class Config {
    public static final String TAG = "jisunLog";
    public static final String TIME_STAMP_FORMAT = "yyyy.M.d a h:mm";

    public static final String SHARED_PREF_NAME = "setting_data";
    public static final String PREF_KEY_PICTURE_SIZE = "pref_key_picture_size";
    public static final String PREF_KEY_PICTURE_SIZE_LIST = "pref_key_picture_size_list";
    public static final String SPLIT_CHAR = "\\|";

    public static void putSharedPreference(Context context, String name, int value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static void putSharedPreference(Context context, String name, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static void putSharedPreference(Context context, String name, String value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static int getSharedPreferenceInt(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(name, -1);
    }

    public static boolean getSharedPreferenceBoolean(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(name, false);
    }

    public static String getSharedPreferenceString(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(name, "");
    }
}
