package com.mindex.challenge.controller;

import com.mindex.challenge.TestUtilities.ObjectHelper;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.mindex.challenge.TestUtilities.ChallengeAssertions.assertEmployeeEquivalence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationControllerSpringTest {

    private String compensationUrl;
    private String compensationIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    /**
     * I'd normally want to run a @Sql or something equivalent to pre-populate the database
     * with an Employee so the Compensation test doesn't run anything not Compensation related.
     */
    @Test
    public void testCreateRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create an employee to test with
        Employee createdEmployee = restTemplate.postForEntity("/employee", testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);

        String id = createdEmployee.getEmployeeId();
        Compensation compensation1 = new Compensation()
                .setEmployee(createdEmployee)
                .setEffectiveDate(LocalDate.of(2022, 1, 1))
                .setSalary(BigDecimal.valueOf(50000));

        Compensation createdCompensation1 = restTemplate
                .postForEntity(compensationUrl, compensation1, Compensation.class, new Object[] {id})
                .getBody();
        assertNotNull(createdCompensation1);

        Compensation compensation2 = new Compensation()
                .setEmployee(createdEmployee)
                .setEffectiveDate(LocalDate.of(2022, 7, 1))
                .setSalary(BigDecimal.valueOf(75000));

        Compensation createdCompensation2 = restTemplate
                .postForEntity(compensationUrl, compensation2, Compensation.class, new Object[] {id})
                .getBody();
        assertNotNull(createdCompensation2);

        Compensation[] result = restTemplate
                .getForEntity(compensationIdUrl, Compensation[].class, new Object[] {id})
                .getBody();

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(0, BigDecimal.valueOf(50000).compareTo(result[0].getSalary()));
        assertEquals(0, BigDecimal.valueOf(75000).compareTo(result[1].getSalary()));
    }
}
