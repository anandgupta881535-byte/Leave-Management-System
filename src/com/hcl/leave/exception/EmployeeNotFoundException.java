package com.hcl.leave.exception;

/**
 * Custom exception - thrown when an employee ID is not found (Case 1).
 */
public class EmployeeNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public EmployeeNotFoundException(String employeeId) {
        super("Employee " + employeeId + " does not exist.");
    }
}
