package com.example.experienceexchange.controller;

import com.example.experienceexchange.exception.*;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AdvisorController extends ResponseEntityExceptionHandler {


    // TODO: НАДО ЛИ ИХ ЛОВИТЬ ?
    @ExceptionHandler(value = {JwtTokenInvalidException.class})
    protected ResponseEntity<Object> handleInvalidToken(RuntimeException exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, AuthenticationException.class})
    protected ResponseEntity<Object> handleAuthentication(Exception exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @ExceptionHandler(value = {UserNotFoundException.class,
            PasswordsNotMatchException.class,
            SectionNotFoundException.class,
            DirectionNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(Exception exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @ExceptionHandler(value = {EmailNotUniqueException.class})
    protected ResponseEntity<Object> handleEmailNotUnique(Exception exception, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        body.put("solution", "Write another email");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return getObjectResponseEntity(exception, request, body, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        body.put("timestamp", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        body.put("error", httpStatus.name());
        body.put("status", String.valueOf(httpStatus.value()));
        // TODO : НЕ РАБОТАЕТ ?
        exception.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            body.put(fieldName, message);
        });
        return handleExceptionInternal(exception, body, new HttpHeaders(), httpStatus, request);
    }

    private ResponseEntity<Object> getObjectResponseEntity(Exception exception,
                                                           WebRequest request, Map<String, String> body, HttpStatus httpStatus) {
        body.put("timestamp", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        body.put("error", httpStatus.name());
        body.put("status", String.valueOf(httpStatus.value()));
        body.put("message", exception.getMessage());
        return handleExceptionInternal(exception, body, new HttpHeaders(), httpStatus, request);
    }
}
