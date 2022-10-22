package com.mindex.challenge.TestUtilities;

import com.mindex.challenge.data.Employee;

import java.util.List;

/**
 * Helper methods to create Objects
 */
public final class ObjectHelper {

    /**
     * Quick helper method to create a new {@link Employee} without calling every setter
     * @param id employee's id
     * @param firstName employee's first name
     * @param lastName employee's last name
     * @param department employee's department
     * @param position employee's position
     * @param directReports employee's direct reports
     * @return a populated {@code Employee}
     */
    public static Employee newEmployee(
            String id,
            String firstName,
            String lastName,
            String department,
            String position,
            List<Employee> directReports) {
        return new Employee()
                .setEmployeeId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setDepartment(department)
                .setPosition(position)
                .setDirectReports(directReports);
    }
}
