package com.example.experienceexchange.constant;

public enum Permission {
    EDIT_DIRECTION("edit-direction"),
    EDIT_SECTION("edit-section"),
    EDIT_COURSE("edit-course"),
    EDIT_LESSON("edit-lesson"),
    READ("read"),
    REGISTRATION_ADMIN("registration-admin"),
    BLOCK_USER("block-user");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
