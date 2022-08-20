package com.example.experienceexchange.exception;

public class JwtTokenInvalidException extends RuntimeException {

    public JwtTokenInvalidException(String message) {
        super(message);
    }
}
