package com.hcl.leave.view;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.hcl.leave.model.Employee;
import com.hcl.leave.model.LeaveRequest;

/**
 * VIEW - all console input and output lives here.
 * It contains NO business logic; it only shows data and reads user input.
 */
public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);

    // ---------------- Input ----------------

    // Reads one line of text
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Reads a whole number; throws IllegalArgumentException for bad input
    public int readInt(String prompt) {
        String text = readString(prompt);
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid whole number.");
        }
    }

    // ---------------- General output ----------------

    public void showMenu() {
        System.out.println();
        System.out.println("========== Employee Leave Management System ==========");
        System.out.println("1. Add Employee");
        System.out.println("2. Display All Employees");
        System.out.println("3. Search Employee");
        System.out.println("4. Apply Leave");
        System.out.println("5. Approve Leave");
        System.out.println("6. Reject Leave");
        System.out.println("7. Display All Leave Requests");
        System.out.println("8. Display Employee Leave Balance");
        System.out.println("9. Exit");
        System.out.println("======================================================");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println("Error: " + message);
    }

    // ---------------- Employee output ----------------

    // Displays all employees using an Iterator
    public void showAllEmployees(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        System.out.println("\n---------------- All Employees ----------------");
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            employee.displayEmployeeDetails(); // polymorphic call
            System.out.println("-----------------------------------------------");
        }
    }

    // Displays the complete details of one employee
    public void showEmployee(Employee employee) {
        System.out.println();
        employee.displayEmployeeDetails("Employee Details");
    }

    // Displays only the leave balance of one employee
    public void showLeaveBalance(Employee employee) {
        System.out.println("Employee ID   : " + employee.getEmployeeId());
        System.out.println("Employee Name : " + employee.getEmployeeName());
        System.out.println("Leave Balance : " + employee.getAvailableLeaveBalance());
    }

    // ---------------- Leave request output ----------------

    // Shown after a leave request is created
    public void showLeaveRequestCreated(LeaveRequest request) {
        System.out.println("Leave request created successfully.");
        System.out.println("Request ID: " + request.getRequestId());
        System.out.println("Status: " + request.getStatus());
    }

    // Shown after a leave request is approved
    public void showApproval(Employee employee, LeaveRequest request, int previousBalance) {
        System.out.println("Leave Request Approved.");
        System.out.println("Employee ID : " + employee.getEmployeeId());
        System.out.println("Previous Balance: " + previousBalance);
        System.out.println("Leave Used : " + request.getNumberOfDays());
        System.out.println("Remaining Leave : " + employee.getAvailableLeaveBalance());
    }

    // Shown after a leave request is rejected
    public void showRejection(Employee employee) {
        System.out.println("Leave Request Rejected.");
        System.out.println("Employee ID : " + employee.getEmployeeId());
        System.out.println("Leave Balance (unchanged) : " + employee.getAvailableLeaveBalance());
    }

    // Displays all leave requests in a table
    public void showAllLeaveRequests(List<LeaveRequest> requests) {
        if (requests.isEmpty()) {
            System.out.println("No leave requests found.");
            return;
        }
        System.out.println();
        System.out.printf("%-12s %-12s %-10s %-6s %-10s%n",
                "Request ID", "Employee ID", "Leave Type", "Days", "Status");
        System.out.println("-------------------------------------------------------");
        Iterator<LeaveRequest> iterator = requests.iterator();
        while (iterator.hasNext()) {
            iterator.next().displayTableRow();
        }
    }

    // Closes the scanner when the application ends
    public void close() {
        scanner.close();
    }
}
