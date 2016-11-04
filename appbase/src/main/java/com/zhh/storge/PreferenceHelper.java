package com.zhh.storge;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MHL on 2016/10/11.
 */
public class PreferenceHelper {

    private static final String FILE_FORMAT = "%s_pref";

    private static String getPublicFile() {
        return String.format(FILE_FORMAT, "public");
    }

    private static String getPersonalFile(long id) {
        return String.format(FILE_FORMAT, id);
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(getPublicFile(), Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static String getString(Context context, int id, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(getPersonalFile(id), Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }
}
