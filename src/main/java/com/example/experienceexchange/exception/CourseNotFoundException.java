package com.example.experienceexchange.exception;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(Long id) {
        super(String.format("Course with id %d not found", id));
    }

    public CourseNotFoundException(String message) {
        super(message);
    }
}
