package com.example.experienceexchange.exception;

public class JwtTokenNotEnteredException extends RuntimeException {

    public JwtTokenNotEnteredException() {
        super("User authentication token not entered");
    }
}
