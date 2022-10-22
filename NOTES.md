# Gradle/Setup/Configuration
1. **Add Mockito dependency.**

    Mockito is a great framework for mocking services in unit tests.

2. **Add JaCoCo plugin to calculate test code coverage.**

# Thoughts
1. The `README.md` shows that the `Employee` JSON schema has `"directReports"` as an array of strings.
    In reality, it isn't. The `GET` response from `/employee/:id` is actually:
    ```json
    {
        "employeeId": "16a596ae-edd3-4847-99fe-c4518e82c86f",
        "firstName": "John",
        "lastName": "Lennon",
        "position": "Development Manager",
        "department": "Engineering",
        "directReports": [
            {
                "employeeId": "b7839309-3348-463b-a7e3-5de1c168beb3",
                "firstName": null,
                "lastName": null,
                "position": null,
                "department": null,
                "directReports": null
            },
            {
                "employeeId": "03aa1462-ffa9-4978-901b-7c001562cf6f",
                "firstName": null,
                "lastName": null,
                "position": null,
                "department": null,
                "directReports": null
            }
        ]
    }
    ```
   `directReports` is actually an array of `Employee` objects, so I'll treat it that way.
2. 


# Changes to Existing Code

1. **Change to constructor-based autowiring.**

    This will make it easier for unit testing since we can inject Mocks into our services under test.

2. **Change POJO setters to return the class to allow for chaining.**

    This makes it a bit less verbose to manually create an object.

3. **Add `@Id` to `Employee.employeeId`.**
    Without it, the `update` call was actually saving a new row of data. We should
    be using JPA to define the `_id` of the row.

# Additions to Code

1. Returning a `ReportingStructure`
    + Add `ReportingStructure` class
    + Add method to `EmployeeController`
    + Add repository method to retrieve multiple `Employee`s by ID values
    + Add service method to `EmployeeService` to get a `ReportingStructure` by employee ID
        + Add recursive method to create a `ReportingStructure` for an `Employee` and traverse tree

# Testing

1. **Move `EmployeeServiceImplTest` to a controller test folder. Rename to `EmployeeControllerSpringTest`.**

   I feel like this is closer to an integration test than a unit test, so I renamed it. It's checking things
   at the controller level instead of at the Service level. If the entry point were the `EmployeeService` methods
   then I'd be comfortable calling it an `EmployeeService` test.

2. **Added `Mindex Java Code Challenge.postman_collection`**
    This Postman collection can be used to manually test creating, getting, and updating Employees.
