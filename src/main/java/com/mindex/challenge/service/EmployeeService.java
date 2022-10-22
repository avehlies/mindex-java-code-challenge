package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id);
    Employee update(Employee employee);

    /**
     * Create a {@link ReportingStructure} for an {@link Employee} given the employee's ID
     * @param id the ID of the employee
     * @return the {@code ReportingStructure} of the employee
     */
    ReportingStructure getReportingStructureById(String id);
}
