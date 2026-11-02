package com.hcl.leave.exception;

/**
 * Custom exception - thrown when a leave request ID is not found.
 */
public class LeaveRequestNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public LeaveRequestNotFoundException(String requestId) {
        super("Leave request " + requestId + " does not exist.");
    }
}
