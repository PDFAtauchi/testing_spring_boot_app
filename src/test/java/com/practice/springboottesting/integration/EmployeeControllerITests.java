package com.practice.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.practice.springboottesting.model.Employee;
import com.practice.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests extends AbstractionContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    private String serviceUrl;

    @BeforeEach
    void setup() {
        employee = getNewEmployee();
        serviceUrl = "/api/employees/";
        employeeRepository.deleteAll();
    }

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

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // Given

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
        employeeRepository.saveAll(employees);

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
        employeeRepository.save(employee);

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
        long employeeId = 1L;

        // When
        ResultActions response = mockMvc.perform(get(serviceUrl + "{id}/", employeeId));

        // Then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        // Given
        employeeRepository.save(employee);
        Employee updatedEmployee = getNewEmployee();

        // When
        ResultActions response = mockMvc.perform(put(serviceUrl + "{id}/", employee.getId())
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
    public void givenInvalidUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        // Given
        long noExistEmployeeId = 2L;
        employeeRepository.save(employee);
        Employee updatedEmployee = getNewEmployee();

        // When
        ResultActions response = mockMvc.perform(put(serviceUrl + "{id}/", noExistEmployeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // Then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnStatusCode200() throws Exception {
        // Given
        employeeRepository.save(employee);

        // When
        ResultActions response = mockMvc.perform(delete(serviceUrl + "{id}/", employee.getId()));

        // Then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
