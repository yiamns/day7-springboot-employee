package com.example.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

        MockHttpServletRequestBuilder request = get("/companies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("spring"))
                .andExpect(jsonPath("$[1].name").value("OOCL"));
    }

    @Test
    void should_return_specific_company_when_get_by_id() throws Exception {
        Company company = controller.create(new Company(null, "spring"));

        MockHttpServletRequestBuilder request = get("/companies/" + company.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.id()))
                .andExpect(jsonPath("$.name").value("spring"));
    }

    @Test
    void should_return_paged_companies_when_get_with_page_and_size() throws Exception {
        for (int i = 0; i < 8; i++) {
            controller.create(new Company(null, "company" + i));
        }

        MockHttpServletRequestBuilder request = get("/companies?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("company5"));
    }

    @Test
    void should_create_company_when_post() throws Exception {
        String requestBody = """
                {
                    "name": "spring"
                }
                """;

        MockHttpServletRequestBuilder request = post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("spring"));
    }

}
