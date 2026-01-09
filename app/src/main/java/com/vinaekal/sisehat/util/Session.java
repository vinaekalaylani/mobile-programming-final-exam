package com.vinaekal.sisehat.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private static final String PREF_NAME = "sisehat_session";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERID = "user_id";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public Session(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // ===== Token =====
    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    // ===== Username =====
    public void saveUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "User");
    }

    // User ID
    public void saveUserId(int userId) {
        editor.putInt(KEY_USERID, userId);
        editor.apply();
    }

    public int getUserId() {
        return pref.getInt(KEY_USERID, -1); // -1 jika belum ada
    }

    // ===== Clear / Logout =====
    public void clear() {
        editor.clear().apply();
    }

    // ===== Check login =====
    public boolean isLoggedIn() {
        return getToken() != null;
    }
}
