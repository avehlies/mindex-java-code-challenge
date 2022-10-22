# Gradle/Setup/Configuration
1. **Add Mockito dependency.**

    Mockito is a great framework for mocking services in unit tests.

2. **Add JaCoCo plugin to calculate test code coverage.**

# Changes to Existing Code

1. **Change to constructor-based autowiring.**

    This will make it easier for unit testing since we can inject Mocks into our services under test.

2. **Change POJO setters to return the class to allow for chaining.**

    This makes it a bit less verbose to manually create an object.

3. **Move `EmployeeServiceImplTest` to a controller test folder. Rename to `EmployeeControllerSpringTest`.**

    I feel like this is closer to an integration test than a unit test, so I renamed it. It's checking things 
    at the controller level instead of at the Service level. If the entry point were the `EmployeeService` methods
    then I'd be comfortable calling it an `EmployeeService` test.


# Additions to Code

1. Returning a `ReportingStructure`
    + Add `ReportingStructure` class
    + Add method to `EmployeeController`
    + Add repository method to retrieve multiple `Employee`s by ID values
    + Add service method to `EmployeeService` to get a `ReportingStructure` by employee ID
        + Add recursive method to create a `ReportingStructure` for an `Employee` and traverse tree
2.