package com.example.experienceexchange.exception;

public class PasswordsNotMatchException extends RuntimeException {

    public PasswordsNotMatchException() {
        super("Passwords do not match");
    }

    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
