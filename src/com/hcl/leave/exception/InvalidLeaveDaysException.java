package com.hcl.leave.exception;

/**
 * Custom exception - thrown when leave days are zero or negative (Case 3).
 */
public class InvalidLeaveDaysException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidLeaveDaysException() {
        super("Leave days must be greater than 0.");
    }
}
