package com.vinaekal.sisehat.model.content;

import com.vinaekal.sisehat.model.response.User;

public class LoginContent {
    private User user;
    private String token;

    public User getUser() { return user; }
    public String getToken() { return token; }
}
