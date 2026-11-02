package com.hcl.leave.exception;

/**
 * Custom exception - thrown when requested days are more than the
 * available leave balance (Case 2).
 */
public class InsufficientLeaveException extends Exception {
    private static final long serialVersionUID = 1L;

    public InsufficientLeaveException(int available, int requested) {
        super("Insufficient leave balance.\nAvailable Leave: " + available
                + "\nRequested Leave: " + requested);
    }
}
