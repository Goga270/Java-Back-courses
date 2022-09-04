package com.example.experienceexchange.exception;

public class NotAccessException extends RuntimeException {

    public NotAccessException() {
        super("No access to edit resource");
    }

    public NotAccessException(String message) {
        super(message);
    }
}
