package com.vinaekal.sisehat.model;

public class ApiRequest {
    private String email;
    private String password;

    public ApiRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
