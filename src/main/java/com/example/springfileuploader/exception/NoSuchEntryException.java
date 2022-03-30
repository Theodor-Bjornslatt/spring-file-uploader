package com.example.springfileuploader.exception;

/**
 * Signals that no entry matching the requirements exists.
 */
public class NoSuchEntryException extends Exception {
    public NoSuchEntryException(String message){
        super(message);
    }
}
