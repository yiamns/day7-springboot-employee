package com.example.company;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final List<Company> companies = new ArrayList<>();
    private int id = 0;

    public void clear() {
        companies.clear();
        id = 0;
    }

    @GetMapping
    public List<Company> list(@RequestParam(required = false, defaultValue = "0") int page,
                              @RequestParam(required = false, defaultValue = "10") int size) {
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, companies.size());
        if (fromIndex >= companies.size()) {
            return new ArrayList<>();
        }
        return companies.subList(fromIndex, toIndex);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        int newId = ++id;
        Company newCompany = new Company(newId, company.name());
        companies.add(newCompany);
        return newCompany;
    }
}
