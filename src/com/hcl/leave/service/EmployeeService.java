package com.hcl.leave.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hcl.leave.exception.DuplicateEmployeeException;
import com.hcl.leave.exception.EmployeeNotFoundException;
import com.hcl.leave.model.ContractEmployee;
import com.hcl.leave.model.Employee;
import com.hcl.leave.model.PermanentEmployee;

/**
 * SERVICE (business logic) - everything related to EMPLOYEES.
 * Owns the ArrayList, HashSet and HashMap collections.
 */
public class EmployeeService {

    // ArrayList : stores all employee records
    private final List<Employee> employeeList = new ArrayList<Employee>();

    // HashSet : keeps unique (lower case) emails -> duplicates are rejected
    private final Set<String> emailSet = new HashSet<String>();

    // HashMap : fast lookup, employee ID -> Employee object
    private final Map<String, Employee> employeeMap = new HashMap<String, Employee>();

    // ------------------------------------------------------------------
    // Add a new employee after validating ID, email, balance and type
    // ------------------------------------------------------------------
    public Employee addEmployee(String id, String name, String department, String email,
                                int balance, String type) throws DuplicateEmployeeException {

        // Basic validation (empty values, negative balance)
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Leave balance cannot be negative.");
        }

        String normalizedId = id.trim().toUpperCase();
        String normalizedEmail = (email == null) ? "" : email.trim().toLowerCase();

        // Duplicate employee ID check (HashMap)
        if (employeeMap.containsKey(normalizedId)) {
            throw new DuplicateEmployeeException("Employee ID " + normalizedId + " already exists.");
        }
        // Duplicate email check (HashSet)
        if (emailSet.contains(normalizedEmail)) {
            throw new DuplicateEmployeeException("Email " + email.trim() + " already exists.");
        }

        // POLYMORPHISM : parent reference holds a child object
        Employee employee;
        String lowerType = (type == null) ? "" : type.trim().toLowerCase();
        if (lowerType.equals("permanent") || lowerType.equals("p") || lowerType.equals("1")) {
            employee = new PermanentEmployee(id, name, department, email, balance);
        } else if (lowerType.equals("contract") || lowerType.equals("c") || lowerType.equals("2")) {
            employee = new ContractEmployee(id, name, department, email, balance);
        } else {
            throw new IllegalArgumentException("Invalid employee type. Enter Permanent or Contract.");
        }

        // Store the employee in all three collections
        // (done last, so nothing is stored if any validation above fails)
        employeeList.add(employee);                          // ArrayList
        employeeMap.put(employee.getEmployeeId(), employee); // HashMap
        emailSet.add(normalizedEmail);                       // HashSet

        return employee;
    }

    // ------------------------------------------------------------------
    // Returns all employees (read-only list)
    // ------------------------------------------------------------------
    public List<Employee> getAllEmployees() {
        return Collections.unmodifiableList(employeeList);
    }

    // ------------------------------------------------------------------
    // Finds an employee by ID using the HashMap (fast lookup)
    // ------------------------------------------------------------------
    public Employee findEmployee(String employeeId) throws EmployeeNotFoundException {
        String id = (employeeId == null) ? "" : employeeId.trim();
        Employee employee = employeeMap.get(id.toUpperCase());
        if (employee == null) {
            throw new EmployeeNotFoundException(id);
        }
        return employee;
    }
}
