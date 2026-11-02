package com.hcl.leave;

import com.hcl.leave.controller.LeaveController;
import com.hcl.leave.service.EmployeeService;
import com.hcl.leave.service.LeaveService;
import com.hcl.leave.view.ConsoleView;

/**
 * MAIN CLASS - entry point of the application.
 * Creates the View, the Services and the Controller, connects them
 * together and starts the menu. (Run THIS class in Eclipse.)
 */
public class LeaveManagementSystem {

    public static void main(String[] args) {
        // View : console input/output
        ConsoleView view = new ConsoleView();

        // Services : business logic
        EmployeeService employeeService = new EmployeeService();
        LeaveService leaveService = new LeaveService(employeeService);

        // Controller : connects view and services
        LeaveController controller = new LeaveController(view, employeeService, leaveService);

        controller.start();
    }
}
