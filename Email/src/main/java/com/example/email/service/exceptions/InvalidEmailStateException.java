package com.example.email.service.exceptions;

/**
 * <p>Custom exception thrown when an email's state is invalid for a specific operation.</p>
 */
public class InvalidEmailStateException extends RuntimeException {

    /**
     * <p>Constructor that creates a new {@link InvalidEmailStateException} with the specified error message.</p>
     *
     * @param message The error message describing the reason for the exception.
     */
    public InvalidEmailStateException(String message) {
        super(message);
    }
}
