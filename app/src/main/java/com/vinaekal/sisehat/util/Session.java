package com.vinaekal.sisehat.util;

import android.content.Context;

public class Session {

    // In-memory session storage (RAM)
    // Data will be lost when the app is killed/swiped up.
    private static String sessionToken = null;
    private static String sessionUsername = "User";
    private static int sessionUserId = 0;
    private static String sessionEmail = "";
    private static String sessionPhone = "";
    private static String sessionBirthDate = "";
    private static String sessionAddress = "";
    private static String sessionJob = "";

    public Session(Context context) { }

    public void saveToken(String token) { sessionToken = token; }
    public String getToken() { return sessionToken; }

    public void saveUsername(String username) { sessionUsername = username; }
    public String getUsername() { return sessionUsername; }

    public void saveUserId(int id) { sessionUserId = id; }
    public int getUserId() { return sessionUserId; }

    public String getEmail() { return sessionEmail; }
    public String getPhone() { return sessionPhone; }
    public String getBirthDate() { return sessionBirthDate; }
    public String getAddress() { return sessionAddress; }
    public String getJob() { return sessionJob; }

    public void saveProfile(String email, String phone, String birthDate, String address, String job) {
        sessionEmail = email;
        sessionPhone = phone;
        sessionBirthDate = birthDate;
        sessionAddress = address;
        sessionJob = job;
    }

    public void clear() {
        sessionToken = null;
        sessionUsername = "";
        sessionUserId = 0;
        sessionEmail = "";
        sessionPhone = "";
        sessionBirthDate = "";
        sessionAddress = "";
        sessionJob = "";
    }

    public boolean isLoggedIn() {
        return sessionToken != null;
    }
}
