package com.example.experienceexchange.exception;

public class EmailNotUniqueException extends RuntimeException {

    public EmailNotUniqueException() {
        super("Email is not unique.");
    }
}
