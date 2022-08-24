package com.example.experienceexchange.constant;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Arrays.asList(Permission.READ, Permission.EDIT_DIRECTION, Permission.EDIT_SECTION, Permission.REGISTRATION_ADMIN)),
    USER(Arrays.asList(Permission.EDIT_COURSE, Permission.EDIT_LESSON, Permission.READ));

    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<GrantedAuthority> getPermissions() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }

    public List<String> getNamePermissions() {
        return permissions.stream()
                .map(Permission::getPermission)
                .collect(Collectors.toList());
    }
}
