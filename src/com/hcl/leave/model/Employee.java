package com.hcl.leave.model;

import com.hcl.leave.exception.InsufficientLeaveException;
import com.hcl.leave.exception.InvalidLeaveDaysException;

/**
 * MODEL - Employee (parent class).
 * Demonstrates ENCAPSULATION (private fields + getters/setters), CONSTRUCTORS,
 * constructor OVERLOADING, METHOD OVERLOADING, private/protected/public
 * access modifiers and the "this" keyword.
 */
public abstract class Employee {

    // private : can be accessed only through getters/setters (encapsulation)
    private String employeeId;
    private String employeeName;
    private String department;
    private String email;

    // protected : visible to child classes (PermanentEmployee / ContractEmployee)
    protected int availableLeaveBalance;

    // Constructor 1 : full constructor, uses setters so validation always runs
    public Employee(String employeeId, String employeeName, String department,
                    String email, int availableLeaveBalance) {
        this.setEmployeeId(employeeId);
        this.setEmployeeName(employeeName);
        this.setDepartment(department);
        this.setEmail(email);
        this.setAvailableLeaveBalance(availableLeaveBalance);
    }

    // Constructor 2 (overloaded) : leave balance defaults to 0
    public Employee(String employeeId, String employeeName, String department, String email) {
        this(employeeId, employeeName, department, email, 0); // "this" calls Constructor 1
    }

    // ---------------- Getters and Setters (with validation) ----------------

    public String getEmployeeId() {
        return employeeId;
    }

    public final void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        }
        // IDs are stored in upper case so that emp101 and EMP101 are the same
        this.employeeId = employeeId.trim().toUpperCase();
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public final void setEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty.");
        }
        this.employeeName = employeeName.trim();
    }

    public String getDepartment() {
        return department;
    }

    public final void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        this.department = department.trim();
    }

    public String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        if (email == null || !email.trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email.trim();
    }

    public int getAvailableLeaveBalance() {
        return availableLeaveBalance;
    }

    public final void setAvailableLeaveBalance(int availableLeaveBalance) {
        if (availableLeaveBalance < 0) {
            throw new IllegalArgumentException("Leave balance cannot be negative.");
        }
        this.availableLeaveBalance = availableLeaveBalance;
    }

    // ---------------- Business logic ----------------

    // Checks whether the employee has enough leave balance
    public boolean hasSufficientBalance(int days) {
        return days <= availableLeaveBalance;
    }

    // METHOD OVERLOADING (1) : deduct leave using a number of days
    public void deductLeave(int days) throws InvalidLeaveDaysException, InsufficientLeaveException {
        if (days <= 0) {
            throw new InvalidLeaveDaysException();
        }
        if (!hasSufficientBalance(days)) {
            throw new InsufficientLeaveException(availableLeaveBalance, days);
        }
        availableLeaveBalance -= days; // operator used for leave calculation
    }

    // METHOD OVERLOADING (2) : deduct leave using a LeaveRequest object
    public void deductLeave(LeaveRequest request)
            throws InvalidLeaveDaysException, InsufficientLeaveException {
        deductLeave(request.getNumberOfDays());
    }

    // Child classes must say which type of employee they are
    protected abstract String getEmployeeType();

    // Displays the employee details (child classes OVERRIDE this method)
    public void displayEmployeeDetails() {
        System.out.println("Employee ID   : " + employeeId);
        System.out.println("Employee Name : " + employeeName);
        System.out.println("Department    : " + department);
        System.out.println("Email         : " + email);
        System.out.println("Leave Balance : " + availableLeaveBalance);
    }

    // METHOD OVERLOADING (3) : display details with a heading
    public void displayEmployeeDetails(String heading) {
        System.out.println("---- " + heading + " ----");
        displayEmployeeDetails(); // polymorphic call - runs the child's version
    }
}
