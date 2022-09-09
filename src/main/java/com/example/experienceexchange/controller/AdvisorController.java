package com.example.experienceexchange.controller;

import com.example.experienceexchange.exception.*;
import com.example.experienceexchange.util.date.DateUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AdvisorController extends ResponseEntityExceptionHandler {

    private static final String DATE_PATTERN = "dd-MM-yyyy HH:mm:ss X";
    private static final String SOLUTION_EMAIL_NOT_UNIQUE = "Write another email";
    private static final String SOLUTION_PASSWORD_NOT_MATCH = "Try entering passwords again";
    private static final String SOLUTION_NUMBER_FORMAT = "Check your search filters";

    @ExceptionHandler({NumberFormatException.class})
    protected ResponseEntity<Object> handleNumberFormat(NumberFormatException exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        body.put("solution", SOLUTION_NUMBER_FORMAT);
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    // TODO: НАДО ЛИ ИХ ЛОВИТЬ ?
    @ExceptionHandler({JwtTokenInvalidException.class})
    protected ResponseEntity<Object> handleInvalidToken(RuntimeException exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @ExceptionHandler({UsernameNotFoundException.class,
            AuthenticationException.class,
            NotAccessException.class})
    protected ResponseEntity<Object> handleAuthentication(Exception exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @ExceptionHandler({UserNotFoundException.class,
            SectionNotFoundException.class,
            DirectionNotFoundException.class,
            LessonNotFoundException.class,
            CourseNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(Exception exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @ExceptionHandler(value = {EmailNotUniqueException.class})
    protected ResponseEntity<Object> handleEmailNotUnique(Exception exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        body.put("solution", SOLUTION_EMAIL_NOT_UNIQUE);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @ExceptionHandler(value = {PasswordsNotMatchException.class})
    protected ResponseEntity<Object> handlePasswordsNotMatch(Exception exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        body.put("solution", SOLUTION_PASSWORD_NOT_MATCH);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @ExceptionHandler(value = {SubscriptionNotPossibleException.class})
    protected ResponseEntity<Object> handleSubscriptionNotPossible(Exception exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        exception.getConstraintViolations().forEach(w -> {
            String propertyPath = w.getPropertyPath().toString();
            String message = w.getMessage();
            body.put(propertyPath, message);
        });
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        body.put("timestamp", new SimpleDateFormat(DATE_PATTERN).format(DateUtil.dateTimeNow()));
        body.put("error", httpStatus.name());
        body.put("status", String.valueOf(httpStatus.value()));
        exception.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            body.put(fieldName, message);
        });
        return handleExceptionInternal(exception, body, new HttpHeaders(), httpStatus, request);
    }

    private ResponseEntity<Object> getObjectResponseEntity(Exception exception,
                                                           WebRequest request, Map<String, String> body, HttpStatus httpStatus) {
        body.put("timestamp", new SimpleDateFormat(DATE_PATTERN).format(DateUtil.dateTimeNow()));
        body.put("error", httpStatus.name());
        body.put("status", String.valueOf(httpStatus.value()));
        body.put("message", exception.getMessage());
        return handleExceptionInternal(exception, body, new HttpHeaders(), httpStatus, request);
    }
}
