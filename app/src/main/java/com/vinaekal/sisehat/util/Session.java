package com.vinaekal.sisehat.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private static final String PREF_NAME = "sisehat_session";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERID = "user_id";
    private static final String KEY_LAST_PAGE = "last_page";
    private static final String KEY_CAN_USE_BIOMETRIC = "can_use_biometric";
    
    // Additional profile fields
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTH_DATE = "birth_date";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_JOB = "job";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public Session(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // ===== Biometric State =====
    public void setCanUseBiometric(boolean canUse) {
        editor.putBoolean(KEY_CAN_USE_BIOMETRIC, canUse);
        editor.apply();
    }

    public boolean canUseBiometric() {
        return pref.getBoolean(KEY_CAN_USE_BIOMETRIC, false);
    }

    // ===== Last Page Persistence =====
    public void saveLastPage(String activityName) {
        editor.putString(KEY_LAST_PAGE, activityName);
        editor.apply();
    }

    public String getLastPage() {
        return pref.getString(KEY_LAST_PAGE, null);
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
        return pref.getInt(KEY_USERID, -1);
    }

    // ===== Profile Fields =====
    public void saveProfile(String email, String phone, String birthDate, String address, String job) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_BIRTH_DATE, birthDate);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_JOB, job);
        editor.apply();
    }

    public String getEmail() { return pref.getString(KEY_EMAIL, ""); }
    public String getPhone() { return pref.getString(KEY_PHONE, ""); }
    public String getBirthDate() { return pref.getString(KEY_BIRTH_DATE, ""); }
    public String getAddress() { return pref.getString(KEY_ADDRESS, ""); }
    public String getJob() { return pref.getString(KEY_JOB, ""); }

    // ===== Logout (Explicit) =====
    public void logout() {
        editor.clear();
        editor.putBoolean(KEY_CAN_USE_BIOMETRIC, false); // Matikan biometrik saat logout manual
        editor.apply();
    }

    // ===== Kill Task (Clear session only) =====
    public void clearSessionOnly() {
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_LAST_PAGE);
        editor.apply();
        // can_use_biometric TETAP TRUE agar bisa login sidik jari setelah di-kill
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }
}