package com.example.experienceexchange.constant;

public enum Permission {
    READ("read"),
    WRITE("write"),
    DELETE("delete"),
    REGISTRATION_ADMIN("registration-admin");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
