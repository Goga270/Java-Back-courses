package com.example.experienceexchange.exception;

public class DirectionNotFoundException extends RuntimeException {
    public DirectionNotFoundException(Long id) {
        super(String.format("Direction with id %d not found", id));
    }
}
