package com.practice.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.practice.springboottesting.model.Employee;
import com.practice.springboottesting.service.EmployeeService;

import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    private String serviceUrl;

    Employee getNewEmployee() {
        Faker faker = new Faker(new Locale("en-US"));
        return Employee.builder()
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
        serviceUrl = "/api/employees/";
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // Given
        given(employeeService.save(any(Employee.class)))
                .willAnswer((invocation) -> {
                    Employee employeeArgument = invocation.getArgument(0);
                    return employeeArgument;
                });

        // When
        ResultActions response = mockMvc.perform(post(serviceUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // Then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        // Given
        int numberEmployees = 4;
        List<Employee> employees = getListOfNewEmployees(numberEmployees);

        given(employeeService.getAllEmployees()).willReturn(employees);

        // When
        ResultActions response = mockMvc.perform(get(serviceUrl));

        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(employees.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // Given
        employee.setId(1L);
        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));

        // When
        ResultActions response = mockMvc.perform(get(serviceUrl + "{id}/", employee.getId()));

        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // Given
        employee.setId(1L);
        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.empty());

        // When
        ResultActions response = mockMvc.perform(get(serviceUrl + "{id}/", employee.getId()));

        // Then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        // Given
        long employeeId = 1L;
        employee.setId(employeeId);

        Employee updatedEmployee = getNewEmployee();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> {
                    Employee employeeArgument = invocation.getArgument(0);
                    return employeeArgument;
                });

        // When
        ResultActions response = mockMvc.perform(put(serviceUrl + "{id}/", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

    }

    @Test
    public void givenInvalidUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {
        // Given
        long employeeId = 1L;
        employee.setId(employeeId);

        Employee updatedEmployee = getNewEmployee();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> {
                    Employee employeeArgument = invocation.getArgument(0);
                    return employeeArgument;
                });

        // When
        ResultActions response = mockMvc.perform(put(serviceUrl + "{id}/", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // Then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnStatusCode200() throws Exception {
        // Given
        long employeeId = 1L;
        employee.setId(employeeId);

        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // When
        ResultActions response = mockMvc.perform(delete(serviceUrl + "{id}/", employeeId));

        // Then
        response.andExpect(status().isOk())
                .andDo(print());
        verify(employeeService, times(1)).deleteEmployee(employee.getId());

    }
}
