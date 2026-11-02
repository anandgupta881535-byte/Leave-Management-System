package com.hcl.leave.controller;

import java.util.NoSuchElementException;

import com.hcl.leave.exception.DuplicateEmployeeException;
import com.hcl.leave.exception.EmployeeNotFoundException;
import com.hcl.leave.exception.InsufficientLeaveException;
import com.hcl.leave.exception.InvalidLeaveDaysException;
import com.hcl.leave.exception.LeaveAlreadyProcessedException;
import com.hcl.leave.exception.LeaveRequestNotFoundException;
import com.hcl.leave.model.Employee;
import com.hcl.leave.model.LeaveRequest;
import com.hcl.leave.service.EmployeeService;
import com.hcl.leave.service.LeaveService;
import com.hcl.leave.view.ConsoleView;

/**
 * CONTROLLER - connects the VIEW and the SERVICES.
 * It shows the menu, reads the user's choice, asks the view for input,
 * calls the correct service method and tells the view what to display.
 * It also catches every exception so the application never crashes.
 */
public class LeaveController {

    private final ConsoleView view;
    private final EmployeeService employeeService;
    private final LeaveService leaveService;

    public LeaveController(ConsoleView view, EmployeeService employeeService,
                           LeaveService leaveService) {
        this.view = view;
        this.employeeService = employeeService;
        this.leaveService = leaveService;
    }

    // ------------------------------------------------------------------
    // start : repeated menu loop (loop + switch)
    // ------------------------------------------------------------------
    public void start() {
        boolean running = true;

        while (running) { // loop keeps the menu repeating
            try {
                view.showMenu();
                int choice = view.readInt("Enter your choice: ");

                switch (choice) { // switch handles the menu
                case 1:
                    addEmployee();
                    break;
                case 2:
                    displayAllEmployees();
                    break;
                case 3:
                    searchEmployee();
                    break;
                case 4:
                    applyLeave();
                    break;
                case 5:
                    approveLeave();
                    break;
                case 6:
                    rejectLeave();
                    break;
                case 7:
                    displayAllLeaveRequests();
                    break;
                case 8:
                    displayLeaveBalance();
                    break;
                case 9:
                    view.showMessage("Thank you for using Employee Leave Management System.");
                    running = false;
                    break;
                default:
                    view.showError("Invalid choice. Please select 1 to 9.");
                }
            } catch (EmployeeNotFoundException | InsufficientLeaveException
                    | InvalidLeaveDaysException | DuplicateEmployeeException
                    | LeaveRequestNotFoundException | LeaveAlreadyProcessedException e) {
                // Business exceptions : show the custom message
                view.showError(e.getMessage());
            } catch (IllegalArgumentException e) {
                // Validation errors (empty fields, bad number, bad email ...)
                view.showError(e.getMessage());
            } catch (NoSuchElementException e) {
                // Input stream closed (Ctrl+D / end of input) -> exit cleanly
                view.showMessage("\nNo more input available. Exiting application.");
                running = false;
            } catch (Exception e) {
                // Safety net for anything unexpected
                view.showMessage("Unexpected error: " + e.getMessage());
            }
        }
        view.close();
    }

    // ------------------------------------------------------------------
    // OPTION 1 : Add Employee
    // ------------------------------------------------------------------
    private void addEmployee() throws DuplicateEmployeeException {
        String id = view.readString("Enter Employee ID: ");
        String name = view.readString("Enter Employee Name: ");
        String department = view.readString("Enter Department: ");
        String email = view.readString("Enter Email: ");
        int balance = view.readInt("Enter Leave Balance: ");
        String type = view.readString("Employee Type (Permanent/Contract): ");

        employeeService.addEmployee(id, name, department, email, balance, type);
        view.showMessage("Employee added successfully.");
    }

    // ------------------------------------------------------------------
    // OPTION 2 : Display all employees
    // ------------------------------------------------------------------
    private void displayAllEmployees() {
        view.showAllEmployees(employeeService.getAllEmployees());
    }

    // ------------------------------------------------------------------
    // OPTION 3 : Search employee by ID
    // ------------------------------------------------------------------
    private void searchEmployee() throws EmployeeNotFoundException {
        String id = view.readString("Enter Employee ID: ");
        Employee employee = employeeService.findEmployee(id);
        view.showEmployee(employee);
    }

    // ------------------------------------------------------------------
    // OPTION 4 : Apply Leave
    // ------------------------------------------------------------------
    private void applyLeave()
            throws EmployeeNotFoundException, InvalidLeaveDaysException, InsufficientLeaveException {
        String id = view.readString("Enter Employee ID: ");
        Employee employee = employeeService.findEmployee(id); // stops here if not found

        String leaveType = view.readString("Enter Leave Type (CASUAL/SICK/EARNED): ");
        int days = view.readInt("Enter Number of Days: ");
        String reason = view.readString("Enter Reason: ");

        LeaveRequest request = leaveService.applyLeave(employee, leaveType, days, reason);
        view.showLeaveRequestCreated(request);
    }

    // ------------------------------------------------------------------
    // OPTION 5 : Approve Leave
    // ------------------------------------------------------------------
    private void approveLeave() throws LeaveRequestNotFoundException, EmployeeNotFoundException,
            LeaveAlreadyProcessedException, InvalidLeaveDaysException, InsufficientLeaveException {
        String requestId = view.readString("Enter Request ID: ");

        // Remember the balance before approval to show "Previous Balance"
        LeaveRequest request = leaveService.findLeaveRequest(requestId);
        Employee employee = employeeService.findEmployee(request.getEmployeeId());
        int previousBalance = employee.getAvailableLeaveBalance();

        leaveService.approveLeave(request.getRequestId());
        view.showApproval(employee, request, previousBalance);
    }

    // ------------------------------------------------------------------
    // OPTION 6 : Reject Leave
    // ------------------------------------------------------------------
    private void rejectLeave() throws LeaveRequestNotFoundException,
            LeaveAlreadyProcessedException, EmployeeNotFoundException {
        String requestId = view.readString("Enter Request ID: ");

        LeaveRequest request = leaveService.rejectLeave(requestId);
        Employee employee = employeeService.findEmployee(request.getEmployeeId());
        view.showRejection(employee);
    }

    // ------------------------------------------------------------------
    // OPTION 7 : Display all leave requests
    // ------------------------------------------------------------------
    private void displayAllLeaveRequests() {
        view.showAllLeaveRequests(leaveService.getAllLeaveRequests());
    }

    // ------------------------------------------------------------------
    // OPTION 8 : Display leave balance of an employee
    // ------------------------------------------------------------------
    private void displayLeaveBalance() throws EmployeeNotFoundException {
        String id = view.readString("Enter Employee ID: ");
        Employee employee = employeeService.findEmployee(id);
        view.showLeaveBalance(employee);
    }
}
