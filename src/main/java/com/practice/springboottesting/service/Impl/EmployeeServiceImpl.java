package com.practice.springboottesting.service.Impl;

import com.practice.springboottesting.exception.ResourceNotFoundException;
import com.practice.springboottesting.model.Employee;
import com.practice.springboottesting.repository.EmployeeRepository;
import com.practice.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        Optional<Employee> retrievedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (retrievedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exist with given email:" + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }
}
