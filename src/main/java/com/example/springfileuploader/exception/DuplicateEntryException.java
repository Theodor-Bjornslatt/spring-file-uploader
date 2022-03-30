package com.example.springfileuploader.exception;

/**
 * Signals that an entry with the same value already exists in the collection.
 */
public class DuplicateEntryException extends Exception {
    public DuplicateEntryException(String reason){
        super(reason);
    }
}
