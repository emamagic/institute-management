package com.emamagic.institutemanagement.exception;

import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e) {
        Set<String> errors = new HashSet<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            var errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build()
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Set<String> errors = new HashSet<>();
        ex.getConstraintViolations().forEach(violation -> {
            String message = violation.getMessage();
            errors.add(message);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build()
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            UsernameNotFoundException.class,
            NoSuchElementException.class,
            CodeValidationException.class,
            RuntimeException.class,
            JwtException.class,
            EmailAlreadyExistException.class,
            CodeValidationException.class
    })
    public ResponseEntity<ExceptionResponse> handleCustomException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(ProfileCompletionException.class)
    public ResponseEntity<ExceptionResponse> handleProfileCompletionException(ProfileCompletionException e) {
        return ResponseEntity
                .status(HttpStatus.PRECONDITION_REQUIRED)
                .body(ExceptionResponse.builder()
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        String prodMessage = "Internal error, contact the admin";
        log.error("Handling exception: " + e.getClass().getName() + ", Message: " + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .error(prodMessage)
                        .build()
                );
    }
}
