package com.example.experienceexchange.security.configure;

import com.example.experienceexchange.security.filter.HandlerChainExceptionFilter;
import com.example.experienceexchange.security.filter.JwtTokenFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenFilter tokenFilter;
    private final HandlerChainExceptionFilter chainExceptionFilter;

    public JwtConfigurer(JwtTokenFilter tokenFilter, HandlerChainExceptionFilter chainExceptionFilter) {
        this.tokenFilter = tokenFilter;
        this.chainExceptionFilter = chainExceptionFilter;
    }

    @Override
    public void configure(HttpSecurity builder) {
        builder.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(chainExceptionFilter, JwtTokenFilter.class);
        log.info("Add filter {} before filter {} for security",
                tokenFilter.getClass().getName(),
                UsernamePasswordAuthenticationFilter.class.getName());
        log.info("Add filter {} before filter {} for security",
                chainExceptionFilter.getClass().getName(),
                JwtTokenFilter.class.getName());
    }
}
