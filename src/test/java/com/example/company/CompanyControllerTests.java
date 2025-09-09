package com.example.company;

import com.example.company.Company;
import com.example.company.CompanyController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController controller;

    @BeforeEach
    void setUp() {
        controller.clear();
    }

    @Test
    void should_return_company_list_when_get_companies() throws Exception {

        controller.create(new Company(null, "spring"));
        controller.create(new Company(null, "OOCL"));

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("spring"))
                .andExpect(jsonPath("$[1].name").value("OOCL"));
    }

    @Test
    void should_return_specific_company_when_get_by_id() throws Exception {
        Company company = controller.create(new Company(null, "spring"));

        mockMvc.perform(get("/companies/" + company.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.id()))
                .andExpect(jsonPath("$.name").value("spring"));
    }

}
