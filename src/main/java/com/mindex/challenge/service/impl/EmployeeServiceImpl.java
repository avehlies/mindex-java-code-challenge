package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure getReportingStructureById(String id) {
        Employee employee = read(id);

        return getReportingStructureForEmployee(employee);
    }

    /**
     * Create a {@link ReportingStructure} for an {@link Employee} and traverse down the tree of reports
     * to get a total of direct and indirect reports
     *
     * @param employee the employee to get a reporting structure for
     * @return a {@code ReportingStructure}
     */
    private ReportingStructure getReportingStructureForEmployee(Employee employee) {
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return new ReportingStructure(employee, 0);
        }

        List<String> employeeReportIds = employee.getDirectReports().stream()
                .map(Employee::getEmployeeId)
                .collect(Collectors.toList());

        // It's more efficient to get all the direct employees with a list of IDs instead of doing one query
        // at a time with a single ID.
        List<Employee> directReports = employeeRepository.findByEmployeeIdIn(employeeReportIds);
        employee.setDirectReports(directReports);

        int reportsFromDirectReports = directReports.stream()
                .map(this::getReportingStructureForEmployee)
                .map(ReportingStructure::getNumberOfReports)
                .reduce(0, Integer::sum);

        return new ReportingStructure(employee, directReports.size() + reportsFromDirectReports);
    }
}
