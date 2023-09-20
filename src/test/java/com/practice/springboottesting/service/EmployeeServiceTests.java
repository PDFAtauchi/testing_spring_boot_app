package com.practice.springboottesting.service;

import com.github.javafaker.Faker;
import com.practice.springboottesting.exception.ResourceNotFoundException;
import com.practice.springboottesting.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.practice.springboottesting.repository.EmployeeRepository;
import com.practice.springboottesting.service.Impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    Employee getNewEmployee() {
        Faker faker = new Faker(new Locale("en-US"));
        long fakeId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return Employee.builder()
                .id(fakeId)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();
    }

    List<Employee> getListOfNewEmployees(int numberEmployees) {
        List<Employee> employees = new ArrayList<Employee>();
        for (int i = 0; i < numberEmployees; i++) {
            employees.add(getNewEmployee());
        }
        return employees;
    }

    @BeforeEach
    public void setup() {
        employee = getNewEmployee();
    }

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // Given and employee

        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        // When
        Employee savedEmployee = employeeService.save(employee);

        // Then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenThrowException() {
        // Given and employee
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // When
        assertThrows(ResourceNotFoundException.class, () -> {
            Employee savedEmployee = employeeService.save(employee);
        });

        // Then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // Given
        int numberEmployees = 3;
        given(employeeRepository.findAll()).willReturn(getListOfNewEmployees(numberEmployees));

        // When
        List<Employee> retrievedEmployeeList = employeeService.getAllEmployees();

        // Then
        assertThat(retrievedEmployeeList).isNotNull();
        assertThat(retrievedEmployeeList.size()).isEqualTo(numberEmployees);
    }

    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // Given
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // When
        List<Employee> retrievedEmployeeList = employeeService.getAllEmployees();

        // Then
        assertThat(retrievedEmployeeList).isEmpty();
        assertThat(retrievedEmployeeList.size()).isEqualTo(0);
    }

    @Test
    public void givenEmployee_whenGetEmployeeById_thenReturnEmployeeObject() {
        // Given
        long id = employee.getId();
        given(employeeRepository.findById(id)).willReturn(Optional.of(employee));

        // When
        Employee retrievedEmployee = employeeService.getEmployeeById(id).get();

        // Then
        assertThat(retrievedEmployee).isNotNull();
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeUpdated() {
        // Given
        given(employeeRepository.save(employee)).willReturn(employee);

        String updatedFirstName = "myNewName";
        String updatedEmail = "mynewEmail@gmail.com";
        employee.setFirstName(updatedFirstName);
        employee.setEmail(updatedEmail);

        // When
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // Then
        assertThat(updatedEmployee.getFirstName()).isEqualTo(updatedFirstName);
        assertThat(updatedEmployee.getEmail()).isEqualTo(updatedEmail);
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenEmployeeNoMoreExist() {
        // Given
        willDoNothing().given(employeeRepository).deleteById(employee.getId());

        // When
        employeeService.deleteEmployee(employee.getId());

        // Then
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }
}
