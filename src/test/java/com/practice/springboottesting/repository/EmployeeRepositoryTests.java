package com.practice.springboottesting.repository;

import com.practice.springboottesting.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // Given
        Employee employee = Employee.builder()
                .firstName("Tom")
                .lastName("lin")
                .email("lin@gmail.com")
                .build();

        // When
        Employee savedEmployee = employeeRepository.save(employee);

        // Then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }

    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        // Given
        Employee employee1 = Employee.builder()
                .firstName("Tom")
                .lastName("lin")
                .email("lin@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Tim")
                .lastName("lang")
                .email("lang@gmail.com")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // When
        List<Employee> employeeList = employeeRepository.findAll();

        // Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // Given
        Employee employee = Employee.builder()
                .firstName("Tom")
                .lastName("lin")
                .email("lin@gmail.com")
                .build();

        employeeRepository.save(employee);

        // When
        Employee retrievedEmployee = employeeRepository.findById(employee.getId()).get();

        // Then
        assertThat(retrievedEmployee).isNotNull();
    }

    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        // Given
        Employee employee = Employee.builder()
                .firstName("Tom")
                .lastName("lin")
                .email("lin@gmail.com")
                .build();

        employeeRepository.save(employee);

        // When
        Employee retrievedEmployee = employeeRepository.findByEmail(employee.getEmail()).get();

        // Then
        assertThat(retrievedEmployee).isNotNull();
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // Given
        Employee employee = Employee.builder()
                .firstName("Tom")
                .lastName("lin")
                .email("lin@gmail.com")
                .build();

        employeeRepository.save(employee);

        // When
        Employee retrievedEmployee = employeeRepository.findById(employee.getId()).get();

        String newEmail = "linupdate@gmail.com";
        String newFirstName = "Lin";
        retrievedEmployee.setEmail(newEmail);
        retrievedEmployee.setFirstName(newFirstName);
        Employee updatedEmployee = employeeRepository.save(retrievedEmployee);

        // Then
        assertThat(updatedEmployee.getEmail()).isEqualTo(newEmail);
        assertThat(updatedEmployee.getFirstName()).isEqualTo(newFirstName);
    }

    @Test
    public void givenEmployeeObject_whenDeleteEmployeeById_thenRemoveEmployee() {
        // Given
        Employee employee = Employee.builder()
                .firstName("Tom")
                .lastName("lin")
                .email("lin@gmail.com")
                .build();

        employeeRepository.save(employee);

        // When
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> retrievedEmployee = employeeRepository.findById(employee.getId());

        // Then
        assertThat(retrievedEmployee).isEmpty();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindBy_thenReturnEmployeeObject() {
        // Given
        String firstName = "Tom";
        String lastName = "lin";
        Employee employee = Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email("lin@gmail.com")
                .build();

        employeeRepository.save(employee);

        // When
        Employee retrievedEmployee = employeeRepository.findByFirstNameAndLastName(firstName, lastName);

        // Then
        assertThat(retrievedEmployee).isNotNull();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByParams_thenReturnEmployeeObject() {
        // Given
        String firstName = "Tom";
        String lastName = "lin";
        Employee employee = Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email("lin@gmail.com")
                .build();

        employeeRepository.save(employee);

        // When
        Employee retrievedEmployee = employeeRepository.findByFirstNameAndLastNameParams(firstName, lastName);

        // Then
        assertThat(retrievedEmployee).isNotNull();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindBySql_thenReturnEmployeeObject() {
        // Given
        String firstName = "Tom";
        String lastName = "lin";
        Employee employee = Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email("lin@gmail.com")
                .build();

        employeeRepository.save(employee);

        // When
        Employee retrievedEmployee = employeeRepository.findByFirstNameAndLastNameSql(firstName, lastName);

        // Then
        assertThat(retrievedEmployee).isNotNull();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByParamsSql_thenReturnEmployeeObject() {
        // Given
        String firstName = "Tom";
        String lastName = "lin";
        Employee employee = Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email("lin@gmail.com")
                .build();

        employeeRepository.save(employee);

        // When
        Employee retrievedEmployee = employeeRepository.findByFirstNameAndLastNameParamsSql(firstName, lastName);

        // Then
        assertThat(retrievedEmployee).isNotNull();
    }
}
