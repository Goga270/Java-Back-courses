package com.example.experienceexchange.exception;

public class NotAccessException extends RuntimeException {

    public NotAccessException() {
        super();
    }

    public NotAccessException(String message) {
        super(message);
    }
}
