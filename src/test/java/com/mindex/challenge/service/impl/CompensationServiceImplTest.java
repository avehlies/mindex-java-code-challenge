package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(BlockJUnit4ClassRunner.class)
public class CompensationServiceImplTest {

    private final CompensationRepository compensationRepository = Mockito.mock(CompensationRepository.class);
    private final CompensationService compensationService = new CompensationServiceImpl(
            compensationRepository
    );

    @Test
    public void testCreate() {
        Employee employee = new Employee()
                .setEmployeeId("1")
                .setFirstName("Andrew")
                .setLastName("Vehlies")
                .setDepartment("Engineering")
                .setPosition("Software Developer")
                .setDirectReports(null);

        Compensation compensation = new Compensation()
                .setEmployee(employee)
                .setEffectiveDate(LocalDate.of(2022, 10, 1))
                .setSalary(BigDecimal.valueOf(80000.00));

        when(compensationRepository.insert(compensation))
                .thenReturn(compensation);

        Compensation result = compensationService.create(compensation);
        assertEquals(compensation, result);

        verify(compensationRepository, times(1))
                .insert(compensation);

        verifyNoMoreInteractions(compensationRepository);
    }

    @Test
    public void testGetByEmployeeId() {
        Employee employee = new Employee()
                .setEmployeeId("1")
                .setFirstName("Andrew")
                .setLastName("Vehlies")
                .setDepartment("Engineering")
                .setPosition("Software Developer")
                .setDirectReports(null);

        Compensation compensation = new Compensation()
                .setEmployee(employee)
                .setEffectiveDate(LocalDate.of(2022, 10, 1))
                .setSalary(BigDecimal.valueOf(80000.00));
        Compensation compensation2 = new Compensation()
                .setEmployee(employee)
                .setEffectiveDate(LocalDate.of(2022, 10, 15))
                .setSalary(BigDecimal.valueOf(80000.01));

        List<Compensation> compensations = Arrays.asList(compensation, compensation2);

        when(compensationRepository.findByEmployeeId(employee.getEmployeeId()))
                .thenReturn(compensations);

        List<Compensation> result = compensationService.getByEmployeeId(employee.getEmployeeId());
        assertEquals(compensations, result);

        verify(compensationRepository, times(1))
                .findByEmployeeId(employee.getEmployeeId());

        verifyNoMoreInteractions(compensationRepository);
    }

}
