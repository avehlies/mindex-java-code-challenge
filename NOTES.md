# Changes to Existing Code

1. **Change to constructor-based autowiring.**

    This will make it easier for unit testing since we can inject Mocks into our services under test.


# Additions to Code

1. Returning a `ReportingStructure`
    + Add `ReportingStructure` class
    + Add method to `EmployeeController`
    + Add repository method to retrieve multiple `Employee`s by ID values
    + Add service method to `EmployeeService` to get a `ReportingStructure` by employee ID
        + Add recursive method to create a `ReportingStructure` for an `Employee` and traverse tree
2.