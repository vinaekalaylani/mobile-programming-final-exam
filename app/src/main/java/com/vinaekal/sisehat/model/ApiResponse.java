package com.vinaekal.sisehat.model;

public class ApiResponse<T> {
    private String code;
    private String message;
    private T content;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getContent() {
        return content;
    }
}
