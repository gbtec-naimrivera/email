package com.example.email.rest.exceptionhandler;

import com.example.email.service.exceptions.InvalidEmailStateException;
import com.example.email.service.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>Global exception handler for handling various exceptions across the entire application.</p>
 * <p>This class provides custom error responses for specific exceptions such as {@link ResourceNotFoundException}
 * and {@link InvalidEmailStateException}, as well as a generic handler for unexpected errors.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * <p>Handles {@link ResourceNotFoundException} and returns a 404 response with the error message.</p>
     *
     * @param ex The {@link ResourceNotFoundException} to handle.
     * @return A {@link ResponseEntity} with a 404 status and the exception's message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * <p>Handles {@link InvalidEmailStateException} and returns a 400 response with the error message.</p>
     *
     * @param ex The {@link InvalidEmailStateException} to handle.
     * @return A {@link ResponseEntity} with a 400 status and the exception's message.
     */
    @ExceptionHandler(InvalidEmailStateException.class)
    public ResponseEntity<String> handleInvalidEmailStateException(InvalidEmailStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * <p>Handles any other generic exceptions and returns a 500 response with a generic error message.</p>
     *
     * @param ex The {@link Exception} to handle.
     * @return A {@link ResponseEntity} with a 500 status and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
