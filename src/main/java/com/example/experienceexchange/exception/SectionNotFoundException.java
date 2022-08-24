package com.example.experienceexchange.exception;

public class SectionNotFoundException extends RuntimeException {

    public SectionNotFoundException(Long id) {
        super(String.format("Section with id %d not found", id));
    }

    public SectionNotFoundException(String message) {
        super(message);
    }
}
