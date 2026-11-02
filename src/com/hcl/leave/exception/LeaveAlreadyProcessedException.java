package com.hcl.leave.exception;

/**
 * Custom exception - thrown when someone tries to approve/reject a leave
 * request that is already APPROVED or REJECTED.
 */
public class LeaveAlreadyProcessedException extends Exception {
    private static final long serialVersionUID = 1L;

    public LeaveAlreadyProcessedException(String requestId, String currentStatus) {
        super("Leave request " + requestId + " is already " + currentStatus + ".");
    }
}
