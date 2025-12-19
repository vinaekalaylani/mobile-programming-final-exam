package com.vinaekal.sisehat.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private static final String PREF_NAME = "sisehat_session";
    private static final String KEY_TOKEN = "jwt_token";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public Session(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    public void clear() {
        editor.clear().apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }
}
