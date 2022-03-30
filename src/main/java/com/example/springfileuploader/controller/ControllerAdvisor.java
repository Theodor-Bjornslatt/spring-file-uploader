package com.example.springfileuploader.controller;

import com.example.springfileuploader.exception.AuthorizationException;
import com.example.springfileuploader.exception.DuplicateEntryException;
import com.example.springfileuploader.exception.InternalServerError;
import com.example.springfileuploader.exception.NoSuchEntryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * Global Exception handler for the controllers of the application.
 * Logs exceptions that have occurred and sends appropriate responses to the client in a standardized format.
 */
@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

    @ExceptionHandler(NoSuchEntryException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchIdException(NoSuchEntryException e){
        log.warn(e.getMessage()+ " --- Responded with status 404 ---");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntryException(DuplicateEntryException e){
        log.warn(e.getMessage()+ " --- Responded with status 409 ---");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthorizationException e){
        log.warn(e.getMessage()+ " --- Responded with status 401 ---");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<Map<String, String>> handleInternalServerErrorException(InternalServerError e){
        log.error(e.getMessage() + " --- Responded with status 500 ---");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
    }
}
