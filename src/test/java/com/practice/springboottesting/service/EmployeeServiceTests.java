package com.practice.springboottesting.service;

import com.practice.springboottesting.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.springboottesting.repository.EmployeeRepository;
import com.practice.springboottesting.service.Impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;

import org.mockito.Mockito;

import java.util.Optional;

public class EmployeeServiceTests {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // Given
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Tom")
                .lastName("lin")
                .email("lin@gmail.com")
                .build();

        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        // When
        Employee savedEmployee = employeeService.save(employee);

        // Then
        assertThat(savedEmployee).isNotNull();
    }

}
