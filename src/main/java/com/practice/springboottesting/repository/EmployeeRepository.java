package com.practice.springboottesting.repository;

import com.practice.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    @Query("select employee from Employee employee where employee.firstName = ?1 and employee.lastName = ?2")
    Employee findByFirstNameAndLastName(String firstName, String lastName);

    @Query("select employee from Employee employee where employee.firstName =:firstName and employee.lastName =:lastName")
    Employee findByFirstNameAndLastNameParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(value = "select * from employees employee where employee.first_name = ?1 and employee.last_name = ?2", nativeQuery = true)
    Employee findByFirstNameAndLastNameSql(String firstName, String lastName);

    @Query(value = "select * from employees employee where employee.first_name =:firstName and employee.last_name =:lastName", nativeQuery = true)
    Employee findByFirstNameAndLastNameParamsSql(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
