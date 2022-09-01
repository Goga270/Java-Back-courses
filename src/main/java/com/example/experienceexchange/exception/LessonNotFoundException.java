package com.example.experienceexchange.exception;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(Long lessonId) {
        super(String.format("Lesson with id %d not found", lessonId));
    }

    public LessonNotFoundException(String message) {
        super(message);
    }
}
