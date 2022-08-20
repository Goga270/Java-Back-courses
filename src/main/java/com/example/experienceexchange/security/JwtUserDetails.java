package com.example.experienceexchange.security;

import com.example.experienceexchange.constant.Role;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Setter
public class JwtUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private Boolean validated;
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getPermissions();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return validated;
    }

    @Override
    public boolean isAccountNonLocked() {
        return validated;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return validated;
    }

    @Override
    public boolean isEnabled() {
        return validated;
    }

    public Long getId() {
        return id;
    }

    public Boolean getValidated() {
        return validated;
    }

    public Role getRole() {
        return role;
    }
}
