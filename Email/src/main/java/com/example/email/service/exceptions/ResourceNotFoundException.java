package com.example.email.service.exceptions;

/**
 * <p>Custom exception thrown when a requested resource is not found.</p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * <p>Constructor that creates a new {@link ResourceNotFoundException} with the specified error message.</p>
     *
     * @param message The error message describing the reason for the exception.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
