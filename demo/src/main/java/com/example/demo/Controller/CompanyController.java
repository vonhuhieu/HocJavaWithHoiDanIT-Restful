package com.example.demo.Controller;

import com.example.demo.Domain.Company;
import com.example.demo.Domain.DTO.Response.ResultPaginationDTO;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Service.CompanyService;
import com.example.demo.Util.ResponseUtil;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")

public class CompanyController {
    private CompanyService companyService;
    private ResponseUtil responseUtil;

    public CompanyController(CompanyService companyService, ResponseUtil responseUtil) {
        this.companyService = companyService;
        this.responseUtil = responseUtil;
    }

    @PostMapping("/companies")
    public ResponseEntity<RestResponse<Object>> createNewCompany(@Valid @RequestBody Company postManCompany) {
        Company newCompany = this.companyService.createCompany(postManCompany);
        return this.responseUtil.buildCreateResponse("create a company successfully", newCompany);
    }

//    @GetMapping("/companies")
//    public ResponseEntity<RestResponse<Object>> fetchListCompanies(
//            @RequestParam("current") Optional<String> currentOptional,
//            @RequestParam("pageSize") Optional<String> pageSizeOptional
//    ) {
//        String stringCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
//        String stringPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
//        int current = Integer.parseInt(stringCurrent);
//        int pageSize = Integer.parseInt(stringPageSize);
//        Pageable pageable = PageRequest.of(current - 1, pageSize);
//        ResultPaginationDTO listCompanies = this.companyService.fetchListCompanies(pageable);
//        RestResponse res = new RestResponse();
//        res.setStatusCode(HttpStatus.OK.value());
//        res.setMessage("Get companies successfully");
//        res.setData(listCompanies);
//        return ResponseEntity.status(HttpStatus.OK).body(res);
//    }

    @GetMapping("/companies")
    public ResponseEntity<RestResponse<ResultPaginationDTO>> fetchListCompanies(@Filter Specification<Company> specification, Pageable pageable) {
        ResultPaginationDTO listCompanies = this.companyService.fetchListCompanies(specification, pageable);
        RestResponse res = new RestResponse();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Get companies successfully");
        res.setData(listCompanies);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/companies")
    public ResponseEntity<RestResponse<Object>> updateCompany(@Valid @RequestBody Company postManCompany) {
        Company updateCompany = this.companyService.updateCompany(postManCompany);
        return this.responseUtil.buildSuccessResponse("update the company successfully", updateCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<RestResponse<Object>> deleteCompany(@PathVariable("id") long id) {
        this.companyService.deleteCompanyByID(id);
        return this.responseUtil.buildSuccessResponse("Delete the company successfully", null);
    }
}
