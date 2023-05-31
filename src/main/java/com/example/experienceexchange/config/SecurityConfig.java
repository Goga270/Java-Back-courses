package com.example.experienceexchange.config;

import com.example.experienceexchange.constant.Permission;
import com.example.experienceexchange.security.configure.JwtConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    private static final int STRENGTH_PASSWORD_ENCODER = 8;

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui/",
            "/swagger-ui"
    };

    private final JwtConfigurer jwtConfigurer;

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .permitAll();
//        http
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/auth/login").permitAll()
//                .antMatchers("/auth/registration").permitAll()
//                .antMatchers("/courses").permitAll()
//                .antMatchers("/lessons").permitAll()
//                .antMatchers("/lessons/{id}").permitAll()
//                .antMatchers("/courses/{id}").permitAll()
//                .antMatchers("/courses/{id}/comments").permitAll()
//                .antMatchers("/lessons/{id}/comments").permitAll()
//                .antMatchers("/auth/registration-admin").hasAuthority(Permission.REGISTRATION_ADMIN.getPermission())
//                .antMatchers("/auth/block-user").hasAuthority(Permission.BLOCK_USER.getPermission())
//                .antMatchers(HttpMethod.GET, "/directions").hasAuthority(Permission.READ.getPermission())
//                .antMatchers(HttpMethod.GET, "/directions/{id}").hasAuthority(Permission.READ.getPermission())
//                .antMatchers("/directions/new-direction").hasAuthority(Permission.EDIT_DIRECTION.getPermission())
//                .antMatchers("/directions/{id}/settings").hasAuthority(Permission.EDIT_DIRECTION.getPermission())
//                .antMatchers(HttpMethod.DELETE, "/directions/{id}").hasAuthority(Permission.EDIT_DIRECTION.getPermission())
//                .antMatchers("/sections/**").hasAuthority(Permission.EDIT_SECTION.getPermission())
//                .antMatchers(AUTH_WHITELIST).permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .apply(jwtConfigurer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH_PASSWORD_ENCODER);
    }
}
