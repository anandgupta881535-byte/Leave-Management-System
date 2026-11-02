package com.hcl.leave.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hcl.leave.exception.EmployeeNotFoundException;
import com.hcl.leave.exception.InsufficientLeaveException;
import com.hcl.leave.exception.InvalidLeaveDaysException;
import com.hcl.leave.exception.LeaveAlreadyProcessedException;
import com.hcl.leave.exception.LeaveRequestNotFoundException;
import com.hcl.leave.model.Employee;
import com.hcl.leave.model.LeaveRequest;
import com.hcl.leave.model.LeaveStatus;
import com.hcl.leave.model.LeaveType;

/**
 * SERVICE (business logic) - everything related to LEAVE REQUESTS.
 * Uses EmployeeService to reach employee data.
 */
public class LeaveService {

    private final EmployeeService employeeService;

    // ArrayList : stores all leave requests
    private final List<LeaveRequest> leaveRequestList = new ArrayList<LeaveRequest>();

    // Used to generate request IDs : LR1001, LR1002, ...
    private int nextRequestNumber = 1001;

    public LeaveService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // ------------------------------------------------------------------
    // Apply leave : validate type, days and balance, then create the
    // request with PENDING status and store it
    // ------------------------------------------------------------------
    public LeaveRequest applyLeave(Employee employee, String leaveTypeText, int days, String reason)
            throws InvalidLeaveDaysException, InsufficientLeaveException {

        // Convert the text to the LeaveType enum
        LeaveType leaveType;
        try {
            leaveType = LeaveType.valueOf(leaveTypeText.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid leave type. Choose CASUAL, SICK or EARNED.");
        }

        // Case 3 : zero or negative days are not allowed
        if (days <= 0) {
            throw new InvalidLeaveDaysException();
        }
        // Case 2 : requested days more than available balance
        if (!employee.hasSufficientBalance(days)) {
            throw new InsufficientLeaveException(employee.getAvailableLeaveBalance(), days);
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Reason cannot be empty.");
        }

        // Create and store the leave request (status = PENDING)
        String requestId = "LR" + nextRequestNumber++;
        LeaveRequest request = new LeaveRequest(requestId, employee.getEmployeeId(),
                leaveType, days, reason.trim());
        leaveRequestList.add(request);
        return request;
    }

    // ------------------------------------------------------------------
    // Approve a PENDING request and deduct the days from the balance
    // ------------------------------------------------------------------
    public LeaveRequest approveLeave(String requestId) throws LeaveRequestNotFoundException,
            LeaveAlreadyProcessedException, EmployeeNotFoundException,
            InvalidLeaveDaysException, InsufficientLeaveException {

        LeaveRequest request = findLeaveRequest(requestId);
        checkPending(request);

        Employee employee = employeeService.findEmployee(request.getEmployeeId());

        // Deducts the days. Throws InsufficientLeaveException if the balance
        // became too low after other approvals (request then stays PENDING).
        employee.deductLeave(request);
        request.setStatus(LeaveStatus.APPROVED);
        return request;
    }

    // ------------------------------------------------------------------
    // Reject a PENDING request (employee balance is NOT changed)
    // ------------------------------------------------------------------
    public LeaveRequest rejectLeave(String requestId)
            throws LeaveRequestNotFoundException, LeaveAlreadyProcessedException {

        LeaveRequest request = findLeaveRequest(requestId);
        checkPending(request);
        request.setStatus(LeaveStatus.REJECTED);
        return request;
    }

    // ------------------------------------------------------------------
    // Finds a leave request by ID
    // ------------------------------------------------------------------
    public LeaveRequest findLeaveRequest(String requestId) throws LeaveRequestNotFoundException {
        String id = (requestId == null) ? "" : requestId.trim();
        for (LeaveRequest request : leaveRequestList) {
            if (request.getRequestId().equals(id.toUpperCase())) {
                return request;
            }
        }
        throw new LeaveRequestNotFoundException(id);
    }

    // ------------------------------------------------------------------
    // Returns all leave requests (read-only list)
    // ------------------------------------------------------------------
    public List<LeaveRequest> getAllLeaveRequests() {
        return Collections.unmodifiableList(leaveRequestList);
    }

    // Only a PENDING request can be approved or rejected
    private void checkPending(LeaveRequest request) throws LeaveAlreadyProcessedException {
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new LeaveAlreadyProcessedException(request.getRequestId(),
                    request.getStatus().toString());
        }
    }
}
