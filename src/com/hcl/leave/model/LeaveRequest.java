package com.hcl.leave.model;

/**
 * MODEL - LeaveRequest. Holds the information of one leave request.
 * All fields are private (encapsulation).
 */
public class LeaveRequest {

    private String requestId;
    private String employeeId;
    private LeaveType leaveType;
    private int numberOfDays;
    private String reason;
    private LeaveStatus status;

    // Every new request starts with the PENDING status
    public LeaveRequest(String requestId, String employeeId, LeaveType leaveType,
                        int numberOfDays, String reason) {
        this.requestId = requestId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.status = LeaveStatus.PENDING;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public String getReason() {
        return reason;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    // Displays the request in the format given in the assessment
    public void displayRequestDetails() {
        System.out.println("Request ID  : " + requestId);
        System.out.println("Employee ID : " + employeeId);
        System.out.println("Leave Type  : " + leaveType);
        System.out.println("Days        : " + numberOfDays);
        System.out.println("Reason      : " + reason);
        System.out.println("Status      : " + status);
    }

    // One row for the "Display All Leave Requests" table
    public void displayTableRow() {
        System.out.printf("%-12s %-12s %-10s %-6d %-10s%n",
                requestId, employeeId, leaveType, numberOfDays, status);
    }
}
