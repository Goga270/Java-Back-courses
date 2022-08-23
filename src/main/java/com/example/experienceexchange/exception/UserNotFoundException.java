package com.example.experienceexchange.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format("User with id %d not found", id));
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
