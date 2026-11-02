package com.hcl.leave.model;

/**
 * MODEL - ContractEmployee (child of Employee).
 * Demonstrates INHERITANCE, "super" and METHOD OVERRIDING.
 */
public class ContractEmployee extends Employee {

    public ContractEmployee(String employeeId, String employeeName, String department,
                             String email, int availableLeaveBalance) {
        super(employeeId, employeeName, department, email, availableLeaveBalance);
    }

    // Overrides the abstract method of the parent
    @Override
    protected String getEmployeeType() {
        return "Contract";
    }

    // Overrides the display method of the parent
    @Override
    public void displayEmployeeDetails() {
        super.displayEmployeeDetails(); // reuse the parent display
        System.out.println("Employee Type : " + getEmployeeType());
    }
}
