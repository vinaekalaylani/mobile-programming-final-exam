package com.vinaekal.sisehat;

public class ProfileItem {

    public enum Type {
        EMAIL,
        PHONE,
        BIRTHDATE,
        ADDRESS,
        JOB
    }

    private final Type type;
    private final String label;
    private final String value;

    public ProfileItem(Type type, String label, String value) {
        this.type = type;
        this.label = label;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
