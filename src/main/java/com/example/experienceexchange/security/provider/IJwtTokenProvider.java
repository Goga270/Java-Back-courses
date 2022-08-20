package com.example.experienceexchange.security.provider;

import com.example.experienceexchange.constant.Role;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface IJwtTokenProvider {

    String createToken(String email, Role role);

    Authentication getAuthentication(String token);

    String getUsername(String token);

    Boolean validateToken(String token);

    String resolveToken(HttpServletRequest request);
}
