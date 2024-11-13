package com.example.email.service.exceptions;

public class InvalidEmailStateException extends RuntimeException {
    public InvalidEmailStateException(String message) {
        super(message);
    }
}