package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    private final CompensationService compensationService;
    private final EmployeeService employeeService;

    public CompensationController(
            final CompensationService compenSationService,
            final EmployeeService employeeService
    ) {
        this.compensationService = compenSationService;
        this.employeeService = employeeService;
    }

    @PostMapping("/compensation")
    public Compensation create(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);

        // Get the current employee so we can store it along with the compensation
        // There's a good chance that we will want to know what the employee's role
        // and department was when we provided a new compensation.
        String employeeId = compensation.getEmployee().getEmployeeId();
        Employee employee = employeeService.read(employeeId);
        compensation.setEmployee(employee);

        return compensationService.create(compensation);
    }

    @GetMapping("/compensation/{id}")
    public List<Compensation> read(@PathVariable String id) {
        LOG.debug("Received compensation create request for id [{}]", id);

        return compensationService.getByEmployeeId(id);
    }
}
