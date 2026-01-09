package com.vinaekal.sisehat.model.response;

public class User {
    private int id;
    private String username;
    private String email;
    private boolean is_active;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return is_active;
    }
}
