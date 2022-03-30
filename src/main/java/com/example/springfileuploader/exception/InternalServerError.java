package com.example.springfileuploader.exception;

/**
 * Signals that an internal error happened which should not be specified to the client.
 */
public class InternalServerError extends Exception {
    public InternalServerError(){
        super("Oops, something went wrong!");
    }
}
