package com.example.employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeController controller;

    @BeforeEach
    public void setup() {
        controller.clear();
    }

    @Test
    void should_return_created_employee_when_post() throws Exception {
        String requestBody = """
                {
                     "name": "John Smith",
                     "age": 32,
                     "gender": "Male",
                     "salary": 5000.0
                }
                """;

        MockHttpServletRequestBuilder request = post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(32))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000.0));
    }

    @Test
    void should_return_employee_when_get_employee_with_id_exist() throws Exception {
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee expect = controller.create(employee);

        MockHttpServletRequestBuilder request = get("/employees/" + expect.id())
                        .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.id()))
                .andExpect(jsonPath("$.name").value(expect.name()))
                .andExpect(jsonPath("$.age").value(expect.age()))
                .andExpect(jsonPath("$.gender").value(expect.gender()))
                .andExpect(jsonPath("$.salary").value(expect.salary()));
    }

    @Test
    void should_return_males_when_list_by_male() throws Exception {
        Employee expect = controller.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        controller.create(new Employee(null, "Lily", 22, "Female", 5000.0));

        MockHttpServletRequestBuilder request = get("/employees?gender=male")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expect.id()))
                .andExpect(jsonPath("$[0].name").value(expect.name()))
                .andExpect(jsonPath("$[0].age").value(expect.age()))
                .andExpect(jsonPath("$[0].gender").value(expect.gender()))
                .andExpect(jsonPath("$[0].salary").value(expect.salary()));
    }

    @Test
    void should_return_employees_when_list_all() throws Exception {
        Employee expect = controller.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        controller.create(new Employee(null, "Lily", 22, "Female", 5000.0));
        controller.create(new Employee(null, "Mary", 33, "Female", 10000.0));

        MockHttpServletRequestBuilder request = get("/employees")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(expect.id()))
                .andExpect(jsonPath("$[0].name").value(expect.name()))
                .andExpect(jsonPath("$[0].age").value(expect.age()))
                .andExpect(jsonPath("$[0].gender").value(expect.gender()))
                .andExpect(jsonPath("$[0].salary").value(expect.salary()));
    }

    @Test
    void should_return_updated_employee_when_put() throws Exception {
        Employee employee = new Employee(null, "John Smith", 25, "Male", 3000.0);
        Employee expect = controller.create(employee);
        String requestBody = """
                {
                     "name": "ethan Smith",
                     "age": 32,
                     "salary": 5000.0
                }
                """;

        MockHttpServletRequestBuilder request = put("/employees/" + expect.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.id()))
                .andExpect(jsonPath("$.name").value("ethan Smith"))
                .andExpect(jsonPath("$.age").value(32))
                .andExpect(jsonPath("$.gender").value(expect.gender()))
                .andExpect(jsonPath("$.salary").value(5000.0));

    }

    @Test
    void should_return_deleted_employee_when_delete() throws Exception {
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee created = controller.create(employee);

        mockMvc.perform(delete("/employees/" + created.id()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employees/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void should_return_page_employees_when_list_page_and_size() throws Exception {
        Employee expect = controller.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        controller.create(new Employee(null, "Lily", 21, "Female", 5000.0));
        controller.create(new Employee(null, "Lily", 22, "Female", 5000.0));
        controller.create(new Employee(null, "Lily", 23, "Female", 5000.0));
        controller.create(new Employee(null, "Lily", 24, "Female", 5000.0));

        MockHttpServletRequestBuilder request = get("/employees?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").value(expect.id()))
                .andExpect(jsonPath("$[0].name").value(expect.name()))
                .andExpect(jsonPath("$[0].age").value(expect.age()))
                .andExpect(jsonPath("$[0].gender").value(expect.gender()))
                .andExpect(jsonPath("$[0].salary").value(expect.salary()));


}
