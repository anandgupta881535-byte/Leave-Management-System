package com.hcl.leave.exception;

/**
 * Custom exception - thrown when an employee ID or email already exists (Case 4).
 */
public class DuplicateEmployeeException extends Exception {
    private static final long serialVersionUID = 1L;

    public DuplicateEmployeeException(String message) {
        super(message);
    }
}
