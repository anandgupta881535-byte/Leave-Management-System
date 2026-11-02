package com.hcl.leave.model;

/**
 * MODEL - PermanentEmployee (child of Employee).
 * Demonstrates INHERITANCE, "super" and METHOD OVERRIDING.
 */
public class PermanentEmployee extends Employee {

    public PermanentEmployee(String employeeId, String employeeName, String department,
                             String email, int availableLeaveBalance) {
        super(employeeId, employeeName, department, email, availableLeaveBalance);
    }

    // Overrides the abstract method of the parent
    @Override
    protected String getEmployeeType() {
        return "Permanent";
    }

    // Overrides the display method of the parent
    @Override
    public void displayEmployeeDetails() {
        super.displayEmployeeDetails(); // reuse the parent display
        System.out.println("Employee Type : " + getEmployeeType());
    }
}
