package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private int id = 0;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        int id = ++this.id;
        Employee newEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;
    }
}
