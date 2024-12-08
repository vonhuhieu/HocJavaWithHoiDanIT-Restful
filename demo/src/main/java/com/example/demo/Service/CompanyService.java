package com.example.demo.Service;

import com.example.demo.Domain.Company;
import com.example.demo.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company){
        Company newCompany = company;
        this.companyRepository.save(newCompany);
        return newCompany;
    }

    public List<Co>
}
