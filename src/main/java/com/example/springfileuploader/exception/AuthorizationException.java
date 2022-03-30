package com.example.springfileuploader.exception;

/**
 * Signals that authentication failed.
 */
public class AuthorizationException extends Exception {
    public AuthorizationException(String reason){
        super(reason);
    }
}
