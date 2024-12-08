package com.example.demo.Controller;

import com.example.demo.Domain.Company;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Service.CompanyService;
import com.example.demo.Util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {
    private CompanyService companyService;
    private ResponseUtil responseUtil;

    public CompanyController(CompanyService companyService, ResponseUtil responseUtil) {
        this.companyService = companyService;
        this.responseUtil = responseUtil;
    }

    @PostMapping("/companies")
    public ResponseEntity<RestResponse<Object>> createNewCompany(@Valid @RequestBody Company postManCompany){
        Company newCompany = this.companyService.createCompany(postManCompany);
        return this.responseUtil.buildCreateResponse("create a company successfully", newCompany);
    }

    @GetMapping("/companies")
    public ResponseEntity<RestResponse<Object>> fetchListCompanies(){
        List<Company> listCompanies = this.companyService.fetchListCompanies();
        return this.responseUtil.buildSuccessResponse("fetch listcompanies succesfully", listCompanies);
    }

    @PutMapping("/companies")
    public ResponseEntity<RestResponse<Object>> updateCompany(@Valid @RequestBody Company postManCompany){
        Company updateCompany = this.companyService.updateCompany(postManCompany);
        return this.responseUtil.buildSuccessResponse("update the company successfully", updateCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<RestResponse<Object>> deleteCompany(@PathVariable("id") long id) {
        this.companyService.deleteCompanyByID(id);
        return this.responseUtil.buildSuccessResponse("Delete the company successfully", null);
    }
}
