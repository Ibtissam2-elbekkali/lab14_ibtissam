package com.example.lab14_ibtissam.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public final class AppPrefs {
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_LANG = "user_lang";
    private static final String KEY_THEME = "user_theme";

    public static class Triple {
        public final String name;
        public final String lang;
        public final String theme;

        public Triple(String name, String lang, String theme) {
            this.name = name;
            this.lang = lang;
            this.theme = theme;
        }
    }

    public static boolean save(Context context, String name, String lang, String theme, boolean commit) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_LANG, lang);
        editor.putString(KEY_THEME, theme);
        if (commit) {
            return editor.commit();
        } else {
            editor.apply();
            return true;
        }
    }

    public static Triple load(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return new Triple(
                prefs.getString(KEY_NAME, "Ibtissam"),
                prefs.getString(KEY_LANG, "fr"),
                prefs.getString(KEY_THEME, "light")
        );
    }

    public static void clear(Context context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }
}