package com.example.experienceexchange.security.filter;

import com.example.experienceexchange.exception.JwtTokenInvalidException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HandlerChainExceptionFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;

    public HandlerChainExceptionFilter(HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtTokenInvalidException exception) {
            resolver.resolveException(request, response, null, exception);
        }
    }
}
