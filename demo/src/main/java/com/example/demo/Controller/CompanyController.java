package com.example.demo.Controller;

import com.example.demo.Domain.Company;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Service.CompanyService;
import com.example.demo.Util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
