package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

import static com.mindex.challenge.TestUtilities.ChallengeAssertions.assertEmployeeEquivalence;
import static com.mindex.challenge.TestUtilities.ObjectHelper.newEmployee;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class EmployeeServiceImplTest {

    private final EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
    private final EmployeeService employeeService = new EmployeeServiceImpl(
            employeeRepository
    );

    @Test
    public void testCreate() {
        Employee jackHughes = newEmployee(
                UUID.randomUUID().toString(),
                "Jack",
                "Hughes",
                "Players",
                "Center",
                null
        );

        List<Employee> lindyRuffDirectReports = new ArrayList<>();
        lindyRuffDirectReports.add(jackHughes);

        Employee lindyRuff = newEmployee(
                null,
                "Lindy",
                "Ruff",
                "Coaching",
                "Head Coach",
                lindyRuffDirectReports
        );

        Employee result = employeeService.create(lindyRuff);
        assertEmployeeEquivalence(lindyRuff, result);
        assertNotNull(result.getEmployeeId());

        // Verify that we're calling Employee repository with an Employee
        // that now does not have a null employeeId
        verify(employeeRepository, times(1))
                .insert(argThat((Employee employee) -> employee.getEmployeeId() != null));

        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    public void testReadSuccess() {
        String id = UUID.randomUUID().toString();
        Employee nicoHischier = newEmployee(
                id,
                "Nico",
                "Hischier",
                "Players",
                "Center",
                null
        );

        when(employeeRepository.findByEmployeeId(id))
                .thenReturn(nicoHischier);

        Employee result = employeeService.read(id);

        assertEquals(nicoHischier, result);

        verify(employeeRepository, times(1))
                .findByEmployeeId(id);

        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    public void testReadFailure() {
        String id = UUID.randomUUID().toString();

        when(employeeRepository.findByEmployeeId(id))
                .thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.read(id);
        });

        assertTrue(exception.getMessage().contains("Invalid employeeId: " + id));
    }

    @Test
    public void testUpdate() {
        Employee damonSeverson = newEmployee(
                UUID.randomUUID().toString(),
                "Damon",
                "Severson",
                "Players",
                "Defense",
                null
        );

        employeeService.update(damonSeverson);

        verify(employeeRepository, times(1))
                .save(damonSeverson);
    }

    @Test
    @Parameters({
            "ceo, 10",
            "cto, 8",
            "cfo, 3",
            "seniorDevManager, 4",
            "devManager, 1",
            "dev1, 0",
            "principalDev, 1",
            "intern1, 0",
            "acctManager1, 0",
            "acctManager2, 0",
            "acctManager3, 0"
    })
    public void testGetReportingStructureById(String id, int expectedNumberOfReports) {
        Map<String, Employee> employeeMap = provideEmployees();
        setUpReportingStructureMocks(employeeMap);

        ReportingStructure ceoResult = employeeService.getReportingStructureById(employeeMap.get("ceo").getEmployeeId());
        assertNotNull(ceoResult);
        assertEmployeeEquivalence(employeeMap.get("ceo"), ceoResult.getEmployee());
        assertEquals(10, ceoResult.getNumberOfReports());

        ReportingStructure cfoResult = employeeService.getReportingStructureById(employeeMap.get("cfo").getEmployeeId());
        assertEmployeeEquivalence(employeeMap.get("cfo"), cfoResult.getEmployee());
        assertEquals(3, cfoResult.getNumberOfReports());

        ReportingStructure principalDevResult = employeeService.getReportingStructureById(employeeMap.get("principalDev").getEmployeeId());
        assertEmployeeEquivalence(employeeMap.get("principalDev"), principalDevResult.getEmployee());
        assertEquals(1, principalDevResult.getNumberOfReports());
    }

    private void setUpReportingStructureMocks(Map<String, Employee> employeeMap) {
        when(employeeRepository.findByEmployeeId(anyString()))
                .thenAnswer(invocationOnMock -> {
                    String id = invocationOnMock.getArgument(0);
                    return employeeMap.get(id);
                });

        // when calling employeeRepository with a list of IDs, return the list of appropriate Employees
        when(employeeRepository.findByEmployeeIdIn(anyList()))
                .thenAnswer(invocationOnMock -> {
                    List<String> ids = invocationOnMock.getArgument(0);
                    return ids.stream()
                            .map(employeeMap::get)
                            .collect(Collectors.toList());
                });
    }

    /**
     * Reporting Hierarchy:
     *  * CEO
     *  * * CTO
     *  * * * Senior Dev Manager
     *  * * * * Dev Manager
     *  * * * * * Dev One
     *  * * * * Principal Developer
     *  * * * * * Intern
     *  * * CFO
     *  * * * Acct Manager 1
     *  * * * Acct Manager 2
     *  * * * Acct Manager 3
     */
    public Map<String, Employee> provideEmployees() {
        Employee intern1 = newEmployee("intern1", "Dev", "Intern", "Development", "Intern", Collections.emptyList());
        Employee dev1 = newEmployee("dev1", "Dev", "One", "Development", "Senior Developer", null);
        Employee devManager = newEmployee("devManager", "DevManager", "One", "Development", "Dev Manager", Collections.singletonList(dev1));
        Employee principalDev = newEmployee("principalDev", "Dev", "Two", "Development", "Principal Developer", Collections.singletonList(intern1));
        Employee seniorDevManager = newEmployee("seniorDevManager", "Senior", "DevManager", "Development", "Senior Dev Manager", Arrays.asList(devManager, principalDev));
        Employee cto = newEmployee("cto", "CTO", "CTO", "Executives", "CTO", Collections.singletonList(seniorDevManager));

        Employee acctManager1 = newEmployee("acctManager1", "AcctManager", "One", "Accounting", "Manager", null);
        Employee acctManager2 = newEmployee("acctManager2", "AcctManager", "Two", "Accounting", "Manager", null);
        Employee acctManager3 = newEmployee("acctManager3", "AcctManager", "Three", "Accounting", "Manager", null);
        Employee cfo = newEmployee("cfo", "CFO", "CFO", "Executives", "CFO", Arrays.asList(acctManager1, acctManager2, acctManager3));

        Employee ceo = newEmployee("ceo", "CEO", "CEO", "Executives", "CEO", Arrays.asList(cto, cfo));

        Map<String, Employee> map = new HashMap<>();
        map.put(ceo.getEmployeeId(), ceo);
        map.put(cfo.getEmployeeId(), cfo);
        map.put(cto.getEmployeeId(), cto);
        map.put(acctManager1.getEmployeeId(), acctManager1);
        map.put(acctManager2.getEmployeeId(), acctManager2);
        map.put(acctManager3.getEmployeeId(), acctManager3);
        map.put(seniorDevManager.getEmployeeId(), seniorDevManager);
        map.put(principalDev.getEmployeeId(), principalDev);
        map.put(devManager.getEmployeeId(), devManager);
        map.put(dev1.getEmployeeId(), dev1);
        map.put(intern1.getEmployeeId(), intern1);

        return map;
    }
}
