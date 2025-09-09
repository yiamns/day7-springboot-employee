package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private int id = 0;

    public void clear() {
        employees.clear();
        id = 0;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        int id = ++this.id;
        Employee newEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;
    }

    @GetMapping("{id}")
    public Employee findById(@PathVariable int id) {
        return employees.stream()
                .filter(employee -> employee.id() == id)
                .findFirst()
                .orElse(null);
    }

    @GetMapping
    public List<Employee> index(@RequestParam(required = false) String gender) {
        List<Employee> result = new ArrayList<>();
        if (gender == null){
            return employees;
        }
        for (Employee e : employees) {
            if (e.gender().compareToIgnoreCase(gender) == 0) {
                result.add(e);
            }
        }
        return result;
    }

    @PutMapping("{id}")
    public Employee updateEmployee(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            if (emp.id() == id) {
                String name = emp.name();
                String gender = emp.gender();
                int age = emp.age();
                double salary = emp.salary();

                if (updates.containsKey("name")) {
                    name = (String) updates.get("name");
                }
                if (updates.containsKey("age")) {
                    age = (Integer) updates.get("age");
                }
                if (updates.containsKey("gender")) {
                    gender = (String) updates.get("gender");
                }
                if (updates.containsKey("salary")) {
                    salary = (Double) updates.get("salary");
                }

                Employee updated = new Employee(id, name, age, gender, salary);
                employees.set(i, updated);
                return updated;
            }
        }
        return null;
    }


}
