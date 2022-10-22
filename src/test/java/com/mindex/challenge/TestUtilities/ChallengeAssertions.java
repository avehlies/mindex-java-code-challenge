package com.mindex.challenge.TestUtilities;

import com.mindex.challenge.data.Employee;

import static org.junit.Assert.assertEquals;

public class ChallengeAssertions {

    public static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
