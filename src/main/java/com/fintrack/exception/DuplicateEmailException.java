package com.fintrack.exception;

/**
 * Exception for duplicate email scenarios.
 */

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
